package com.pinkmoon.flux.db.canvas_classes.join_course_assignment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CourseAssignmentJoinViewModel extends AndroidViewModel {

    private CourseAssignmentJoinRepository courseAssignmentJoinRepository;

    private LiveData<List<CourseAssignmentJoin>> assignmentsByDueDate;

    private LiveData<List<CourseAssignmentJoin>> allCourseAssignments;

    public CourseAssignmentJoinViewModel(@NonNull Application application) {
        super(application);

        courseAssignmentJoinRepository = new CourseAssignmentJoinRepository(application);
    }

    public LiveData<List<CourseAssignmentJoin>> getAssignmentsByDueDate(String dueDate) {
        assignmentsByDueDate = courseAssignmentJoinRepository.getAssignmentsByDueDate(dueDate);
        return assignmentsByDueDate;
    }

    public LiveData<List<CourseAssignmentJoin>> getAllCourseAssignmentsByCompletedStatus(boolean isComplete,
                                                                                         String date) {
        allCourseAssignments = courseAssignmentJoinRepository
                .getAllCourseAssignmentsByCompletedStatus(isComplete, date);
        return allCourseAssignments;
    }
}
