package io.onebrick.sdk.ui.common

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import io.onebrick.sdk.CoreBrickSDK
import io.onebrick.sdk.IRequestSubmitInstitution
import io.onebrick.sdk.util.TrackingManager
import io.onebrick.sdk.*
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.model.InstitutionResponseSubmit
import io.onebrick.sdk.util.insti_suggest_submitted
import org.json.JSONObject


class SubmitInstitutionActivity : BaseActivity() {

    lateinit var spinner:Spinner
    private lateinit var institutionName: EditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_institution)

        spinner = findViewById(R.id.spinner_dropdown)
        buttonSubmit = findViewById(R.id.submit_button)
        institutionName = findViewById(R.id.insititution_name)

        buttonSubmit.isEnabled =  false


        institutionName.addTextChangedListener(generalTextWatcher)
        initLanguageButton(baseContext, this)
        showBackButton()
        initCloseButton()
        showSuccessMessage(false,"")
        showErrorMessage(false,"")
        addDataToSpinner()

        buttonSubmit.setOnClickListener {
            showLoadingActivity()

            var eventProperties = JSONObject()
            eventProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
            eventProperties.put("email", ConfigStorage.accessTokenRequest.data.clientEmail)

            var userProperties = JSONObject()
            userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
            userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)
            TrackingManager.trackEvent(insti_suggest_submitted,applicationContext,application,eventProperties,userProperties)

            CoreBrickSDK.submitInstitution(
                institutionName.text.toString(),
                spinner.selectedItem.toString().toLowerCase(), object : IRequestSubmitInstitution {

                    override fun success(credentials: InstitutionResponseSubmit?) {
                        dismissLoadingActivity()
                        redirectToSuccessPage()
                    }

                    override fun error(t: Throwable?) {
                        dismissLoadingActivity()
                        showErrorMessage(
                            true,
                            "Institution Cannot be submitted"
                        )
                    }

                })
        }
    }

    fun redirectToSuccessPage() {
        val brickCoreUIIntent = Intent(this, SubmitInstituionSuccessActivity::class.java)
        brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(brickCoreUIIntent)
    }

    private fun addDataToSpinner() {
        var list: List<String> = ArrayList()
        ConfigStorage.institutionList.data.groupBy { it.type}.forEach{ it
            list +=it.key
        }
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, list
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter
    }

    private val generalTextWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            Log.v("BRICK", buttonSubmit.isEnabled.toString())
            if (institutionName.text.trim().isNotEmpty() ) {
                buttonSubmit.isEnabled = true
                buttonSubmit.setBackgroundColor(buttonSubmit.context.resources.getColor(R.color.OrangeRed))
            } else {
                buttonSubmit.setBackgroundColor(buttonSubmit.context.resources.getColor(R.color.Gray3))
                buttonSubmit.isEnabled = false
            }
        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int,
            after: Int
        ) {

        }

        override fun afterTextChanged(s: Editable) {

        }
    }
}