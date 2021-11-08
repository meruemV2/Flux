package com.pinkmoon.flux.db.settings;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "table_settings")
public class Settings
{
    @PrimaryKey(autoGenerate = true)
    private int settingsId;

    private String userAccessToken;

    @ColumnInfo(defaultValue = "D")
    private Character canvasSyncFrequency;

    public Settings(String userAccessToken, Character canvasSyncFrequency) {
        this.userAccessToken = userAccessToken;
        this.canvasSyncFrequency = canvasSyncFrequency;
    }

    public int getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(int settingsId) {
        this.settingsId = settingsId;
    }

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    public Character getCanvasSyncFrequency() {
        return canvasSyncFrequency;
    }

    public void setCanvasSyncFrequency(Character canvasSyncFrequency) {
        this.canvasSyncFrequency = canvasSyncFrequency;
    }
}