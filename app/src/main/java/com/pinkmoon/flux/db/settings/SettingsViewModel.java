package com.pinkmoon.flux.db.settings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel
{
    private SettingsRepository settingsRepository;

    private LiveData<List<Settings>> allSettings;

    public SettingsViewModel(@NonNull Application application) {
        super(application);

        settingsRepository = new SettingsRepository(application);

        allSettings = settingsRepository.getAllSettings();

    }


    public void insertSettings(Settings settings) {
        settingsRepository.insertSettings(settings);
    }

    public void updateSettings(Settings settings){
        settingsRepository.updateSettings(settings);
    }

    public LiveData<List<Settings>> getAllSettings(){
        return allSettings;
    }

}
