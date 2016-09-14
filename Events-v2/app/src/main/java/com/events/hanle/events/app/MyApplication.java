package com.events.hanle.events.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.crashlytics.android.Crashlytics;
import com.events.hanle.events.Constants.MyPreferenceManager;
import com.events.hanle.events.UserChangeOfDescision;
import com.events.hanle.events.UserLoginActivity;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;


public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static MyApplication mInstance;

    private MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        OneSignal.startInit(this)
                .setAutoPromptLocation(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();

        OneSignal.enableInAppAlertNotification(true);
        OneSignal.enableNotificationsWhenActive(true);
        //OneSignal.sendTag("test12", "test12");
        Fabric.with(this, new Crashlytics());
        JSONObject tags = new JSONObject();
        try {
            tags.put("event22", "event_value22");


            OneSignal.sendTags(tags);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mInstance = this;


    }


    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler{

        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            String additionalMessage = "";

            try {
                if (additionalData != null) {
                    if (additionalData.has("actionSelected"))
                        additionalMessage += "Pressed ButtonID: " + additionalData.getString("actionSelected");
                    Toast.makeText(MyApplication.this, message, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(MyApplication.this,UserChangeOfDescision.class);
                    startActivity(i);
                    additionalMessage = message + "\nFull additionalData:\n" + additionalData.toString();
                }

                Log.d("OneSignalExample", "message:\n" + message + "\nadditionalMessage:\n" + additionalMessage);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        }





    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }
        return pref;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void logout() {
        pref.clear();
        Intent intent = new Intent(this, UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
