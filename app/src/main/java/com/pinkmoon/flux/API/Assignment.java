package com.pinkmoon.flux.API;

import com.google.gson.annotations.SerializedName;

public class Assignment
{
    @SerializedName("name")
    private String assignmentName;
    @SerializedName("id")
    private String assignmentId;
    @SerializedName("due_at")
    private String assignmentDueDate;
    @SerializedName("course_id")
    private String assignmentCourseId;

    public Assignment(String aAssignmentName, String aAssignmentId, String aAssignmentDueDate, String aAssignmentCourseId)
    {
        assignmentName = aAssignmentName;
        assignmentId = aAssignmentId;
        assignmentDueDate = aAssignmentDueDate;
        assignmentCourseId = aAssignmentCourseId;
    }

    public String GetName() { return assignmentName; }
    public String GetId() { return assignmentId; }
    public String GetDueDate() { return assignmentDueDate; }
    public String GetCourseId() { return assignmentCourseId; }
}
