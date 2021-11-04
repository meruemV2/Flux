package com.pinkmoon.flux.db.canvas_classes.join_course_assignment;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.pinkmoon.flux.API.Assignment;
import com.pinkmoon.flux.API.Course;

@Entity(tableName = "table_course_assignment_join",
        foreignKeys = {
            @ForeignKey(entity = Course.class,
                parentColumns = "courseId",
                childColumns = "courseId"),
            @ForeignKey(entity = Assignment.class,
                parentColumns = "assignmentId",
                childColumns = "assignmentId")
        })
public class CourseAssignmentJoin {
    @PrimaryKey
    private int assignmentId;

    private int courseId;

    private String courseName;

    private String assignmentName;

    private String assignmentDueDate;

    private boolean isComplete;

    public CourseAssignmentJoin(int assignmentId, int courseId,
                                String courseName, String assignmentName,
                                String assignmentDueDate, boolean isComplete) {
        this.assignmentId = assignmentId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.assignmentName = assignmentName;
        this.assignmentDueDate = assignmentDueDate;
        this.isComplete = isComplete;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentDueDate() {
        return assignmentDueDate;
    }

    public void setAssignmentDueDate(String assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
