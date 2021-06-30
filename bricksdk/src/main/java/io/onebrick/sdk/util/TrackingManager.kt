@file:Suppress("NAME_SHADOWING")

package io.onebrick.sdk.util

import android.app.Application
import android.content.Context
import android.util.Log
import com.amplitude.api.Amplitude
import io.onebrick.sdk.model.ConfigStorage
import org.json.JSONObject


class TrackingManager {

    companion object  {
        fun trackEvent(name: String,
                       context: Context,
                       application: Application,
                       eventProperties: JSONObject,
                       userProperties:JSONObject) {

            val client = Amplitude.getInstance()
                .initialize(context, ConfigStorage.ampAPIKEY)
                .enableForegroundTracking(application)

            val userProperties = userProperties
            userProperties.put("source", SOURCE)


            Log.v("Brick", eventProperties.toString())
            client.setUserProperties(userProperties)
            client.logEvent(name, eventProperties)
        }
    }
}