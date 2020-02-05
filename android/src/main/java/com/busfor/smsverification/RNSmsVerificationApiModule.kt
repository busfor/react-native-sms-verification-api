package com.busfor.smsverification

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod

class RNSmsVerificationApiModule(reactContext: ReactApplicationContext)
    : ReactContextBaseJavaModule(reactContext) {
    private val mPhoneRetrieverService: PhoneRetrieverService = PhoneRetrieverService(currentActivity)
    private val mSmsRetrieverService: SmsRetrieverService = SmsRetrieverService(reactContext)

    override fun getName(): String {
        return "RNSmsVerificationApi"
    }

    override fun getConstants(): HashMap<String, Any>? {
        val resultMap = HashMap<String, Any>()
        resultMap["SMS_EVENT"] = SMS_EVENT

        return resultMap
    }

    @Suppress("unused")
    @ReactMethod
    fun requestPhoneNumber(promise: Promise) {
        val context: ReactApplicationContext = reactApplicationContext
        val eventListener = mPhoneRetrieverService.getActivityEventListener()

        context.addActivityEventListener(eventListener)

        mPhoneRetrieverService.setListener(object : PhoneRetrieverService.Listener {
            override fun phoneNumberResultReceived() {
                context.removeActivityEventListener(eventListener)
            }
        })

        mPhoneRetrieverService.requestPhoneNumber(context, promise)
    }

    @Suppress("unused")
    @ReactMethod
    fun startSmsRetriever(promise: Promise) {
        mSmsRetrieverService.startRetriever(promise)
    }
}
