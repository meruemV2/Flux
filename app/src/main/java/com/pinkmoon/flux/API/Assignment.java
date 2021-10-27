package com.pinkmoon.flux.API;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "table_assignment")
public class Assignment
{

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @NonNull
    private String assignmentId;

    @SerializedName("name")
    private String assignmentName;

    @SerializedName("course_id")
    private String assignmentCourseId;

    @SerializedName("due_at")
    private String assignmentDueDate;

    @ColumnInfo(defaultValue = "false")
    private boolean isComplete;

    public Assignment(@NonNull String assignmentId, String assignmentName, String assignmentCourseId,
                      String assignmentDueDate, Boolean isComplete) {
        this.assignmentId = assignmentId;
        this.assignmentName = assignmentName;
        this.assignmentCourseId = assignmentCourseId;
        this.assignmentDueDate = assignmentDueDate;
        this.isComplete = isComplete;
    }


    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentCourseId() {
        return assignmentCourseId;
    }

    public void setAssignmentCourseId(String assignmentCourseId) {
        this.assignmentCourseId = assignmentCourseId;
    }

    public String getAssignmentDueDate() {
        return assignmentDueDate;
    }

    public void setAssignmentDueDate(String assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }
}
