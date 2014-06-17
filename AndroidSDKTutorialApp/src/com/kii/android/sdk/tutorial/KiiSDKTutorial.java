package com.kii.android.sdk.tutorial;

import android.app.Application;

import com.kii.cloud.storage.Kii;

public class KiiSDKTutorial extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Kii.initialize(AppConstants.APP_ID, AppConstants.APP_KEY,
                AppConstants.APP_SITE);
    }
}
