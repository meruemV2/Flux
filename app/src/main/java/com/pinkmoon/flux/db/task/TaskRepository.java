package com.pinkmoon.flux.db.task;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pinkmoon.flux.db.FluxDB;

import java.util.List;

/**
 * Handles all operations that will interact with the DB.
 * All requests will be passed down to this class from its respective
 * ViewModel class. Do not call on methods from this class explicitly within
 * UI or business logic code. Use the ViewModel instead.
 */
public class TaskRepository {

    private TaskDao taskDao;

    private LiveData<List<Task>> allTasksSortedByDueDate;

    /**
     * A constructor that will instantiate a repo object.
     * @param application the context this is being used from.
     */
    public TaskRepository(Application application) {
        FluxDB fluxDB = FluxDB.getInstance(application);

        taskDao = fluxDB.taskDao();

        // LiveData objects
        allTasksSortedByDueDate = taskDao.getAllTasksSortedByDueDate();
    }

    // Database operations
    public void insertTask(Task task) {
        new InsertTaskAsync(taskDao).execute(task);
    }

    public void updateTask(Task task) {
        new UpdateTaskAsync(taskDao).execute(task);
    }

    public void deleteTask(Task task) {
        new DeleteTaskAsync(taskDao).execute(task);
    }

    public LiveData<List<Task>> getAllTasksSortedByDueDate() { return allTasksSortedByDueDate; }

    // Async task operations
    public class InsertTaskAsync extends AsyncTask<Task, Void, Void> {

        TaskDao taskDao;

        public InsertTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insertTask(tasks[0]);
            return null;
        }
    }

    public class UpdateTaskAsync extends AsyncTask<Task, Void, Void> {

        TaskDao taskDao;

        public UpdateTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.updateTask(tasks[0]);
            return null;
        }
    }

    public class DeleteTaskAsync extends AsyncTask<Task, Void, Void> {

        TaskDao taskDao;

        public DeleteTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteTask(tasks[0]);
            return null;
        }
    }
}
