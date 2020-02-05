package com.busfor

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.common.api.GoogleApiClient
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.BaseActivityEventListener

internal class PhoneRetrieverService(private val mActivity: Activity?) {
    private var mPromise: Promise? = null
    private var mListener: Listener? = null

    fun getActivityEventListener(): ActivityEventListener {
        return mActivityEventListener
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun requestPhoneNumber(context: Context, promise: Promise?) {
        if (promise == null) {
            reject("", "")
            return
        }
        if (mActivity == null) {
            reject(ErrorType.NULL_ACTIVITY.type, ErrorType.NULL_ACTIVITY.message)
            return
        }

        mPromise = promise

        if (!GooglePlayServicesHelper.isAvailable(context)) {
            reject(ErrorType.SERVICES_UNAVAILABLE.type, ErrorType.SERVICES_UNAVAILABLE.message)
            return
        }
        if (GooglePlayServicesHelper.hasSupportedVersion(context)) {
            reject(ErrorType.UNSUPPORTED_VERSION.type, ErrorType.UNSUPPORTED_VERSION.message)
            return
        }


        val request: HintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()
        val googleApiClient: GoogleApiClient = GooglePlayServicesHelper
                .getGoogleApiClient(context, mActivity, mApiClientConnectionCallbacks, mApiClientOnConnectionFailedListener)
        val intent: PendingIntent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, request)

        try {
            mActivity.startIntentSenderForResult(intent.intentSender,
                    REQUEST_PHONE_NUMBER_REQUEST_CODE, null, 0, 0, 0)
        } catch (e: IntentSender.SendIntentException) {
            reject(ErrorType.SEND_INTENT_ERROR.type, ErrorType.SEND_INTENT_ERROR.message)
        }
    }

    private fun resolve(value: Any) {
        mPromise?.resolve(value)
        mListener?.phoneNumberResultReceived()
        GooglePlayServicesHelper.disconnectApiClient(mActivity)

        mPromise = null
        mListener = null
    }

    private fun reject(type: String, message: String) {
        mPromise?.reject(type, message)
        mListener?.phoneNumberResultReceived()
        GooglePlayServicesHelper.disconnectApiClient(mActivity)

        mPromise = null
        mListener = null
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
