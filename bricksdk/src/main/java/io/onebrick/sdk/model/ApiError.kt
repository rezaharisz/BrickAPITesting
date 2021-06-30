package io.onebrick.sdk.model

import com.google.gson.annotations.SerializedName


class ApiError {
    @SerializedName("status")
    val status: String? = null

    @SerializedName("message")
    val message: String? = null

    override fun toString(): String {
        return "ApiError{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}'
    }
}

