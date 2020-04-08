package com.coronatrace.covidtracker.auth

import android.content.Context
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import java.util.concurrent.ExecutionException
import java.util.concurrent.FutureTask

/**
 * Authenticate
 *
 * Authorize with guest access, and set the trackingId as the identity pool ID
 */
class Auth(val context: Context) {


    var userIdentityId: String? = null
        private set

    init {
        val ft =
            FutureTask(
                Runnable {}, Any()
            )

        // Initialise & get guest credentials
        AWSMobileClient.getInstance().initialize(
            context.applicationContext,
            object : Callback<UserStateDetails?> {
                override fun onResult(userStateDetails: UserStateDetails?) {
                    AWSMobileClient.getInstance().credentials
                }

                override fun onError(e: Exception) {
                    // TODO
                }
            }
        )

        // Re-initialise to get the user identity pool ID
        // This is necessary as per https://docs.amplify.aws/lib/auth/guest-access?platform=android
        AWSMobileClient.getInstance().initialize(
            context.applicationContext,
            object : Callback<UserStateDetails?> {
                override fun onResult(userStateDetails: UserStateDetails?) {
                    userIdentityId = AWSMobileClient.getInstance().identityId
                    ft.run()
                }

                override fun onError(e: Exception) {
                    // TODO
                }
            }
        )
        try {
            try {
                ft.get()
            } catch (e: InterruptedException) {
            }
        } catch (f: ExecutionException) {
        }
    }


}