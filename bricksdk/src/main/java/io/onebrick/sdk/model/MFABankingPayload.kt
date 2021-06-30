package io.onebrick.sdk.model

import com.google.gson.annotations.SerializedName

data class MFABankingPayload(
    @SerializedName("institutionId") val institutionId: Long = 0,
    @SerializedName("redirectRefId") val redirectRefId: String? = null,
    @SerializedName("duration") val duration: Long? = 0,
    @SerializedName("username") val username: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("sessionId") val sessionId: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("deviceId") val deviceId: String? = null,
    @SerializedName("otpNumber") val otpNumber: String? = null,
    @SerializedName("otp") val otp: String? = null,
    @SerializedName("otpToken") val otpToken: String? = null,
    @SerializedName("pin") val pin: String? = null,
    @SerializedName("refId") val refId: String? = null,
    @SerializedName("uniqueId") val uniqueId: String? = null
)

