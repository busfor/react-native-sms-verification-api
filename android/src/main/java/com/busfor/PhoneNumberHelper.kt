package com.busfor

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.BaseActivityEventListener
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.common.api.GoogleApiClient

class PhoneNumberHelper {
    private var mPromise: Promise? = null
    private var mListener: Listener? = null
    private var mServicesHelper: GooglePlayServicesHelper? = null

    fun getActivityEventListener(): ActivityEventListener {
        return mActivityEventListener
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun requestPhoneNumber(context: Context, activity: Activity?, promise: Promise?) {
        if (promise == null) {
            reject("", "")
            return
        }
        if (activity == null) {
            reject(ErrorType.NULL_ACTIVITY.type, ErrorType.NULL_ACTIVITY.message)
            return
        }

        mServicesHelper = GooglePlayServicesHelper(context, activity,
                mApiClientConnectionCallbacks, mApiClientOnConnectionFailedListener)
        mPromise = promise

        if (!mServicesHelper?.isAvailable()!!) {
            reject(ErrorType.SERVICES_UNAVAILABLE.type, ErrorType.SERVICES_UNAVAILABLE.message)
            return
        }
        if (!mServicesHelper?.hasSupportedVersion()!!) {
            reject(ErrorType.UNSUPPORTED_VERSION.type, ErrorType.UNSUPPORTED_VERSION.message)
            return
        }


        val request: HintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()
        val googleApiClient: GoogleApiClient = mServicesHelper?.getGoogleApiClient()!!
        val intent: PendingIntent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, request)

        try {
            activity.startIntentSenderForResult(intent.intentSender,
                    REQUEST_PHONE_NUMBER_REQUEST_CODE, null, 0, 0, 0)
        } catch (e: IntentSender.SendIntentException) {
            reject(ErrorType.SEND_INTENT_ERROR.type, ErrorType.SEND_INTENT_ERROR.message)
        }
    }

    private fun resolve(value: Any) {
        mPromise?.resolve(value)
        mListener?.phoneNumberResultReceived()
        mServicesHelper?.disconnectApiClient()

        mPromise = null
        mListener = null
        mServicesHelper = null
    }

    private fun reject(type: String, message: String) {
        mPromise?.reject(type, message)
        mListener?.phoneNumberResultReceived()
        mServicesHelper?.disconnectApiClient()

        mPromise = null
        mListener = null
        mServicesHelper = null
    }

    private val mActivityEventListener = object : BaseActivityEventListener() {
        override fun onActivityResult(activity: Activity, requestCode: Int,
                                      resultCode: Int, data: Intent) {
            if (requestCode == REQUEST_PHONE_NUMBER_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    val credential: Credential = data.getParcelableExtra(Credential.EXTRA_KEY)
                    val phoneNumber: String = credential.id
                    resolve(phoneNumber)
                    return
                }
            }
            reject(ErrorType.ACTIVITY_RESULT_NOOK.type, ErrorType.ACTIVITY_RESULT_NOOK.message)
        }
    }

    private val mApiClientConnectionCallbacks = object : GoogleApiClient.ConnectionCallbacks {
        override fun onConnected(bundle: Bundle?) {}

        override fun onConnectionSuspended(i: Int) {
            reject(ErrorType.CONNECTION_SUSPENDED.type, ErrorType.CONNECTION_SUSPENDED.message)
        }
    }

    private val mApiClientOnConnectionFailedListener = GoogleApiClient.OnConnectionFailedListener {
        reject(ErrorType.CONNECTION_FAILED.type, ErrorType.CONNECTION_FAILED.message)
    }

    interface Listener {
        fun phoneNumberResultReceived()
    }
}
