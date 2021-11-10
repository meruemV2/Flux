package com.pinkmoon.flux.ui.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.pinkmoon.flux.db.FluxDB;
import com.pinkmoon.flux.db.canvas_classes.assignment.AssignmentRepository;
import com.pinkmoon.flux.db.canvas_classes.assignment.AssignmentViewModel;
import com.pinkmoon.flux.ui.dashboard.DashboardFragment;

public class NotificationInteractionReciever extends BroadcastReceiver {
    private int reminderId;
    private NotificationManagerCompat notificationManagerCompat;
    private AssignmentRepository assignmentRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        reminderId = intent.getIntExtra(DashboardFragment.EXTRA_REMINDER_ID, -1);
        notificationManagerCompat = NotificationManagerCompat.from(context);


        new ExecuteReminderStatusChange(context).execute();
    }

    private class ExecuteReminderStatusChange extends AsyncTask<Context, Void, Void> {
        Context passedContext;

        public ExecuteReminderStatusChange(Context passedContext) {
            this.passedContext = passedContext;

        }

        @Override
        protected Void doInBackground(Context... contexts) {
           assignmentRepository  = new AssignmentRepository(passedContext);
           assignmentRepository.tagReminderComplete(true, reminderId);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }
}
