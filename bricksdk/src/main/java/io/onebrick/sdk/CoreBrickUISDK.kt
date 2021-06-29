package io.onebrick.sdk

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import io.onebrick.sdk.model.AuthenticateUserResponseData
import io.onebrick.sdk.ui.common.LandingActivity
import io.onebrick.sdk.util.Environment

public interface ICoreBrickUISDK  {
    fun onTransactionSuccess(transactionResult: AuthenticateUserResponseData)
}

public class CoreBrickUISDK {

    companion object {
        var contextParent:Context? = null
        var coreUIInterface: ICoreBrickUISDK? = null
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
            brickCoreUIIntent.setClass(contextParent!!, LandingActivity::class.java)
            contextParent!!.startActivity(brickCoreUIIntent)
        }

        fun delegatingBackResult(result: AuthenticateUserResponseData, context: Context) {

            if(contextParent is ICoreBrickUISDK) coreUIInterface = this as ICoreBrickUISDK
            Log.v("BRICK", result.toString())
            Log.v("BRICK", coreUIInterface.toString())
            Log.v("BRICK", contextParent.toString())
            coreUIInterface?.onTransactionSuccess(result)
        }


    }
}