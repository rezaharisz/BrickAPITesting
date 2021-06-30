package com.demo.brick.coresdkdemo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import io.onebrick.sdk.*
import io.onebrick.sdk.model.AccessToken
import io.onebrick.sdk.model.AuthenticateUserResponse
import io.onebrick.sdk.model.AuthenticateUserResponseData
import io.onebrick.sdk.util.Environment

class MainActivity : Activity(), ICoreBrickUISDK {
    private val clientId = "3b3a0ab3-41e5-499b-b39b-c16ce8f24de8"
    private val clientSecret = "0tIwEufrDPCie209lV5GakJhd8FHsf"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    private  var coreBrickUIDelegate: ICoreBrickUISDK? = null

    companion object{
        const val TOKEN = "token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coreBrickUIDelegate = this
        Log.v("BRICK", coreBrickUIDelegate.toString())

        CoreBrickUISDK.initializedUISDK( applicationContext, clientId, clientSecret, name, url,
            coreBrickUIDelegate!!, Environment.SANDBOX)

        val buttonAccessToken = findViewById<Button>(R.id.button_access_token)
        buttonAccessToken.setOnClickListener {
                CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.SANDBOX)
        }

        val buttonInstitution = findViewById<Button>(R.id.button_institution)

        buttonInstitution.setOnClickListener {
          CoreBrickSDK.requestAccessToken(object : IAccessTokenRequestResult {
              override fun success(accessToken: AccessToken?) {
                  Log.e(TOKEN, accessToken.toString())
              }

              override fun error(t: Throwable?) {
                  Log.e(TOKEN, t.toString())
              }

          })
        }
        val buttonAuthUser = findViewById<Button>(R.id.button_auth_user)
        buttonAuthUser.setOnClickListener {
           CoreBrickSDK.authenticateUser("someUser","somePassword","1",object:
               IRequestResponseUserAuth {
               override fun success(response: AuthenticateUserResponse) {
                   Log.e(TOKEN, response.toString())
               }

               override fun error(t: Throwable?) {
                   Log.e(TOKEN, t.toString())
               }
           })
        }

        val buttonListAccount = findViewById<Button>(R.id.button_list_account)
        buttonListAccount.setOnClickListener {
            CoreBrickSDK.listAccountUser()
        }

        val buttonDemoUi = findViewById<Button>(R.id.button_demo_ui)
        buttonDemoUi.setOnClickListener {
            CoreBrickUISDK.initializedUISDK( applicationContext, clientId, clientSecret, name, url,
                this.coreBrickUIDelegate!!, Environment.SANDBOX)
        }


    }

    override fun onTransactionSuccess(transactionResult: AuthenticateUserResponseData) {
        Log.v("BRICKMAIN", transactionResult.toString())
    }

}