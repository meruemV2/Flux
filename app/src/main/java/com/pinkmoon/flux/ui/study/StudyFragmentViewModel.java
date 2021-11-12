package com.pinkmoon.flux.ui.study;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StudyFragmentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StudyFragmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Study fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
