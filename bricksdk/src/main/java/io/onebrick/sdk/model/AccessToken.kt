package io.onebrick.sdk.model

public data class AccessToken(
        val status: Long,
        val message: String,
        val data: AccessTokenData
)

public data class AccessTokenData (
    val access_token: String
)
