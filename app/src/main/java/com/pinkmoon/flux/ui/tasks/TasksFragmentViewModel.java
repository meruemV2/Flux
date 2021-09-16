package com.pinkmoon.flux.ui.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TasksFragmentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TasksFragmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Tasks fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}