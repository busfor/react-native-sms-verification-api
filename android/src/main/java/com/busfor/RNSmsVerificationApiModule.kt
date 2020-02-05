package com.busfor

import android.app.Activity
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod

class RNSmsVerificationApiModule(reactContext: ReactApplicationContext)
    : ReactContextBaseJavaModule(reactContext) {
    private val mPhoneNumberHelper: PhoneNumberHelper = PhoneNumberHelper()

    override fun getName(): String {
        return "RNSmsVerificationApi"
    }

    @ReactMethod
    fun requestPhoneNumber(promise: Promise) {
        val context: ReactApplicationContext = reactApplicationContext
        val activity: Activity? = currentActivity
        val eventListener = mPhoneNumberHelper.getActivityEventListener()

        context.addActivityEventListener(eventListener)

        mPhoneNumberHelper.setListener(object : PhoneNumberHelper.Listener {
            override fun phoneNumberResultReceived() {
                context.removeActivityEventListener(eventListener)
            }
        })

        mPhoneNumberHelper.requestPhoneNumber(context, activity, promise)
    }
}
