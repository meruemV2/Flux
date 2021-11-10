package com.pinkmoon.flux.ui.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.pinkmoon.flux.MainActivity;
import com.pinkmoon.flux.R;
import com.pinkmoon.flux.ui.dashboard.DashboardFragment;

public class AlertReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManagerCompat;
    public static final String CHANNEL_REMINDER_REG =
            "com.pinkmoon.flux.DashboardFragment.CHANNEL_REMINDER_REG";
    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderTitle = intent.getStringExtra(DashboardFragment.EXTRA_REMINDER_TITLE);
        String reminderBody = intent.getStringExtra(DashboardFragment.EXTRA_REMINDER_BODY);
        int reminderId = intent.getIntExtra(DashboardFragment.EXTRA_REMINDER_ID, -1);

        notificationManagerCompat = NotificationManagerCompat.from(context);

        sendOnRegularReminderChannel(context, reminderTitle, reminderBody, reminderId);

    }

    private void sendOnRegularReminderChannel(Context context, String reminderTitle,
                                              String reminderBody, int reminderId) {
        Intent activityIntent = new Intent(context, DashboardFragment.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                reminderId, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent broadcastIntent = new Intent(context, NotificationInteractionReciever.class);
        broadcastIntent.putExtra(DashboardFragment.EXTRA_REMINDER_ID, reminderId);
        broadcastIntent.putExtra(DashboardFragment.EXTRA_REMINDER_BODY, reminderBody);

        PendingIntent actionIntent = PendingIntent.getBroadcast(context,
                reminderId, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context,
                CHANNEL_REMINDER_REG)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle(reminderTitle)
                .setContentText(reminderBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, "Mark as complete", actionIntent)
                .build();

        notificationManagerCompat.notify(reminderId, notification);
    }
}
