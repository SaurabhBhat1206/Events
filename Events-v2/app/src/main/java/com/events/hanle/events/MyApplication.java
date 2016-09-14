package com.events.hanle.events;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.onesignal.OneSignal;

/**
 * Created by Hanle on 5/4/2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);

    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}