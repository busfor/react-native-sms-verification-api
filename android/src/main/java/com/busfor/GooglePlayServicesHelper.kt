package com.busfor

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.FragmentActivity
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.auth.api.Auth

class GooglePlayServicesHelper(context: Context, activity: Activity,
                               connectionCallbacks: GoogleApiClient.ConnectionCallbacks,
                               connectionListener: GoogleApiClient.OnConnectionFailedListener) {
    private val mActivity = activity
    private val mContext = context
    private val mConnectionCallbacks =  connectionCallbacks
    private val mConnectionListener = connectionListener
    private var mGoogleApiClient: GoogleApiClient? = null

    fun isAvailable(): Boolean {
        val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext)

        return (result != ConnectionResult.SERVICE_MISSING
                && result != ConnectionResult.SERVICE_DISABLED
                && result != ConnectionResult.SERVICE_INVALID)
    }

    fun hasSupportedVersion(): Boolean {
        val manager = mContext.packageManager

        return try {
            val version = manager
                    .getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0)
                    .versionCode
            version >= MINIMAL_VERSION
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getGoogleApiClient(): GoogleApiClient {
        if (mGoogleApiClient == null) {
            var builder: GoogleApiClient.Builder = GoogleApiClient.Builder(mContext)
            builder.addConnectionCallbacks(mConnectionCallbacks)
            builder.addApi(Auth.CREDENTIALS_API)

            if (mActivity is FragmentActivity) {
                builder = builder.enableAutoManage(mActivity, mConnectionListener)
            }
            mGoogleApiClient = builder.build()
        }

        return mGoogleApiClient as GoogleApiClient
    }

    fun disconnectApiClient() {
        if (mGoogleApiClient != null) {
            if (mActivity is FragmentActivity) {
                mGoogleApiClient?.stopAutoManage(mActivity)
            }
            mGoogleApiClient?.disconnect()
            mGoogleApiClient = null
        }
    }
}
