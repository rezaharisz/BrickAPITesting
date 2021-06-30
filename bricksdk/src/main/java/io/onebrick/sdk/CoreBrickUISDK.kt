@file:Suppress("CAST_NEVER_SUCCEEDS")

package io.onebrick.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import io.onebrick.sdk.model.AuthenticateUserResponseData
import io.onebrick.sdk.ui.common.LandingActivity
import io.onebrick.sdk.util.Environment

interface ICoreBrickUISDK  {
    fun onTransactionSuccess(transactionResult: AuthenticateUserResponseData)
}

class CoreBrickUISDK {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var contextParent: Context
        private var coreUIInterface: ICoreBrickUISDK? = null
        fun initializedUISDK(
                context: Context,
                clientId: String,
                secret: String,
                name:String,
                url:String,
                delegate: ICoreBrickUISDK,
                environment: Environment) {
            contextParent = context
            coreUIInterface = delegate
            CoreBrickSDK.initializedSDK(clientId, secret, name, url, environment)

            val brickCoreUIIntent = Intent()
            brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            brickCoreUIIntent.setClass(contextParent, LandingActivity::class.java)
            contextParent.startActivity(brickCoreUIIntent)
        }

        fun delegatingBackResult(result: AuthenticateUserResponseData) {

            if(contextParent is ICoreBrickUISDK) coreUIInterface = this as ICoreBrickUISDK
            Log.v("BRICK", result.toString())
            Log.v("BRICK", coreUIInterface.toString())
            Log.v("BRICK", contextParent.toString())
            coreUIInterface?.onTransactionSuccess(result)
        }


    }
}