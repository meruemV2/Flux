package com.pinkmoon.flux.ui.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    

    private Calendar fetchDateTimeFromDB;
    private Calendar today;
    private long timeDifference;



    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
