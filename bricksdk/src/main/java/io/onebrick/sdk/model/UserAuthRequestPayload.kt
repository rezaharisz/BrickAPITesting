package io.onebrick.sdk.model

import com.google.gson.annotations.SerializedName

public data class UserAuthRequestPayload(
        @SerializedName("institutionId") val institutionId: String? = null,
        @SerializedName("username") val username: String? = null,
        @SerializedName("password") val password: String? = null,
        @SerializedName("redirectRefId") val redirectRefId: String? = null
)
