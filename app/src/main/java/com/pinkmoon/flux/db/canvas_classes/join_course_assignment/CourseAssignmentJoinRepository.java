package com.pinkmoon.flux.db.canvas_classes.join_course_assignment;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pinkmoon.flux.db.FluxDB;

import java.util.List;

public class CourseAssignmentJoinRepository {

    private CourseAssignmentJoinDao courseAssignmentJoinDao;

    private LiveData<List<CourseAssignmentJoin>> assignmentsByDueDate;

    public CourseAssignmentJoinRepository(Application application) {
        FluxDB fluxDB = FluxDB.getInstance(application);

        courseAssignmentJoinDao = fluxDB.courseAssignmentJoinDao();
    }

    public LiveData<List<CourseAssignmentJoin>> getAssignmentsByDueDate(String dueDate) {
        assignmentsByDueDate = courseAssignmentJoinDao.getAllCourseAssignmentsByDueDate(dueDate);
        return assignmentsByDueDate;
    }
}
