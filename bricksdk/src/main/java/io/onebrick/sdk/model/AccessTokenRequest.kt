package io.onebrick.sdk.model

data class AccessTokenRequest (
    val status: Long,
    val message: String,
    val data: AccessTokenRequestData
)

data class AccessTokenRequestData (
    val clientId: Long,
    val clientName: String,
    val clientAlias: String,
    val clientImageUrl: String,
    val clientEmail:String,
    val clientFullName:String,
    val clientFavicon: String,
    val redirectRefId: Long
)
