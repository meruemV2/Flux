package com.pinkmoon.flux.db.canvas_classes.assignment;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pinkmoon.flux.API.Assignment;
import com.pinkmoon.flux.db.FluxDB;

import java.util.List;

/**
 * Handles all operations that will interact with the DB and API.
 * All requests will be passed down to this class from its respective
 * ViewModel class. Do not call on methods from this class explicitly within
 * UI or business logic code. Use the ViewModel instead.
 */
public class AssignmentRepository {
    private AssignmentDao assignmentDao;

    private LiveData<List<Assignment>> allAssignments;

    private Application application; // to be used with Volley for API calls

    // API Stuff

    public AssignmentRepository(Application application){
        FluxDB fluxDB = FluxDB.getInstance(application);

        assignmentDao = fluxDB.assignmentDao();

        allAssignments = assignmentDao.getAllAssignments();

        this.application = application;
    }

    public void insertAssignment(Assignment assignment) {
        new InsertAssignmentAsync(assignmentDao).execute(assignment);
    }

    public void updateAssignment(Assignment assignment){
        new UpdateAssignmentAsync(assignmentDao).execute(assignment);
    }

    public void deleteAssignment(Assignment assignment) {
        new DeleteAssignmentAsync(assignmentDao).execute(assignment);
    }

    public LiveData<List<Assignment>> getAllAssignments() {
        return allAssignments;
    }

    // Async Task Operations
    public class InsertAssignmentAsync extends AsyncTask<Assignment, Void, Void> {
        AssignmentDao assignmentDao;

        public InsertAssignmentAsync(AssignmentDao assignmentDao) {
            this.assignmentDao = assignmentDao;
        }

        @Override
        protected Void doInBackground(Assignment... assignments) {
            assignmentDao.insertAssignment(assignments[0]);
            return null;
        }
    }

    public class UpdateAssignmentAsync extends AsyncTask<Assignment, Void, Void>{
        AssignmentDao assignmentDao;

        public UpdateAssignmentAsync(AssignmentDao assignmentDao) {
            this.assignmentDao = assignmentDao;
        }

        @Override
        protected Void doInBackground(Assignment... assignments) {
            assignmentDao.updateAssignment(assignments[0]);
            return null;
        }
    }

    public class DeleteAssignmentAsync extends AsyncTask<Assignment, Void, Void> {
        AssignmentDao assignmentDao;

        public DeleteAssignmentAsync(AssignmentDao assignmentDao) {
            this.assignmentDao = assignmentDao;
        }

        @Override
        protected Void doInBackground(Assignment... assignments) {
            assignmentDao.deleteAssignment(assignments[0]);
            return null;
        }
    }
}
