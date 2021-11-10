package com.pinkmoon.flux.ui.tasks;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddEditTaskFragmentViewModel extends ViewModel {

    private MutableLiveData<String> taskName;
    private MutableLiveData<String> taskDescription;
    private MutableLiveData<Integer> categorySpinnerPosition;

    public AddEditTaskFragmentViewModel() {
        taskName = new MutableLiveData<>();
        taskDescription = new MutableLiveData<>();
        categorySpinnerPosition = new MutableLiveData<>();
        categorySpinnerPosition.setValue(0);
    }

    public MutableLiveData<String> getTaskName() {
        return taskName;
    }

    public MutableLiveData<String> getTaskDescription() {
        return taskDescription;
    }

    public MutableLiveData<Integer> getCategorySpinnerPosition() {
        return categorySpinnerPosition;
    }
}
