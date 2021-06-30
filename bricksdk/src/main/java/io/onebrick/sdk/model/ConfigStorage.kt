package io.onebrick.sdk.model

import io.onebrick.sdk.util.AMP_PROD
import io.onebrick.sdk.util.AMP_SANDBOX
import io.onebrick.sdk.util.URL_PRODUCTION_V2
import io.onebrick.sdk.util.URL_STAGING_v2
import io.onebrick.sdk.util.Environment

class ConfigStorage {

    companion object {
        var clientId:String = ""
        var secret:String = ""
        var urlV2API:String = ""
        var ampAPIKEY:String = ""
        var currentImages = ""
        var appName = ""
        private var isProduction:Boolean = false
        var name:String = ""
        var url:String = ""
        var currentPhoneNumber = ""
        var currentUCU = ""
        var currentUCP = ""
        var redirectReffId = ""
        lateinit var institutionData: InstitutionData
        lateinit var accessTokenRequest: AccessTokenRequest
        var barrierToken:String = ""
        lateinit var authenticateUserResponseData: AuthenticateUserResponseData
        var userSessionToken:String = ""
        lateinit var institutionList: Institution
        lateinit var responseOTPRequest: AuthenticateUserResponseData
        var userId:String = ""

        fun setCurrentInstitution(currentInstitutionData: InstitutionData) {
            institutionData = currentInstitutionData
        }

        fun setConfiguration(clientId:String, secret:String, name:String,url:String, environment: Environment) {
            Companion.clientId = clientId
            Companion.secret = secret
            Companion.name = name
            Companion.url = url
            when(environment){
                Environment.PRODUCTION -> {
                    ampAPIKEY = AMP_PROD
                    isProduction = true
                    urlV2API = URL_PRODUCTION_V2
                }
                Environment.SANDBOX -> {
                    ampAPIKEY = AMP_SANDBOX
                    isProduction = false
                    urlV2API = URL_STAGING_v2
                }
            }
        }
    }
}