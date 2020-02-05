package com.busfor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.modules.core.DeviceEventManagerModule

internal class SmsBroadcastReceiver(private val mContext: ReactApplicationContext?) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            if (extras == null) {
                emitJSEvent(ErrorType.EXTRAS_NULL.type, ErrorType.EXTRAS_NULL.message)
                return
            }

            val status = extras.get(SmsRetriever.EXTRA_STATUS) as Status?
            if (status == null) {
                emitJSEvent(ErrorType.STATUS_NULL.type, ErrorType.STATUS_NULL.message)
                return
            }

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    emitJSEvent(MESSAGE_KEY, message)
                }

                CommonStatusCodes.TIMEOUT -> {
                    emitJSEvent(ErrorType.TIMEOUT.type, ErrorType.TIMEOUT.message)
                }
            }
        }
    }

    private fun emitJSEvent(key: String, message: String) {
        if (mContext == null || !mContext.hasActiveCatalystInstance()) return

        val map = WritableNativeMap()
        map.putString(key, message)
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(SMS_EVENT, map)
    }
}
