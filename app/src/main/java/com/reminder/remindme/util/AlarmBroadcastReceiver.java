package com.reminder.remindme.util;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.reminder.remindme.R;


public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create the notification to be shown
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_about)
                .setContentTitle("Reminder")
                .setContentText(".")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Get the Notification manager service
        NotificationManager am = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Generate an Id for each notification
        int id = (int) (System.currentTimeMillis()/1000);

        // Show a notification
        if (am != null) {
            am.notify(id, (mBuilder).build());
        }
    }
}
