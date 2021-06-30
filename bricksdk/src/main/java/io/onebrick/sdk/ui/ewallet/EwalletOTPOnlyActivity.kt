@file:Suppress("DEPRECATION")

package io.onebrick.sdk.ui.ewallet

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import io.onebrick.sdk.CoreBrickSDK
import io.onebrick.sdk.IRequestTransactionResult
import io.onebrick.sdk.R
import io.onebrick.sdk.model.AuthenticateUserResponse
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.model.MFABankingPayload
import io.onebrick.sdk.ui.common.BaseActivity
import com.goodiebag.pinview.Pinview


class EwalletOTPOnlyActivity : BaseActivity() {

    private lateinit var textTitle: TextView
    private lateinit var textSubtitle: TextView
    private lateinit var buttonSubmit: Button
    private lateinit var pinTextField: Pinview
    private var isOTPComplete:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ewallet_o_t_p_only)

        textTitle = findViewById(R.id.text_title)
        textSubtitle = findViewById(R.id.empty_string)
        buttonSubmit = findViewById(R.id.submit_button)
        pinTextField =  findViewById(R.id.squareField1)

        textTitle.text = ConfigStorage.institutionData.bankName
        pinTextField.setPinViewEventListener { _, _ ->
            isOTPComplete = true
            checkingField()
        }

        buttonSubmit.setOnClickListener{
            submitOTP(pinTextField.value.toString())
        }
        showBackButton()
        initCloseButton()
        showErrorMessage(false, "")
        showSuccessMessage(false, "")
        if (intent.getStringExtra("showNotif") == "true") {
            showSuccessMessage(true, getString(R.string.otpSuccess))
        }

        initLanguageButton(baseContext, this)
    }

    override fun onPause() {
        super.onPause()
        showSuccessMessage(false, "")
    }
    private fun checkingField() {
        if (isOTPComplete ) {
            buttonSubmit.isEnabled = true
            buttonSubmit.setBackgroundColor(buttonSubmit.context.resources.getColor(R.color.OrangeRed))
        } else {
            buttonSubmit.setBackgroundColor(buttonSubmit.context.resources.getColor(R.color.Gray3))
            buttonSubmit.isEnabled = false
        }
    }

    private fun submitOTP(otp: String) {
        showLoadingActivity()

        val payload = MFABankingPayload(
            institutionId = ConfigStorage.institutionData.id,
            otp = otp,
            otpToken = ConfigStorage.responseOTPRequest.otpToken,
            redirectRefId = ConfigStorage.redirectReffId,
            sessionId = ConfigStorage.userSessionToken,
            uniqueId = ConfigStorage.responseOTPRequest.uniqueId,
            username = ConfigStorage.currentPhoneNumber
        )

//        var userProperties = JSONObject()
//
//        var eventProperties = JSONObject()

//        TrackingManager.trackEvent(login_visited,applicationContext,application,eventProperties,userProperties)
//        userProperties.put("client_id",ConfigStorage.accessTokenRequest.data.clientId)
//        userProperties.put("client_email",ConfigStorage.accessTokenRequest.data.clientEmail)
//        userProperties.put("bank_id",ConfigStorage.institutionData.id.toString())
//        userProperties.put("bank_name",ConfigStorage.institutionData.id.toString())
//
//        eventProperties.put("bank_name",ConfigStorage.institutionData.bankName.toString())
//        eventProperties.put("name",ConfigStorage.accessTokenRequest.data.clientName)
//        eventProperties.put("bank_name",ConfigStorage.institutionData.bankName.toString())
//        eventProperties.put("client_full_name",ConfigStorage.accessTokenRequest.data.clientFullName)
//        TrackingManager.trackEvent(otp_submitted,applicationContext,application,eventProperties,userProperties)

        CoreBrickSDK.submitCredentialsForMFAAccount(payload, object : IRequestTransactionResult {

            override fun success(response: AuthenticateUserResponse?) {
                dismissLoadingActivity()
                redirectToThankYouPage()
            }

            override fun error(t: Throwable?) {
                dismissLoadingActivity()
                showErrorMessage(true, getString(R.string.securityNotValid))
                showSuccessMessage(false, "")
            }

        })
    }


}