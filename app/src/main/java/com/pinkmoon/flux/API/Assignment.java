package com.pinkmoon.flux.API;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "table_assignment")
public class Assignment
{

    @PrimaryKey(autoGenerate = true)
    private int primId;

    @SerializedName("id")
    private String assignmentId;

    @SerializedName("name")
    private String assignmentName;

    @SerializedName("course_id")
    private String assignmentCourseId;

    @SerializedName("due_at")
    private String assignmentDueDate;

    public Assignment(String assignmentId, String assignmentName, String assignmentCourseId, String assignmentDueDate) {
        this.assignmentId = assignmentId;
        this.assignmentName = assignmentName;
        this.assignmentCourseId = assignmentCourseId;
        this.assignmentDueDate = assignmentDueDate;
    }

    public int getPrimId() {
        return primId;
    }

    public void setPrimId(int primId) {
        this.primId = primId;
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
}
