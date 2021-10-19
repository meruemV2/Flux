package com.pinkmoon.flux.API;

import com.google.gson.annotations.SerializedName;

public class Course {
    @SerializedName("name")
    private String courseName;
    @SerializedName("id")
    private String courseId;

    public Course(String aCourseName, String aCourseId)
    {
        courseName   = aCourseName;
        courseId     = aCourseId;
    }

    public String GetName() { return courseName; }
    public String GetId()   { return courseId; }

}

