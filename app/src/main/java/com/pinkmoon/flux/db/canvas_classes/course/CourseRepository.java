package com.pinkmoon.flux.db.canvas_classes.course;

import android.app.Application;
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
import com.pinkmoon.flux.API.Course;
import com.pinkmoon.flux.db.FluxDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all operations that will interact with the DB and API.
 * All requests will be passed down to this class from its respective
 * ViewModel class. Do not call on methods from this class explicitly within
 * UI or business logic code. Use the ViewModel instead.
 */
public class CourseRepository {
    private Application application;  // to be used with API calls
    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;

    public MutableLiveData<List<Course>> listOfCanvasCourses;

    public static final String url = "https://canvas.instructure.com/api/v1/courses/";
    public static final String accessToken = "10284~nqtIdmzKxZtdTw324H6HQ3zZlG9TJSPNqagCfIlgjPwiErZttFv5Yj1ticxUT0xN";

    public CourseRepository(Application application) {
        this.application = application;

        FluxDB fluxDB = FluxDB.getInstance(application);
        courseDao = fluxDB.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public void insertCourse(Course course){
        new InsertCourseAsync(courseDao).execute(course);
    }

    public void updateCourse(Course course){
        new UpdateCourseAsync(courseDao).execute(course);
    }

    public void deleteCourse(Course course){
        new DeleteCourseAsync(courseDao).execute(course);
    }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }

    public class InsertCourseAsync extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        public InsertCourseAsync(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.insertCourse(courses[0]);
            return null;
        }
    }

    public class UpdateCourseAsync extends AsyncTask<Course, Void, Void>{
        private CourseDao courseDao;

        public UpdateCourseAsync(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.updateCourse(courses[0]);
            return null;
        }
    }

    public class DeleteCourseAsync extends AsyncTask<Course, Void, Void>{
        private CourseDao courseDao;

        public DeleteCourseAsync(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.deleteCourse(courses[0]);
            return null;
        }
    }

    public MutableLiveData<List<Course>> getListOfCanvasCourses() {
        if(listOfCanvasCourses == null){
            listOfCanvasCourses = new MutableLiveData<>();
            loadCanvasCourses();
        }
        return listOfCanvasCourses;
    }

    // API Calls
    /**
     * Loads Course objects to MutableLiveData list from Asynchronous Volley call.
     */
    public void loadCanvasCourses(){
        RequestQueue queue = Volley.newRequestQueue(application);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Course> canvasCourseList = convertJSONToListOfObject(response);
                listOfCanvasCourses.setValue(canvasCourseList);
            }
        }, error -> {
            Toast.makeText(application, "ERROR: Volley could not satisfy the request for COURSES", Toast.LENGTH_SHORT).show();
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

    /**
     * Converts a JSON array of objects into a List of POJOs.
     * @param response JSON request response from the volley network API call
     * @return a list of Courses directly from Canvas
     */
    private List<Course> convertJSONToListOfObject(JSONArray response){
        List<Course> list = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try{
                JSONObject singleCourse = response.getJSONObject(i);
                Gson gson = new Gson();
                Course course = gson.fromJson(singleCourse.toString(), Course.class);
                if(course.getCourseName() != null && course.getCourseId() != null){
                    list.add(course);
                }else{
                    Log.e("WARNING - EXPECTED", "A JSON Course Object  did not have a Name and was ignored");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR","Could not trim some object from the JSONArray of Courses");
            }
        }
        return list;
    }
}
