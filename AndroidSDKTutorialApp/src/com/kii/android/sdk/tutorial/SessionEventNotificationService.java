package com.kii.android.sdk.tutorial;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.kii.cloud.analytics.KiiEvent;
import com.kii.cloud.analytics.impl._KiiSession;
import com.kii.cloud.analytics.impl._KiiSessionCallback;

public class SessionEventNotificationService extends Service {
    private static final int NOTIFICATION_ID = 12345;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("KiiSessionnotify", "KiiSession notify service started.");
        _KiiSession.setSessionCallback(new _KiiSessionCallback() {

            @Override
            public void onSessionEventSent(KiiEvent event) {
                showNotification(event);
            }

        });
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    public void showNotification(KiiEvent event) {
        String title = "Session event sent";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.launcher).setContentTitle(title)
                .setContentText("Tap to view event json");
        Notification notification = builder.build();
        Intent targetIntent = new Intent(this.getApplicationContext(),
                SessionEventDisplayActivity.class);
        targetIntent.putExtra("eventJson", event.toJson().toString());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.setLatestEventInfo(this,
                this.getString(R.string.app_name), title, contentIntent);
        nManager.cancelAll();
        nManager.notify(NOTIFICATION_ID, notification);
    }

}
