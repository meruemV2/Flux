package com.pinkmoon.flux.db.settings;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pinkmoon.flux.API.Quiz;
import com.pinkmoon.flux.db.FluxDB;
import com.pinkmoon.flux.db.canvas_classes.quiz.QuizDao;
import com.pinkmoon.flux.db.canvas_classes.quiz.QuizRepository;

import java.util.List;

public class SettingsRepository
{
    private SettingsDao settingsDao;
    private LiveData<List<Settings>> allSettings;
    private Application application;

    public SettingsRepository(SettingsDao settingsDao, LiveData<List<Settings>> allSettings) {
        this.settingsDao = settingsDao;
        this.allSettings = allSettings;
    }

    public SettingsRepository(Application application) {
        FluxDB fluxDB = FluxDB.getInstance(application);

        settingsDao = fluxDB.settingsDao();

        allSettings = settingsDao.getAllSettings();

        this.application = application;
    }

    public void insertSettings(Settings settings) {
        new SettingsRepository.InsertSettingsAsync(settingsDao).execute(settings);
    }

    public void updateSettings(Settings settings){
        new SettingsRepository.UpdateSettingsAsync(settingsDao).execute(settings);
    }

    public LiveData<List<Settings>> getAllSettings() {
        return allSettings;
    }

    // Async Task Operations
    public class InsertSettingsAsync extends AsyncTask<Settings, Void, Void> {

        SettingsDao settingsDao;
        public InsertSettingsAsync(SettingsDao settingsDao) {
            this.settingsDao = settingsDao;
        }

        @Override
        protected Void doInBackground(Settings... settings) {
            settingsDao.insertSettings(settings[0]);
            return null;
        }
    }
    public class UpdateSettingsAsync extends AsyncTask<Settings, Void, Void>{

        SettingsDao settingsDao;
        public UpdateSettingsAsync(SettingsDao settingsDao) {
            this.settingsDao = settingsDao;
        }

        @Override
        protected Void doInBackground(Settings... settings) {
            settingsDao.updateSettings(settings[0]);
            return null;
        }
    }
}
