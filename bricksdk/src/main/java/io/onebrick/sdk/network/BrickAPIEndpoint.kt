package io.onebrick.sdk.network

import io.onebrick.sdk.model.*
import io.onebrick.sdk.util.ACCOUNT_LIST
import io.onebrick.sdk.util.AUTH_USER
import io.onebrick.sdk.util.INSTITUTION_LIST
import io.onebrick.sdk.util.REQUEST_ACCESS_TOKEN
import retrofit2.Call
import retrofit2.http.*

interface BrickAPIEndpoint {
        @GET(REQUEST_ACCESS_TOKEN)
        fun getAccessToken(@Header("Authorization") authorization: String): Call<AccessToken>

        @Headers("Content-Type: application/json")
        @POST(REQUEST_ACCESS_TOKEN)
        fun requestTokenCredentials(@Body body: AccessTokenRequestPayload): Call<AccessTokenRequest>

        @Headers("Content-Type: application/json")
        @GET("$INSTITUTION_LIST/{id}")
        fun requestInstitutionList(@Header("Authorization") authorization: String, @Path("id") id: String): Call<Institution>

        @Headers("Content-Type: application/json")
        @POST("$AUTH_USER/{id}?uuid=1")
        fun authenticateUser(@Header("Authorization") authorization: String, @Path("id") id: String, @Body body: UserAuthRequestPayload): Call<AuthenticateUserResponse>

        @Headers("Content-Type: application/json")
        @POST("$AUTH_USER/{id}?uuid=1")
        fun authenticateEwalletUser(@Header("Authorization") authorization: String, @Path("id") id: String, @Body body: MFABankingPayload): Call<AuthenticateUserResponse>


        @Headers("Content-Type: application/json")
        @POST("$INSTITUTION_LIST/{id}?uuid=1")
        fun submitInstitution(@Header("Authorization") authorization: String, @Path("id") id: String, @Body body: SubmitInstitutionPayload): Call<InstitutionResponseSubmit>

        @Headers("Content-Type: application/json")
        @POST("$AUTH_USER/{accountName}/{id}?uuid=1")

        fun requestCommonMFAAuthUser(@Header("Authorization") authorization: String,@Path("accountName") accountName: String, @Path("id") id: String, @Query("reffId") reffId: String, @Body body: MFABankingPayload): Call<AuthenticateUserResponse>

        @Headers("Content-Type: application/json")
        @POST("$AUTH_USER/{accountName}/retry/{id}?uuid=1")

        fun requestResendOTP(@Header("Authorization") authorization: String,@Path("accountName") accountName: String, @Path("id") id: String): Call<AuthenticateUserResponse>


        @Headers("Content-Type: application/json")
        @GET(ACCOUNT_LIST)
        fun requestAccountList(@Header("Authorization") authorization: String): Call<AccountListStatus>
}