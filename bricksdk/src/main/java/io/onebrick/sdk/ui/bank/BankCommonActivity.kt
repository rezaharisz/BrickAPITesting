@file:Suppress("DEPRECATION")

package io.onebrick.sdk.ui.bank

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import io.onebrick.sdk.util.TrackingManager
import io.onebrick.sdk.CoreBrickSDK
import io.onebrick.sdk.IRequestResponseUserAuth
import io.onebrick.sdk.R
import io.onebrick.sdk.model.AuthenticateUserResponse
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.ui.common.BaseActivity
import io.onebrick.sdk.util.MOCKBANK
import io.onebrick.sdk.util.login_visited
import io.onebrick.sdk.ui.common.CommonBadgeFragment
import org.json.JSONObject
import java.util.*


class BankCommonActivity : BaseActivity() {

    private lateinit var textTitle:TextView
    private lateinit var textSubtitle:TextView
    private lateinit var usernameTextField:EditText
    private lateinit var passwordTextField:EditText
    private lateinit var buttonSubmit: Button
    private lateinit var showHideImage: ImageView
    private lateinit var badge: CommonBadgeFragment
    private lateinit var mockBankFrame:FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_common)

        textTitle = findViewById(R.id.text_title)
        textSubtitle = findViewById(R.id.empty_string)
        usernameTextField = findViewById(R.id.user_id_text)
        passwordTextField = findViewById(R.id.password_text)
        buttonSubmit = findViewById(R.id.submit_button)
        showHideImage = findViewById(R.id.show_pass_btn)

        textTitle.text = ConfigStorage.institutionData.bankName
        textSubtitle.text = String.format(
            getString(R.string.inputCredentials),
            ConfigStorage.institutionData.bankName
        )

        buttonSubmit.isEnabled =  false
        badge = CommonBadgeFragment()
        usernameTextField.addTextChangedListener(generalTextWatcher)
        passwordTextField.addTextChangedListener(generalTextWatcher)

        val userProperties = JSONObject()
        userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
        userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

        val eventProperties = JSONObject()
        eventProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
        eventProperties.put("bank_id", ConfigStorage.institutionData.id.toString())
        eventProperties.put("public_token", ConfigStorage.barrierToken)

        TrackingManager.trackEvent(login_visited,applicationContext,application,eventProperties,userProperties)

        mockBankFrame = findViewById(R.id.mockbank_frame)
        showBackButton()
        initCloseButton()
        showErrorMessage(false,"")
        showSuccessMessage(false,"")
        initLanguageButton(baseContext, this)
        buttonSubmit.setOnClickListener {
            showLoadingActivity()


           CoreBrickSDK.authenticateUser(
               usernameTextField.text.toString(),
               passwordTextField.text.toString(),
               ConfigStorage.institutionData.id.toString(),
               object : IRequestResponseUserAuth {

                   override fun success(response: AuthenticateUserResponse) {
                        if (response.status?.compareTo(200.0) == 0) {
                            redirectToThankYouPage()
                        } else if(response.status?.compareTo(428) == 0) {
                            ConfigStorage.currentUCP = passwordTextField.text.toString()
                            ConfigStorage.currentUCU = usernameTextField.text.toString()
                            redirectToMFAPage()
                        }
                   }

                   override fun error(t: Throwable?) {
                      dismissLoadingActivity()
                       val message = getString(R.string.invalidErr)
                      showErrorMessage(true,String.format(
                          message
                      ))
                   }

               })
        }

        if (ConfigStorage.institutionData.bankName.toLowerCase(Locale.ROOT) != MOCKBANK) {
            mockBankFrame.visibility = View.GONE
        }

    }

    fun showHidePassword(view: View){

        if(view.id ==R.id.show_pass_btn){
            if(passwordTextField.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                passwordTextField.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else{
                passwordTextField.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }
    }



    private val generalTextWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            Log.v("BRICK", buttonSubmit.isEnabled.toString())
            if (usernameTextField.text.trim().isNotEmpty() && passwordTextField.text.trim().isNotEmpty() ) {
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