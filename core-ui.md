# Getting started with the UI Android SDK

Set up the Brick Android SDK so that you can begin using Brick SDK


Getting started with the Brick Android SDK requires five steps:

* Install the SDK in your app
* Configure your app
* Set up the connection token endpoint in your app and backend
* Initialize the SDK in your app
* Connect your app to the simulated reader


## 1. Install the SDK in your app
To install the SDK, add Brick to the dependencies block of your app/build.gradle file:

```
apply plugin: 'com.android.application'

android { ... }

dependencies {
  implementation "com.io.io.sdk:1.0.0"
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
    
   CoreBrickUISDK.initializedUISDK(
                    applicationContext,
                    clientId,
                    clientSecret,
                    Environment.SANDBOX)
```







