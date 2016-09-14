package com.events.hanle.events;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationPayload;

import java.math.BigInteger;

/**
 * Created by Hanle on 7/21/2016.
 */
public class NotificationRecievedMessages extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationPayload notification) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                return builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            }
        };

        OSNotificationDisplayedResult result = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + result.notificationId);
        Toast.makeText(NotificationRecievedMessages.this, ""+ result.notificationId, Toast.LENGTH_SHORT).show();
        Toast.makeText(NotificationRecievedMessages.this, ""+ notification, Toast.LENGTH_SHORT).show();




        return true;
    }
}