@file:Suppress("DEPRECATION")

package io.onebrick.sdk.ui.ewallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import io.onebrick.sdk.util.TrackingManager
import io.onebrick.sdk.CoreBrickSDK
import io.onebrick.sdk.IRequestTransactionResult
import io.onebrick.sdk.R
import io.onebrick.sdk.model.AuthenticateUserResponse
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.model.MFABankingPayload
import io.onebrick.sdk.ui.common.BaseActivity
import io.onebrick.sdk.util.OVO
import io.onebrick.sdk.util.login_visited
import org.json.JSONObject
import java.util.*

class EwalletActivity : BaseActivity() {

    private lateinit var textTitle: TextView
    private lateinit var textSubtitle: TextView
    private lateinit var usernameTextField: EditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ewallet)

        textTitle = findViewById(R.id.text_title)
        textSubtitle = findViewById(R.id.empty_string)
        usernameTextField = findViewById(R.id.user_id_text)
        buttonSubmit = findViewById(R.id.submit_button)

        textTitle.text = ConfigStorage.institutionData.bankName
        textSubtitle.text = String.format(getString(R.string.commonEwallet), ConfigStorage.institutionData.bankName)
        usernameTextField.addTextChangedListener(generalTextWatcher)

        buttonSubmit.setOnClickListener {
            submitPhoneNumber(usernameTextField.text.toString())
        }
        showBackButton()
        initCloseButton()
        showErrorMessage(false,"")
        showSuccessMessage(false,"")
        initLanguageButton(baseContext,this)

        val userProperties = JSONObject()
        userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
        userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

        val eventProperties = JSONObject()
        eventProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
        eventProperties.put("email", ConfigStorage.accessTokenRequest.data.clientEmail)
        eventProperties.put("bank_id", ConfigStorage.institutionData.id.toString())
        eventProperties.put("public_token", ConfigStorage.barrierToken)

        TrackingManager.trackEvent(login_visited,applicationContext,application,eventProperties,userProperties)

    }

    private fun submitPhoneNumber(phoneNumber:String) {
        Log.v("BRICK", ConfigStorage.institutionData.bankName)
        Log.v("BRICK", phoneNumber)
        showLoadingActivity()
        val payload = MFABankingPayload(
            username = phoneNumber,
            institutionId = ConfigStorage.institutionData.id,
            redirectRefId = ConfigStorage.redirectReffId

        )


        CoreBrickSDK.authenticateEwalletUser(payload,object: IRequestTransactionResult {
            override fun success(response: AuthenticateUserResponse?) {
                Log.v("BRICK", response.toString())
                Log.v("BRICK", ConfigStorage.institutionData.toString())
                Log.v("BRICK", ConfigStorage.institutionData.bankName)
                ConfigStorage.currentPhoneNumber = phoneNumber
                dismissLoadingActivity()
                return if(ConfigStorage.institutionData.bankName.toUpperCase(Locale.getDefault()) == OVO)  {
                    Log.v("BRICK", "redirectToPINandOTP")
                    redirectToPINandOTP()
                } else {
                    redirectOTPOnly()
                }
            }
            override fun error(t: Throwable?) {
                dismissLoadingActivity()
                showErrorMessage(true,getString(R.string.phoneNotRegistered))
            }
        })

    }

    fun redirectOTPOnly() {
        val brickCoreUIIntent = Intent(this, EwalletOTPOnlyActivity::class.java)
        brickCoreUIIntent.putExtra("showNotif","true")
        brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(brickCoreUIIntent)
    }
    fun redirectToPINandOTP() {
        val brickCoreUIIntent = Intent(this, EWalletPINActivity::class.java)
        brickCoreUIIntent.putExtra("showNotif","true")
        brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(brickCoreUIIntent)
    }

    private val generalTextWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            Log.v("BRICK", buttonSubmit.isEnabled.toString())
            if (usernameTextField.text.trim().isNotEmpty()) {
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