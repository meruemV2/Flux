package com.pinkmoon.flux.API;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "table_course")
public class Course {

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @NonNull
    private String courseId;

    @SerializedName("name")
    private String courseName;

    public Course(@NonNull String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}

