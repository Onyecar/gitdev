package com.onyx.gitdev;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Morph-Deji on 15-Mar-17.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
