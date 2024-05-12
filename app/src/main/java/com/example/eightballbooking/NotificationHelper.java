package com.example.eightballbooking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    private Context mContext;
    private static final String CHANNEL_ID = "appointment_notification_channel";
    private static final String CHANNEL_NAME = "Appointment Notifications";
    private static final String CHANNEL_DESC = "Notifications for Appointments";

    public NotificationHelper(Context context) {
        this.mContext = context;
        createNotificationChannel();
    }

    // Create the notification channel for API 26+
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            NotificationManager manager = mContext.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    // Send high priority notification
    public void sendHighPriorityNotification(String title, String body, Class activityName, int notificationId) {
        Intent intent = new Intent(mContext, activityName);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_ball_icon)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(Color.BLUE)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}); // Vibration pattern: Wait, Vibrate, Sleep, Vibrate, Sleep

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        if (ActivityCompat.checkSelfPermission(this.mContext, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }
}
