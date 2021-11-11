package com.pinkmoon.flux.db.task;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * A ViewModel class for our Task object. Any database operations should
 * be called using this ViewModel, not the TaskRepository nor the TaskDao.
 */
public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;

    private LiveData<List<Task>> allTasksSortedByDueDate;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        taskRepository = new TaskRepository(application);
        allTasksSortedByDueDate = taskRepository.getAllTasksSortedByDueDate();
    }

    public long insertTask(Task task) {
       return taskRepository.insertTask(task);
    }

    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }

    public LiveData<List<Task>> getAllTasksSortedByDueDate() {
        return allTasksSortedByDueDate;
    }
}
