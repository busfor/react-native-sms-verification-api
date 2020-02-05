package com.busfor.smsverification

import android.content.BroadcastReceiver
import android.content.IntentFilter
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext

internal class SmsRetrieverService(private val mContext: ReactApplicationContext) {
    private var mReceiver: BroadcastReceiver? = null
    private var mPromise: Promise? = null

    fun startRetriever(promise: Promise) {
        mPromise = promise

        if (!GooglePlayServicesHelper.isAvailable(mContext)) {
            reject(ErrorType.SERVICES_UNAVAILABLE.type, ErrorType.SERVICES_UNAVAILABLE.message)
            return
        }

        if (!GooglePlayServicesHelper.hasSupportedVersion(mContext)) {
            reject(ErrorType.UNSUPPORTED_VERSION.type, ErrorType.UNSUPPORTED_VERSION.message)
            return
        }

        GooglePlayServicesHelper.startSmsRetriever(mContext, mOnSuccessListener, mOnFailureListener)
    }

    private fun registerReceiver(): Boolean {
        mReceiver = SmsBroadcastReceiver(mContext)

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)

        return try {
            mContext.registerReceiver(mReceiver, intentFilter)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    private fun unregisterReceiver() {
        if (mReceiver == null) {
            return
        }

        try {
            mContext.unregisterReceiver(mReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun resolve(value: Any) {
        mPromise?.resolve(value)
        mPromise = null
    }

    private fun reject(type: String, message: String) {
        mPromise?.reject(type, message)
        mPromise = null
    }

    private val mOnSuccessListener = OnSuccessListener<Void> {
        val registered = registerReceiver()
        resolve(registered)
    }

    private val mOnFailureListener = OnFailureListener {
        unregisterReceiver()
        reject(ErrorType.TASK_FAILURE.type, ErrorType.TASK_FAILURE.message)
    }
}
