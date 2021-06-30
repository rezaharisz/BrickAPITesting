@file:Suppress("DEPRECATION")

package io.onebrick.sdk.ui.ewallet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import io.onebrick.sdk.CoreBrickSDK
import io.onebrick.sdk.IRequestTransactionResult
import io.onebrick.sdk.R
import io.onebrick.sdk.model.AuthenticateUserResponse
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.model.MFABankingPayload
import io.onebrick.sdk.ui.common.BaseActivity
import com.goodiebag.pinview.Pinview

class EWalletPINActivity : BaseActivity() {

    private lateinit var textTitle: TextView
    private lateinit var textSubtitle: TextView
    private lateinit var passwordTextField: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var pinTextField:Pinview
    private lateinit var showHideImage: ImageView
    private var isOTPComplete:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_e_wallet_p_i_n)
        textTitle = findViewById(R.id.text_title)
        textSubtitle = findViewById(R.id.empty_string)
        passwordTextField = findViewById(R.id.password_text)
        buttonSubmit = findViewById(R.id.submit_button)
        showHideImage = findViewById(R.id.show_pass_btn)
        pinTextField =  findViewById(R.id.squareField1)

        textTitle.text = ConfigStorage.institutionData.bankName
        pinTextField.setPinViewEventListener { _, _ ->
            isOTPComplete = true
            checkingField()
        }

        passwordTextField.addTextChangedListener(generalTextWatcher)

        buttonSubmit.setOnClickListener{
            submitPIN()
        }
        showBackButton()
        initCloseButton()
        showErrorMessage(false, "")
        showSuccessMessage(false, "")
        if (intent.getStringExtra("showNotif") == "true") {
            showSuccessMessage(true, getString(R.string.otpSuccess))
        }

        initLanguageButton(baseContext,this)
    }

    fun checkingField() {
        if (isOTPComplete && passwordTextField.text.trim().isNotEmpty() ) {
            buttonSubmit.isEnabled = true
            buttonSubmit.setBackgroundColor(buttonSubmit.context.resources.getColor(R.color.OrangeRed))
        } else {
            buttonSubmit.setBackgroundColor(buttonSubmit.context.resources.getColor(R.color.Gray3))
            buttonSubmit.isEnabled = false
        }
    }

    private fun submitPIN() {
        showLoadingActivity()

        val payload = MFABankingPayload(
            pin = passwordTextField.text.toString(),
            otpNumber = pinTextField.value.toString(),
            deviceId = ConfigStorage.responseOTPRequest.deviceId,
            refId = ConfigStorage.responseOTPRequest.refId,

            institutionId = ConfigStorage.institutionData.id,
            redirectRefId = ConfigStorage.redirectReffId,
            username = ConfigStorage.currentPhoneNumber
        )
        CoreBrickSDK.submitCredentialsForMFAAccount(payload,object: IRequestTransactionResult {

            override fun success(response: AuthenticateUserResponse?) {
                dismissLoadingActivity()
                redirectToThankYouPage()
            }

            override fun error(t: Throwable?) {
                dismissLoadingActivity()
                showErrorMessage(true,getString(R.string.securityNotValid))
                showSuccessMessage(false,"")
            }

        })
    }
    private val generalTextWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            Log.v("BRICK", buttonSubmit.isEnabled.toString())
            checkingField()
        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int,
            after: Int
        ) {

        }

        override fun afterTextChanged(s: Editable) {

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
}