package com.pinkmoon.flux.db.canvas_classes.assignment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinkmoon.flux.API.Assignment;
import com.pinkmoon.flux.API.Course;

import java.util.List;

public class AssignmentViewModel extends AndroidViewModel {

    private AssignmentRepository assignmentRepository;

    private LiveData<List<Assignment>> allAssignments;

    private MutableLiveData<List<Assignment>> listOfCanvasAssignmentsByCourse;

    public AssignmentViewModel(@NonNull Application application) {
        super(application);

        assignmentRepository = new AssignmentRepository(application);

        allAssignments = assignmentRepository.getAllAssignments();
    }

    public void insertAssignment(Assignment assignment){
        assignmentRepository.insertAssignment(assignment);
    }

    public void updateAssignment(Assignment assignment){
        assignmentRepository.updateAssignment(assignment);
    }

    public void deleteAssignment(Assignment assignment){
        assignmentRepository.deleteAssignment(assignment);
    }

    public LiveData<List<Assignment>> getAllAssignments() {
        return allAssignments;
    }

    public MutableLiveData<List<Assignment>> getListOfCanvasAssignments(Course course) {
        listOfCanvasAssignmentsByCourse = assignmentRepository.getListOfCanvasAssignmentsByCourse(course);
        return listOfCanvasAssignmentsByCourse;
    }
}
