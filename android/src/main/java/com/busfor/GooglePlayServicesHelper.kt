package com.busfor

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.FragmentActivity
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

object GooglePlayServicesHelper {
    private var mGoogleApiClient: GoogleApiClient? = null

    fun isAvailable(context: Context): Boolean {
        val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)

        return (result != ConnectionResult.SERVICE_MISSING
                && result != ConnectionResult.SERVICE_DISABLED
                && result != ConnectionResult.SERVICE_INVALID)
    }

    fun hasSupportedVersion(context: Context): Boolean {
        val manager = context.packageManager

        return try {
            val version = manager
                    .getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0)
                    .versionCode
            version >= MINIMAL_VERSION
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getGoogleApiClient(context: Context, activity: Activity,
                           connectionCallbacks: GoogleApiClient.ConnectionCallbacks,
                           connectionListener: GoogleApiClient.OnConnectionFailedListener): GoogleApiClient {
        if (mGoogleApiClient == null) {
            var builder: GoogleApiClient.Builder = GoogleApiClient.Builder(context)
            builder.addConnectionCallbacks(connectionCallbacks)
            builder.addApi(Auth.CREDENTIALS_API)

            if (activity is FragmentActivity) {
                builder = builder.enableAutoManage(activity, connectionListener)
            }
            mGoogleApiClient = builder.build()
        }

        return mGoogleApiClient as GoogleApiClient
    }

    fun disconnectApiClient(activity: Activity?) {
        if (mGoogleApiClient != null) {
            if (activity is FragmentActivity) {
                mGoogleApiClient?.stopAutoManage(activity)
            }
            mGoogleApiClient?.disconnect()
            mGoogleApiClient = null
        }
    }

    fun startSmsRetriever(context: Context, onSuccessListener: OnSuccessListener<Void>,
                          onFailureListener: OnFailureListener) {
        val client = SmsRetriever.getClient(context)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener(onSuccessListener)
        task.addOnFailureListener(onFailureListener)
    }
}
