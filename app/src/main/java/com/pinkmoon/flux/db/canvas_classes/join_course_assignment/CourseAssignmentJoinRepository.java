package com.pinkmoon.flux.db.canvas_classes.join_course_assignment;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pinkmoon.flux.db.FluxDB;

import java.util.List;

public class CourseAssignmentJoinRepository {

    private CourseAssignmentJoinDao courseAssignmentJoinDao;

    private LiveData<List<CourseAssignmentJoin>> assignmentsByDueDate;

    private LiveData<List<CourseAssignmentJoin>> allCourseAssignments;

    public CourseAssignmentJoinRepository(Application application) {
        FluxDB fluxDB = FluxDB.getInstance(application);

        courseAssignmentJoinDao = fluxDB.courseAssignmentJoinDao();
    }

    public LiveData<List<CourseAssignmentJoin>> getAssignmentsByDueDate(String dueDate) {
        assignmentsByDueDate = courseAssignmentJoinDao.getAllCourseAssignmentsByDueDate(dueDate);
        return assignmentsByDueDate;
    }

    public LiveData<List<CourseAssignmentJoin>> getAllCourseAssignmentsByCompletedStatus(boolean isComplete,
                                                                                         String date) {
        allCourseAssignments = courseAssignmentJoinDao
                .getAllCourseAssignmentsByDateAndCompletedStatus(isComplete, date);
        return allCourseAssignments;
    }
}
