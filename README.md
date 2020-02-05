# react-native-sms-verification-api
![npm](https://img.shields.io/npm/dw/@busfor/react-native-sms-verification-api?style=for-the-badge)
[![npm (tag)](https://img.shields.io/npm/v/@busfor/react-native-sms-verification-api/latest?style=for-the-badge)](https://img.shields.io/npm/v/@busfor/react-native-sms-verification-api/latest?style=for-the-badge)
[![](https://img.shields.io/npm/types/typescript?style=for-the-badge)](https://img.shields.io/npm/types/typescript?style=for-the-badge)

Verify your users by SMS without making them deal with verification code.


---

## Versions

| 1.0.x               			 |
| :-------------------------:| 
| Android support libraries  |


## Getting started

1. Install package
`$ yarn add react-native-sms-verification-api`

2. Add Kotlin gradle plugin
```gradle
buildscript {
	ext {
		...
		kotlinVersion = '1.3.50'
	}
	...
	dependencies {
		...
		classpath org.jetbrains.kotlin:kotlin-gradle-plugin:${project.ext.kotlinVersion}
	}
}
```

## Linking 

### >= 0.60

Autolinking will just do the job.

### < 0.60

### Mostly automatic installation

`$ react-native link @busfor/react-native-sms-verification-api`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.busfor.smsverification.RNSmsVerificationApiPackage;` to the imports at the top of the file
  - Add `new RNSmsVerificationApiPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':@busfor_react-native-sms-verification-api'
		project(':@busfor_react-native-sms-verification-api').projectDir = new File(rootProject.projectDir, '../node_modules/@busfor/react-native-sms-verification-api/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-sms-verification-api')
  	```

## Usage
```javascript
import SmsVerificationApi from 'react-native-sms-verification-api';

SmsVerificationApi.requestPhoneNumber()
      .then((phone) => Alert.alert("SmsVerificationApi.requestPhoneNumber", phone))
      .catch((reason) => console.log(reason))

SmsVerificationApi.startSmsRetriever()

SmsVerificationApi.addSmsListener((event) => {
	Alert.alert("SmsVerificationApi.smsRetriever", event.message)
})

SmsVerificationApi.removeSmsListener()
```
