package io.onebrick.sdk

import android.util.Log
import io.onebrick.sdk.model.*
import io.onebrick.sdk.network.BrickAPIEndpoint
import io.onebrick.sdk.network.ServiceBuilder
import io.onebrick.sdk.util.JENIUS
import io.onebrick.sdk.util.Environment
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

interface IAccessTokenRequestResult {
    fun success(accessToken: AccessToken?)
    fun error(t: Throwable?)
}

interface IRequestTokenCredentials {
    fun success(response: AccessTokenRequest?)
    fun error(t: Throwable?)
}

interface IRequestInstituion {
    fun success(response: Institution?)
    fun error(t: Throwable?)
}

interface IRequestResponseUserAuth {
    fun success(response: AuthenticateUserResponse)
    fun error(t: Throwable?)
}
interface IRequestTransactionResult {
    fun success(response: AuthenticateUserResponse?)
    fun error(t: Throwable?)
}

interface IRequestSubmitInstitution {
    fun success(response: InstitutionResponseSubmit?)
    fun error(t: Throwable?)
}

class CoreBrickSDK {

    companion object {

        fun initializedSDK(
                           clientId: String,
                           secret: String,
                           name:String,
                           url:String,
                           environment: Environment) {
            ConfigStorage.setConfiguration(
                    clientId,
                    secret,
                    name,
                    url,
                    environment)
        }

        fun listInstitution(result: IRequestInstituion) {
            val bearer = "Bearer ${ConfigStorage.barrierToken}"
            val request = ServiceBuilder.buildService(BrickAPIEndpoint::class.java)

            request.requestInstitutionList(bearer,
                ConfigStorage.userId).enqueue(object: Callback<Institution>{
                override fun onFailure(call: Call<Institution>, t: Throwable) {
                    return result.error(t)
                }

                override fun onResponse(call: Call<Institution>, response: Response<Institution>) {
                    if (response.isSuccessful) {

                        val institution: Institution? = response.body()
                        if (institution != null) {
                            ConfigStorage.institutionList = institution
                        }
                        result.success(institution)
                    }

                }

            })
        }

        fun submitInstitution(
            name:String,
            institutionType: String,
            result: IRequestSubmitInstitution
        ) {
            val bearer = "Bearer ${ConfigStorage.barrierToken}"
            val request = ServiceBuilder.buildService(BrickAPIEndpoint::class.java)

            val tokenPayload = SubmitInstitutionPayload(
                institutionName = name,
                institutionType = institutionType

            )

            request.submitInstitution(bearer,
                ConfigStorage.userId,tokenPayload).enqueue(object: Callback<InstitutionResponseSubmit>{
                override fun onFailure(call: Call<InstitutionResponseSubmit>, t: Throwable) {
                    result.error(Throwable())
                }

                override fun onResponse(call: Call<InstitutionResponseSubmit>, response: Response<InstitutionResponseSubmit>) {
                    if (response.isSuccessful) {
                        val authUserData: InstitutionResponseSubmit = response.body() as InstitutionResponseSubmit
                        return result.success(authUserData)
                    } else {
                        result.error(Throwable())
                    }
                }


            })
        }
        fun authenticateUser(username:String, password: String, institutionId: String, result: IRequestResponseUserAuth) {
            val bearer = "Bearer ${ConfigStorage.barrierToken}"
            val request = ServiceBuilder.buildService(BrickAPIEndpoint::class.java)
            val tokenPayload = UserAuthRequestPayload(
                    username = username,
                    institutionId = institutionId,
                    password = password,
                    redirectRefId = ConfigStorage.redirectReffId
            )
            request.authenticateUser(bearer,
                ConfigStorage.userId,tokenPayload).enqueue(object:Callback<AuthenticateUserResponse>{


                override fun onFailure(call: Call<AuthenticateUserResponse>, t: Throwable) {
                    result.error(Throwable())
                }

                override fun onResponse(call: Call<AuthenticateUserResponse>, response: Response<AuthenticateUserResponse>) {
                    if(response.isSuccessful) {
                        val authUserData: AuthenticateUserResponse = response.body() as AuthenticateUserResponse
                        if(authUserData.data == null) {
                            return result.error(Throwable())
                        }
                        ConfigStorage.authenticateUserResponseData = authUserData.data
                        ConfigStorage.userSessionToken = authUserData.data.sessionId.toString()
                        ConfigStorage.responseOTPRequest = authUserData.data
                        return result.success(authUserData)
                    } else {
                        result.error(Throwable())
                    }
                }
                

            })
        }

        fun listAccountUser() {
            val request = ServiceBuilder.buildService(BrickAPIEndpoint::class.java)
            val bearer = "Bearer ${ConfigStorage.userSessionToken}"

            request.requestAccountList(bearer).enqueue(object: Callback<AccountListStatus>{
                override fun onFailure(call: Call<AccountListStatus>, t: Throwable) {
                    Log.v("BRICK", t.toString())
                }

                override fun onResponse(call: Call<AccountListStatus>, response: Response<AccountListStatus>) {
                    Log.v("BRICK", response.toString())
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        Log.v("BRICK", response.code().toString())
                        Log.v("BRICK", call.request().toString())
                    }
                }

            })
        }

        fun submitCredentialsForMFAAccount(payload: MFABankingPayload, result: IRequestTransactionResult) {
            val bearer = "Bearer ${ConfigStorage.barrierToken}"
            val request = ServiceBuilder.buildService(BrickAPIEndpoint::class.java)
            var institution: String

            (if(ConfigStorage.institutionData.bankName == JENIUS) {
                "jenius"
            } else {
                ConfigStorage.institutionData.bankName.lowercase(Locale.getDefault())
            }).also { institution = it }

            request.requestCommonMFAAuthUser(bearer,institution,
                ConfigStorage.userId,
                ConfigStorage.redirectReffId, payload).enqueue(object:Callback<AuthenticateUserResponse>{

                override fun onFailure(call: Call<AuthenticateUserResponse>, t: Throwable) {
                    Log.v("BRICK", t.toString())
                    result.error(Throwable())
                }

                override fun onResponse(call: Call<AuthenticateUserResponse>, response: Response<AuthenticateUserResponse>) {

                    if (response.isSuccessful) {
                        val authUserData: AuthenticateUserResponse = response.body() as AuthenticateUserResponse
                        ConfigStorage.authenticateUserResponseData = authUserData.data!!
                        ConfigStorage.userSessionToken = authUserData.data.sessionId.toString()
                        ConfigStorage.responseOTPRequest = authUserData.data
                        return result.success(authUserData)
                    } else {
                        result.error(Throwable())
                    }
                }


            })

        }

        fun requestResendOTP(result: IRequestTransactionResult) {
            val bearer = "Bearer ${ConfigStorage.barrierToken}"
            val request = ServiceBuilder.buildService(BrickAPIEndpoint::class.java)
            var institution: String

            (if(ConfigStorage.institutionData.bankName == JENIUS) {
                "jenius"
            } else {
                ConfigStorage.institutionData.bankName.lowercase(Locale.getDefault())
            }).also { institution = it }

            request.requestResendOTP(bearer,institution,
                ConfigStorage.userId).enqueue(object:Callback<AuthenticateUserResponse>{

                override fun onFailure(call: Call<AuthenticateUserResponse>, t: Throwable) {
                    result.error(Throwable())
                }

                override fun onResponse(call: Call<AuthenticateUserResponse>, response: Response<AuthenticateUserResponse>) {

                    if (response.isSuccessful) {
                        val authUserData: AuthenticateUserResponse = response.body() as AuthenticateUserResponse
                        ConfigStorage.authenticateUserResponseData = authUserData.data!!
                        ConfigStorage.userSessionToken = authUserData.data.sessionId.toString()
                        ConfigStorage.responseOTPRequest = authUserData.data
                        return result.success(authUserData)
                    } else {
                        result.error(Throwable())
                    }
                }


            })

        }


        fun authenticateEwalletUser(payload: MFABankingPayload, result: IRequestTransactionResult) {
            val bearer = "Bearer ${ConfigStorage.barrierToken}"
            val request = ServiceBuilder.buildService(BrickAPIEndpoint::class.java)
            request.authenticateEwalletUser(bearer,
                ConfigStorage.userId,payload).enqueue(object:Callback<AuthenticateUserResponse>{


                override fun onFailure(call: Call<AuthenticateUserResponse>, t: Throwable) {
                    result.error(Throwable())
                }

                override fun onResponse(call: Call<AuthenticateUserResponse>, response: Response<AuthenticateUserResponse>) {
                    if (response.isSuccessful) {
                        val authUserData: AuthenticateUserResponse = response.body() as AuthenticateUserResponse
                        ConfigStorage.authenticateUserResponseData = authUserData.data!!
                        ConfigStorage.userSessionToken = authUserData.data.sessionId.toString()
                        ConfigStorage.responseOTPRequest = authUserData.data
                        return result.success(authUserData)
                    } else {
                        result.error(Throwable())
                    }
                }


            })

        }

        fun requestTokenCredentials(result: IRequestTokenCredentials) {
            val request = ServiceBuilder.buildService(BrickAPIEndpoint::class.java)
            val tokenPayload = AccessTokenRequestPayload(
                    accessToken = ConfigStorage.barrierToken,
                    userId = ConfigStorage.name,
                    redirectUrl = ConfigStorage.url
            )

            request.requestTokenCredentials(tokenPayload).enqueue(object: Callback<AccessTokenRequest>{
                override fun onFailure(call: Call<AccessTokenRequest>, t: Throwable) {
                    Log.v("BRICK", t.toString())
                    result.error(t)
                }

                override fun onResponse(call: Call<AccessTokenRequest>, response: Response<AccessTokenRequest>) {
                    if (response.isSuccessful) {
                        val accessTokenRequest: AccessTokenRequest? = response.body()
                        if (accessTokenRequest != null) {
                            ConfigStorage.accessTokenRequest = accessTokenRequest
                        }
                        ConfigStorage.userId = accessTokenRequest?.data?.clientId.toString()
                        ConfigStorage.redirectReffId = accessTokenRequest?.data?.redirectRefId.toString()
                        ConfigStorage.appName = accessTokenRequest?.data?.clientName.toString()
                        ConfigStorage.currentImages = accessTokenRequest?.data?.clientImageUrl.toString()
                        result.success(accessTokenRequest)
                    } else {
                        result.error(Throwable())
                    }
                }

            })

        }

        fun requestAccessToken(result: IAccessTokenRequestResult) {

            val request = ServiceBuilder.buildService(BrickAPIEndpoint::class.java)
            val secret = Credentials.basic(ConfigStorage.clientId, ConfigStorage.secret)

            request.getAccessToken(secret).enqueue(object: Callback<AccessToken>{
                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    result.error(t)
                }

                override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                    if (response.isSuccessful) {
                        response.code()
                        val accessToken: AccessToken? = response.body()
                        Log.v("BRICK", accessToken?.data?.access_token.toString())
                        ConfigStorage.barrierToken = accessToken?.data?.access_token.toString()

                        result.success(accessToken)
                    }
                }
            })
        }
    }

}
