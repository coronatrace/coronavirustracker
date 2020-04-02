package com.coronavirustracker.auth;

import android.content.Context;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class AnonymousAuth {
    private static String trackingId;
    private Context activityContext;

    public AnonymousAuth(Context context) {
        activityContext = context;
    }

    public String initialise() {
        /**
         * Authorize with guest access
         * The trackingId is set as the identity pool ID
         */

        final FutureTask<Object> ft = new FutureTask<Object>(() -> {}, new Object());

        // Initialise & get guest credentials
        AWSMobileClient.getInstance().initialize(activityContext.getApplicationContext(), new Callback<UserStateDetails>() {
                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        AWSMobileClient.getInstance().getCredentials();
                    }

                    @Override
                    public void onError(Exception e) {
                        //Log.e("Amplify Auth", "Initialization error.", e);
                    }
                }
        );

        // Re-initialise to get the user identity pool ID
        // This is necessary as per https://docs.amplify.aws/lib/auth/guest-access?platform=android
        AWSMobileClient.getInstance().initialize(activityContext.getApplicationContext(), new Callback<UserStateDetails>() {
                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        trackingId = AWSMobileClient.getInstance().getIdentityId();
                        ft.run();
                    }

                    @Override
                    public void onError(Exception e) {
                        //Log.e("Amplify Auth", "Initialization error.", e);
                    }
                }
        );

        try { try {
            ft.get();
        } catch (InterruptedException e) {}
        } catch (ExecutionException f) {}

        return trackingId;
    }
}
