package com.example.eightballbooking;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION_TITLE = "notification-title";
    public static final String NOTIFICATION_BODY = "notification-body";

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String body = intent.getStringExtra(NOTIFICATION_BODY);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Ellenőrizd, hogy a notificationManager nem null
        if (notificationManager != null && notificationManager.areNotificationsEnabled()) {
            NotificationHelper notificationHelper = new NotificationHelper(context);
            notificationHelper.sendHighPriorityNotification(title, body, MainActivity.class, notificationId);
        } else {
            System.out.println("NotificationReceiver: " + "Notification Manager is not available or notifications are disabled.");
        }
    }
}

