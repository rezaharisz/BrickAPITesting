package io.onebrick.sdk.model

import com.google.gson.annotations.SerializedName

public data class CommonEwalletPayload(
    @SerializedName("institutionId") val institutionId: Long = 0,
    @SerializedName("redirectRefId") val redirectRefId: String? = null,
    @SerializedName("username") val username: String? = null
)


public data class OVORequestCredentialPayload(
    @SerializedName("deviceId") val deviceId: String? = null,
    @SerializedName("institutionId") val institutionId: String? = null,
    @SerializedName("otpNumber") val otpNumber: String? = null,
    @SerializedName("pin") val pin: String? = null,
    @SerializedName("redirectRefId") val redirectRefId: String? = null,
    @SerializedName("refId") val refId: String? = null,
    @SerializedName("username") val username: String? = null
)
