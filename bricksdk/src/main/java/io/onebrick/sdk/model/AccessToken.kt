package io.onebrick.sdk.model

data class AccessToken(
        val status: Long,
        val message: String,
        val data: AccessTokenData
)

data class AccessTokenData (
    val access_token: String
)
