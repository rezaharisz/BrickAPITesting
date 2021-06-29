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
    private var coreSDK: CoreBrickUISDK? = null
    private val url = "https://onebrick.io"
    private  var coreBrickUIDelegate: ICoreBrickUISDK? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coreBrickUIDelegate = this as ICoreBrickUISDK
        Log.v("BRICK", coreBrickUIDelegate.toString())

        CoreBrickUISDK.initializedUISDK( applicationContext, clientId, clientSecret, name, url,
            coreBrickUIDelegate!!, Environment.SANDBOX)

        val button_access_token = findViewById<Button>(R.id.button_access_token)
        button_access_token.setOnClickListener {
                CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.SANDBOX)
        }

        val button_institution = findViewById<Button>(R.id.button_institution)

        button_institution.setOnClickListener {
          CoreBrickSDK.requestAccessToken(object : IAccessTokenRequestResult {
              override fun success(accessToken: AccessToken?) {
              }

              override fun error(t: Throwable?) {
              }

          })
        }
        val button_auth_user = findViewById<Button>(R.id.button_auth_user)
        button_auth_user.setOnClickListener {
           CoreBrickSDK.authenticateUser("someUser","somePassword","1",object:
               IRequestResponseUserAuth {
               override fun success(response: AuthenticateUserResponse) {
                  //// you need to handle response.status here
                   //// if status response 200 then you can use
                   /// if status response 428 then go to next step

               }

               override fun error(t: Throwable?) {

               }

           })
        }

        val button_list_account = findViewById<Button>(R.id.button_list_account)
        button_list_account.setOnClickListener {
            CoreBrickSDK.listAccountUser()
        }



        val button_demo_ui = findViewById<Button>(R.id.button_demo_ui)
        button_demo_ui.setOnClickListener {
            CoreBrickUISDK.initializedUISDK( applicationContext, clientId, clientSecret, name, url,
                this.coreBrickUIDelegate!!, Environment.SANDBOX)
        }


    }

    override fun onTransactionSuccess(transactionResult: AuthenticateUserResponseData) {
        Log.v("BRICKMAIN", transactionResult.toString())
    }

}