package com.pinkmoon.flux.db.canvas_classes.join_course_assignment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseAssignmentJoinDao {

    @Insert
    void insert(CourseAssignmentJoin courseAssignmentJoin);

    @Query("SELECT table_assignment.assignmentId AS assignmentId, " +
            "table_course.courseId AS courseId, " +
            "table_course.courseName AS courseName, " +
            "table_assignment.assignmentName AS assignmentName, " +
            "table_assignment.assignmentDueDate AS assignmentDueDate, " +
            "table_assignment.isComplete AS isComplete " +
            "FROM table_assignment " +
            "LEFT JOIN table_course ON table_assignment.assignmentCourseId = table_course.courseId " +
            "WHERE table_assignment.assignmentDueDate LIKE :dueDate || '%'" +
            "ORDER BY courseName DESC")
    LiveData<List<CourseAssignmentJoin>> getAllCourseAssignmentsByDueDate(String dueDate);

    @Query("SELECT table_assignment.assignmentId AS assignmentId, " +
            "table_course.courseId AS courseId, " +
            "table_course.courseName AS courseName, " +
            "table_assignment.assignmentName AS assignmentName, " +
            "table_assignment.assignmentDueDate AS assignmentDueDate, " +
            "table_assignment.isComplete AS isComplete " +
            "FROM table_assignment " +
            "LEFT JOIN table_course ON table_assignment.assignmentCourseId = table_course.courseId " +
            "WHERE isComplete = :isComplete AND assignmentDueDate > :date " +
            "ORDER BY courseName DESC")
    LiveData<List<CourseAssignmentJoin>> getAllCourseAssignmentsByDateAndCompletedStatus(
            boolean isComplete,
            String date);
}
