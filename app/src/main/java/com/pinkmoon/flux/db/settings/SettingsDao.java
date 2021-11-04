package com.pinkmoon.flux.db.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pinkmoon.flux.API.Quiz;

import java.util.List;

public interface SettingsDao
{
    @Insert
    void insertSettings(Settings settings);

    @Update
    void updateSettings(Settings settings);

    @Query("SELECT * FROM table_settings")
    LiveData<List<Settings>> getAllSettings();

}
