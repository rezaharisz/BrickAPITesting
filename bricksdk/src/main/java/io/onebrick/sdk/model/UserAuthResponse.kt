package io.onebrick.sdk.model

data class AuthenticateUserResponse (
        val status: Long? = null,
        val message: String? = null,
        val data: AuthenticateUserResponseData? = null
)


data class AuthenticateUserResponseData (
        val accessToken: String? = null,
        val bankId: String? = null,
        val userId: Any? = null,
        val target: String? = null,
        val sessionId: String? = null,
        val requestId: String? = null,
        val token: Any? = null,
        val duration: Long? = null,
        val refId: String? = null,
        val deviceId: String? = null,
        val uniqueId: String? = null,
        val otpToken: String? = null
)

