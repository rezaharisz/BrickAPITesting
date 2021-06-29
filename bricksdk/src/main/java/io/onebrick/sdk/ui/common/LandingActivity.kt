package io.onebrick.sdk.ui.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import io.onebrick.sdk.CoreBrickSDK
import io.onebrick.sdk.IAccessTokenRequestResult
import io.onebrick.sdk.ICoreBrickUISDK
import io.onebrick.sdk.IRequestTokenCredentials
import io.onebrick.sdk.*
import io.onebrick.sdk.model.AccessToken
import io.onebrick.sdk.model.AccessTokenRequest
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.ui.institutions.ListInstitution
import io.onebrick.sdk.util.TrackingManager
import io.onebrick.sdk.util.consent_allowed
import io.onebrick.sdk.util.consent_cancel
import io.onebrick.sdk.util.consent_page_visited
import com.squareup.picasso.Picasso
import org.json.JSONObject


class LandingActivity : BaseActivity() {

    private lateinit var checkmarkButton: CheckBox
    private lateinit var acceptButton: Button
    private lateinit var cancelButton: Button
    private lateinit var welcomeLabel: TextView
    private lateinit var accessPermission: TextView
    private lateinit var brandLogo: ImageView
    private  var coreBrickUIDelegate: ICoreBrickUISDK? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.fetchCredentials()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)


        checkmarkButton = findViewById<CheckBox>(R.id.checkBox)
        acceptButton = findViewById<Button>(R.id.allow_button)
        cancelButton = findViewById(R.id.button_cancel)
        welcomeLabel = findViewById(R.id.text_title)
        brandLogo = findViewById(R.id.brand_logo)
        accessPermission = findViewById(R.id.empty_string)
        acceptButton.isEnabled = false
        hideBackButton()
        showSuccessMessage(false, "")
        showErrorMessage(false, "")
        initCloseButton()

        initLanguageButton(baseContext, this,true)

        checkmarkButton.setOnClickListener{
            acceptButton.isEnabled = checkmarkButton.isChecked
                if (checkmarkButton.isChecked) {
                    acceptButton.setBackgroundColor(this.acceptButton.context.resources.getColor(R.color.OrangeRed))
                } else {
                    acceptButton.setBackgroundColor(this.acceptButton.context.resources.getColor(R.color.Gray3))
                }
        }

        acceptButton.setOnClickListener {
            var userProperties = JSONObject()
            userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
            userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

            var eventProperties = JSONObject()
            eventProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
            eventProperties.put("public_token", ConfigStorage.barrierToken)

            TrackingManager.trackEvent(consent_allowed,applicationContext,application,eventProperties,userProperties)


            val brickCoreUIIntent = Intent(this, ListInstitution::class.java)
            brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(brickCoreUIIntent)
        }
        cancelButton.setOnClickListener {

            var userProperties = JSONObject()
            userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
            userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

            var eventProperties = JSONObject()
            eventProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
            eventProperties.put("public_token", ConfigStorage.barrierToken)

            TrackingManager.trackEvent(consent_cancel,applicationContext,application,eventProperties,userProperties)
           finish()
        }
        if(intent.getBooleanExtra("CLOSE_TO_MAIN",false)) {
            CoreBrickUISDK.delegatingBackResult(ConfigStorage.responseOTPRequest,baseContext)
            finish();
        }
    }

    override fun initCloseButton() {
        val view = findViewById<View>(R.id.toolbarCommon)
        var closeButton:Button = view.findViewById(R.id.close_button) as Button
        closeButton.setOnClickListener{
            Log.v("BRICK",coreBrickUIDelegate.toString())
           // CoreBrickUISDK.delegatingBackResult(ConfigStorage.responseOTPRequest,baseContext)
            finish()
        }
    }
    private fun fetchCredentials() {
        ////loading here
        showLoadingActivity()
        CoreBrickSDK.requestAccessToken(object : IAccessTokenRequestResult {
            override fun success(accessToken: AccessToken?) {

                CoreBrickSDK.requestTokenCredentials(
                    object : IRequestTokenCredentials {
                        override fun success(accessToken: AccessTokenRequest?) {

                            var userProperties = JSONObject()
                            userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
                            userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

                            var eventProperties = JSONObject()
                            eventProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
                            eventProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

                            TrackingManager.trackEvent(consent_page_visited,applicationContext,application,eventProperties,userProperties)

                            dismissLoadingActivity()

                            welcomeLabel.text = String.format(
                                getString(R.string.permissionTitle),
                                accessToken?.data?.clientName
                            )

                            accessPermission.text = String.format(
                                getString(R.string.accessPermission),
                                accessToken?.data?.clientName
                            )

                            Picasso.get().load(accessToken?.data?.clientImageUrl).into(brandLogo)

                            cancelButton.text = String.format(
                                getString(R.string.backToAppName),
                                accessToken?.data?.clientName
                            )
                        }

                        override fun error(t: Throwable?) {
                            dismissLoadingActivity()
                            showToast("Something Wrong")
                        }
                    })

            }

            override fun error(t: Throwable?) {
                dismissLoadingActivity()
                showToast("Something Wrong")
            }
        })
    }

}