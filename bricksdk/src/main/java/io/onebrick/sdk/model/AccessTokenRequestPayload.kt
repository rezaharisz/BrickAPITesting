package io.onebrick.sdk.model

import com.google.gson.annotations.SerializedName

public data class AccessTokenRequestPayload (
        @SerializedName("accessToken") val accessToken: String,
        @SerializedName("userId") val userId: String,
        @SerializedName("redirectUrl") val redirectUrl: String
)