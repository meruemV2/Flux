package com.pinkmoon.flux.ui.notifications;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;

// Making Notification Channel1
//Two channels: Canvas Assignments, Normal Tasks

public class NotificationChannel extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            android.app.NotificationChannel channel1 = new android.app.NotificationChannel(
                    CHANNEL_1_ID,
                    "Flux Canvas Notification",
                    IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1. It has Canvas notifications.");

            android.app.NotificationChannel channel2 = new android.app.NotificationChannel(
                    CHANNEL_2_ID,
                    "Flux Task Notification",
                    IMPORTANCE_HIGH
            );
            channel2.setDescription("This is Channel 2. It has tasks notifications.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }


    }
}
