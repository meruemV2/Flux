package com.pinkmoon.flux.db.canvas_classes.assignment;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pinkmoon.flux.API.Assignment;
import com.pinkmoon.flux.API.Course;
import com.pinkmoon.flux.FluxDate;
import com.pinkmoon.flux.db.FluxDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Handles all operations that will interact with the DB and API.
 * All requests will be passed down to this class from its respective
 * ViewModel class. Do not call on methods from this class explicitly within
 * UI or business logic code. Use the ViewModel instead.
 */
public class AssignmentRepository {
    private AssignmentDao assignmentDao;

    private LiveData<List<Assignment>> allAssignments;
    private LiveData<List<Assignment>> assignmentsByYearMonth;

    private Application application; // to be used with Volley for API calls
    public MutableLiveData<List<Assignment>> listOfCanvasAssignmentsByCourse;

    public static final String url = "https://canvas.instructure.com/api/v1/courses/";
    public static final String accessToken = "10284~nqtIdmzKxZtdTw324H6HQ3zZlG9TJSPNqagCfIlgjPwiErZttFv5Yj1ticxUT0xN";

    public AssignmentRepository(Application application){
        FluxDB fluxDB = FluxDB.getInstance(application);

        assignmentDao = fluxDB.assignmentDao();

        allAssignments = assignmentDao.getAllAssignments();

        this.application = application;
    }

    public AssignmentRepository(Context context)
    {
        FluxDB fluxDB = FluxDB.getInstance(application);
        assignmentDao = fluxDB.assignmentDao();
    }

    public void tagReminderComplete(int reminderStatus, int reminderId)
    {
        new TagReminderAsCompleteTask(assignmentDao, reminderStatus, reminderId).execute();
    }



    public void insertAssignment(Assignment... assignment) {
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

    public LiveData<List<Assignment>> getAssignmentsByYearMonth(String yearMonth){
        assignmentsByYearMonth = assignmentDao.getAssignmentsByYearMonth(yearMonth);
        return assignmentsByYearMonth;
    }

    public MutableLiveData<List<Assignment>> getListOfCanvasAssignmentsByCourse(List<Course> courses) {
        if(listOfCanvasAssignmentsByCourse == null){
            listOfCanvasAssignmentsByCourse = new MutableLiveData<>();
        }
        loadCanvasAssignments(courses);
        return listOfCanvasAssignmentsByCourse;
    }

    // Async Task Operations
    public class InsertAssignmentAsync extends AsyncTask<Assignment, Void, Void> {

        AssignmentDao assignmentDao;
        public InsertAssignmentAsync(AssignmentDao assignmentDao) {
            this.assignmentDao = assignmentDao;
        }


        @Override
        protected Void doInBackground(Assignment... assignments) {
            assignmentDao.insertAssignment(assignments);
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

    public class TagReminderAsCompleteTask extends AsyncTask<Void, Void, Void>{
        AssignmentDao assignmentDao;
        int reminderStatus;
        int reminderId;

        public TagReminderAsCompleteTask(AssignmentDao assignmentDao, int reminderStatus, int reminderId) {
            this.assignmentDao = assignmentDao;
            this.reminderStatus = reminderStatus;
            this.reminderId = reminderId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assignmentDao.tagReminderAsComplete(reminderStatus, reminderId);
            return null;
        }
    }

    // API Calls
    private void loadCanvasAssignments(List<Course> courses) {
        for (Course course: courses) {
            String assignmentsURL = url + course.getCourseId() + "/assignments";
            RequestQueue queue = Volley.newRequestQueue(application);

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, assignmentsURL,
                    null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    List<Assignment> canvasAssignmentList = convertJSONToListOfObject(response);
                    listOfCanvasAssignmentsByCourse.setValue(canvasAssignmentList);
                }
            }, error -> {
                Toast.makeText(application, "ERROR: Volley could not satisfy the request for ASSIGNMENTS", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + accessToken);
                    return headers;
                }
            };
            queue.add(request);
        }
    }

    private List<Assignment> convertJSONToListOfObject(JSONArray response){
        List<Assignment> list = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try{
                JSONObject singleAssignment = response.getJSONObject(i);
                Gson gson = new Gson();
                Assignment assignment = gson.fromJson(singleAssignment.toString(), Assignment.class);
                assignment.setAssignmentDueDate(FluxDate.convertToLocalTime(assignment.getAssignmentDueDate()));
                if(assignment.getAssignmentName() != null && assignment.getAssignmentId() != null){
                    list.add(assignment);
                }else{
                    Log.e("WARNING - EXPECTED", "A JSON Assignment Object did not have a Name nor ID and was ignored");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR","Could not trim some object from the JSONArray of Assignments");
            }
        }
        return list;

    }
}
