package com.pinkmoon.flux.ui.notifications;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Calendar;

public class FluxReminderHelper {

    //INTENT EXTRAS, Alarm stuff.
    public static final String EXTRA_REMINDER_TITLE = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_TITLE";
    public static final String EXTRA_REMINDER_BODY = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_BODY";
    public static final String EXTRA_REMINDER_ID =   "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_ID";
    public static final String EXTRA_REMINDER_DATE = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_DATE";
    public static final String EXTRA_REMINDER_TIME = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_TIME";
    public static final String EXTRA_REMINDER_STATUS = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_STATUS";

    // Alarm stuff
    private static AlarmManager alarmManager;
    private static Intent reminderIntent;
    private static PendingIntent pendingIntent;

    public static FluxReminderHelper INSTANCE = null;

    private Activity activity;

    private FluxReminderHelper(Activity activity){
        this.activity = activity;
    }

    public static FluxReminderHelper getInstance(Activity activity){


        if(INSTANCE == null){
            INSTANCE = new FluxReminderHelper(activity);
            //Alarm stuff.
            alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
            reminderIntent = new Intent(activity, AlertReceiver.class);
        }
        return (INSTANCE);
    }

    public void createSingleReminder(long timeInMillis,
                                            String reminderTitle, String reminderBody,
                                            int reminderId){
        reminderFunctionalities(timeInMillis, reminderTitle, reminderBody, reminderId);

    }

    private static void reminderFunctionalities(long timeInMillis,
                                                String reminderTitle, String reminderBody,
                                                int reminderId){
        reminderIntent.putExtra(EXTRA_REMINDER_TITLE, reminderTitle);
        // Reminder intent -- CHECKPOINT
        reminderIntent.putExtra(EXTRA_REMINDER_BODY, reminderBody);
        reminderIntent.putExtra(EXTRA_REMINDER_ID, reminderId);
        pendingIntent = PendingIntent.getBroadcast(
                INSTANCE.activity.getApplicationContext(),
                reminderId,
                reminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }
}
