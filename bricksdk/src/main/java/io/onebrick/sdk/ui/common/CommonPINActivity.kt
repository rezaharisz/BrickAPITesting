package io.onebrick.sdk.ui.common

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.goodiebag.pinview.Pinview
import io.onebrick.sdk.CoreBrickSDK
import io.onebrick.sdk.IRequestTransactionResult
import io.onebrick.sdk.R
import io.onebrick.sdk.model.AuthenticateUserResponse
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.model.MFABankingPayload


class CommonPINActivity : BaseActivity() {
    private lateinit var textTitle: TextView
    private lateinit var pinTextField: Pinview
    private lateinit var textSubtitle: TextView
    private lateinit var buttonSubmit: Button
    private lateinit var resend:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_p_i_n)

        pinTextField =  findViewById<Pinview>(R.id.squareField1)
        textTitle = findViewById<TextView>(R.id.text_title)
        textSubtitle = findViewById<TextView>(R.id.empty_string)
        buttonSubmit = findViewById<Button>(R.id.submit_button)
        resend =  findViewById<TextView>(R.id.resend_verification)
        textTitle.text = ConfigStorage.institutionData.bankName

        pinTextField.setPinViewEventListener(Pinview.PinViewEventListener { pinview, fromUser -> //Make api calls here or what not
            submitPIN(pinTextField.value.toString())
        })

        showBackButton()
        initCloseButton()
        initLanguageButton(baseContext,this)
        resend.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
               showLoadingActivity()
                CoreBrickSDK.requestResendOTP(object : IRequestTransactionResult {
                    override fun success(credentials: AuthenticateUserResponse?) {
                        dismissLoadingActivity()
                        showToast("OTP Has been sent, please check your inbox")
                    }

                    override fun error(t: Throwable?) {
                        dismissLoadingActivity()
                        showToast("Something Wrong")
                    }

                })
            }
        })


    }

    fun submitPIN(pin: String) {
        showLoadingActivity()
        buttonSubmit.isEnabled = true
        buttonSubmit.setBackgroundColor(this.buttonSubmit.context.resources.getColor(R.color.OrangeRed))

        val payload = MFABankingPayload(
                token = pin,
                duration = ConfigStorage.responseOTPRequest.duration,
                institutionId = ConfigStorage.institutionData.id,
                redirectRefId = ConfigStorage.redirectReffId,
                sessionId = ConfigStorage.userSessionToken,
                username = ConfigStorage.currentUCU,
                password = ConfigStorage.currentUCP
        )
        CoreBrickSDK.submitCredentialsForMFAAccount(payload, object : IRequestTransactionResult {

            override fun success(credentials: AuthenticateUserResponse?) {
                dismissLoadingActivity()
                redirectToThankYouPage()
            }

            override fun error(t: Throwable?) {
                dismissLoadingActivity()
                showToast("Something Wrong")
            }

        })
    }
}

