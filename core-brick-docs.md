# Getting started with the Core Android SDK

Set up the Brick Android SDK so that you can begin using Brick SDK

## Requirements

* Android 5.0 (API level 21) and above
* Android Gradle Plugin 3.5.1
* Gradle 5.4.1+
* AndroidX (as of v11.0.0)


**Getting started with the Brick Android SDK requires 2 steps:**

* Install the SDK in your app
* Configure your app

### Another Explanation

* Method Avail
* Method Explanation



## 1. Install the SDK in your app
To install the SDK, add Brick to the dependencies block of your app/build.gradle file:

```
apply plugin: 'com.android.application'

android { ... }

dependencies {
  implementation "com.io.io.sdk:latest_version"
  // ...
}
```
## 2. Configure your app
You must enable internet, network state and phone state in order to use the Android SDK. Before initializing the SDK, add the following check to make sure that the `INTERNET`,`ACCESS_NETWORK_STATE`,`READ_PHONE_STATE` permission is enabled in your app:

Import Brick SDK

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment
	
```
Then initialized SDK with trigger eg button etc

```
   private val clientId = "xxx"
   private val clientSecret = "xxx"
   private val name = "xxx"
   private val url = "xxx"
    
   CoreBrickSDK.initializedSDK(
                           clientId: String,
                           secret: String,
                           name:String,
                           url:String,
                           environment: Environment)
```

*Notes* 

```
You can choose environtment SANBOX or PRODUCTION
```
                       
                           
## 3 Method Available

* **Request Credentils From Brick Server**

```
CoreBrickSDK.requestTokenCredentials(result:IRequestTokenCredentials)
```
* R**equest Access Token From User**

```
CoreBrickSDK.requestAccessToken(result: IAccessTokenRequestResult)
```
* **List Institution**

```
CoreBrickSDK.listInstitution(result: IRequestInstituion)
```
* **Authenticate User BANK/Income Verification Insititution**

```
CoreBrickSDK.authenticateUser(username:String, password: String, institutionId: String, result:IRequestResponseUserAuth)
```
* **Authenticate User Ewallet**

```
CoreBrickSDK.authenticateEwalletUser(payload:MFABankingPayload, result:IRequestTransactionResult) 
```
* **MFA/OTP Authentication For User**

```
CoreBrickSDK.submitCredentialsForMFAAccount(payload:MFABankingPayload, result:IRequestTransactionResult)
```


## 4 Integration Guide

**1. Setup Brick**

Configure the SDK with your Brick `key`, `secret`, so that it can make requests to the Brick API, such as in your Application subclass:



```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
}
```

**2. Request Access Token**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
          CoreBrickSDK.requestAccessToken(object :IAccessTokenRequestResult {
              override fun success(accessToken: AccessToken?) {
                 ...
              }

              override fun error(t: Throwable?) {
                ...
              }

          })
        }
}
```

**3. Request Access Token**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
			CoreBrickSDK.requestTokenCredentials(object:IRequestTokenCredentials{
               override fun success(response: AccessTokenRequest?) {
                  ...
               }

               override fun error(t: Throwable?) {
                  ...
               }

           })
        }
}
```

**4. List Insitution**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
		CoreBrickSDK.listInstitution(object :IRequestInstituion{
               override fun success(response: Institution?) {
                   // will return list of institution
               }

               override fun error(t: Throwable?) {
        
               }

           })
           }
}
```

**5. List Insitution**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
		CoreBrickSDK.listInstitution(object :IRequestInstituion{
               override fun success(response: Institution?) {
                   // will return list of institution
               }
               override fun error(t: Throwable?) {
        
               }
           })
           }
}
```

**5. Authenticate User With Bank**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
		  CoreBrickSDK.authenticateUser("someUser","somePassword","1",object:IRequestResponseUserAuth{
            override fun success(response: AuthenticateUserResponse) {
              //// you need to handle response.status here
              //// if status response 200 then you can use
              /// if status response 428 then go to next step
             }
             override fun error(t: Throwable?) { 
                   ...
             }

           })
    }
}
```


**5.1 Authenticate User With Bank OTP Request**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
        
       val payload = MFABankingPayload(
                token = "Some pin",
                duration = " you will get the response from the previous step",
                institutionId = "you will get the response from the previous step",
                redirectRefId = "you will get the response from the previous step",
                sessionId = "you will get the response from the previous step",
                username = "you will get the response from the previous step",
                password = "you will get the response from the previous step"
        )
        
		  CoreBrickSDK.authenticateUser("someUser","somePassword","1",object:IRequestResponseUserAuth{
            override fun success(credentials: AuthenticateUserResponse?) {
                ....
            }

            override fun error(t: Throwable?) {
               ....
               
           })
    }
}
```

**6. Authenticate User With Ewallet**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
        
        val payload = MFABankingPayload(
            username = "you will get the response from the previous step",
            institutionId = "you will get the response from the previous step",
            redirectRefId = "you will get the response from the previous step"

        )
 		CoreBrickSDK.authenticateEwalletUser(payload,object:IRequestTransactionResult{
            override fun success(credentials: AuthenticateUserResponse?) {
            
            }
            override fun error(t: Throwable?) {
                dismissLoadingActivity()
                showErrorMessage(true,getString(R.string.phoneNotRegistered))
            }
        })
    }
}
```

**7. Authenticate User With Ewallet**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
        
        val payload = MFABankingPayload(
            username = "you will get the response from the previous step",
            institutionId = "you will get the response from the previous step",
            redirectRefId = "you will get the response from the previous step"

        )
 		CoreBrickSDK.authenticateEwalletUser(payload,object:IRequestTransactionResult{
            override fun success(credentials: AuthenticateUserResponse?) {
            	...
            }
            override fun error(t: Throwable?) {
            	...
            }
        })
    }
}
```

**7.1 Special Case GOJEK**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
        
        val payload = MFABankingPayload(
            institutionId = "you will get the response from the previous step",
            otp = "SMS that you get from the phone",
            otpToken = "you will get the response from the previous step",,
            redirectRefId = "you will get the response from the previous step",
            sessionId = "you will get the response from the previous step",,
            uniqueId = "you will get the response from the previous step",,
            username = "you will get the response from the previous step",
        )
        
 		CoreBrickSDK.authenticateEwalletUser(payload,object:IRequestTransactionResult{
            override fun success(credentials: AuthenticateUserResponse?) {
            	...
            }
            override fun error(t: Throwable?) {
            	...
            }
        })
    }
}
```

**7.1 Special Case OVO**

```
import com.io.io.sdk.*
import com.io.io.sdk.util.Environment

class MyApp : Application() {

    private val clientId = "Some-client-key"
    private val clientSecret = "some-client-secret"
    private val name = "BRICK"
    private val url = "https://onebrick.io"
    
    override fun onCreate() {
        super.onCreate()
          CoreBrickSDK.initializedSDK(clientId,clientSecret,name,url,Environment.YOUR_ENV)
    }
    
    val buttonRequest = findViewById<Button>(R.id.button_institution)
        buttonRequest.setOnClickListener {
        
        val payload = MFABankingPayload(
            pin = "USER INPUT",
            otpNumber = "SMS that you get from the phone",
            deviceId = "you will get the response from the previous step",
            refId = "you will get the response from the previous step",

            institutionId ="you will get the response from the previous step",
            redirectRefId = "you will get the response from the previous step",
            username = "you will get the response from the previous step",
        )
        
 		CoreBrickSDK.authenticateEwalletUser(payload,object:IRequestTransactionResult{
            override fun success(credentials: AuthenticateUserResponse?) {
            	...
            }
            override fun error(t: Throwable?) {
            	...
            }
        })
    }
}
```









