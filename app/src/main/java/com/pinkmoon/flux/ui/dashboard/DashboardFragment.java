package com.pinkmoon.flux.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pinkmoon.flux.R;
import com.pinkmoon.flux.API.Assignment;
import com.pinkmoon.flux.API.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {
    // Widgets
    private TextView tvDashboardPlaceholder;

    // Local vars
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        defineWidgets(view);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvDashboardPlaceholder.setText(s);
            }
        });

        //Starts the Asynchronous API Calls
        API_CALL();

        return view;
    }

    /**
     * Define all of the widgets you add for each fragment here.
     * @param view instance of the inflated view within the fragment
     */
    private void defineWidgets(View view) {
        tvDashboardPlaceholder = view.findViewById(R.id.tv_dashboard_placeholder);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    //Global Elements
    static List<Course> ListOfCourses = new ArrayList<>();
    static List<Assignment> ListOfAssignments = new ArrayList<>();
    static String accessToken = "10284~qox5hujzApMTFagpoCoqLIU3CC0ABWPRFxwGGnizZEpnnXN8DYzFrYvpDMwGcOA7";


    //Cleans the lists in memory in case the user repeatedly executes the call during
    //a single execution of the app.
    //Sends an API request for the courses. NOTICE: Once the data for the courses is
    //obtained this is designed to automatically redirect itself
    //to call for the assignments of each course via callback.
    //If the callback fails AT ANY POINT nothing happens and the app will not crash
    //That means we will only obtain no data. Logs will reflect all the occurring events
    private void API_CALL()
    {
        ListOfCourses.clear();
        ListOfAssignments.clear();

        GetCanvasCourses();
    }


    void GetCanvasCourses()
    {
        String url = "https://canvas.instructure.com/api/v1/courses/";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        //Generating the API request using the URL, method and desired response type.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                Log.d("API-SUCCESS", "API responded with JSONArray for COURSES");
                //Obtaining Courses

                ListOfCourses = CoursesJSONArrayToList(response);
                GetAssignmentsForAllCourses();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(requireContext(), "ERROR: Volley could not satisfy the request for COURSES", Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            public Map<String, String> getHeaders () throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        queue.add(request);

        Toast. makeText(requireContext(), "Courses", Toast.LENGTH_SHORT).show();

    }

    //Goes over all the courses stored in the list of courses and
    //proceeds to send an API request for the assignments of each course
    private void GetAssignmentsForAllCourses() {
        for (int each = 0; each < ListOfCourses.size(); each++)
        {
            Log.d("TEST", String.valueOf(each));
            GetCanvasAssignments(ListOfCourses.get(each));
        }
    }

    //Takes each of the courses that are in the JSONArray.
    //Splits the array into objects
    //If the objects have a "name" and are accessible then stores
    //them in the linked list of courses
    LinkedList<Course> CoursesJSONArrayToList(JSONArray response)
    {

        //Local List of Courses
        LinkedList<Course> ListOfCourses = new LinkedList<Course>();

        //Going over all the courses in the JSONArray
        for(int i=0; i<response.length(); i++)
        {
            try {
                JSONObject singleCourse = response.getJSONObject(i);
                String stringCourse = singleCourse.toString();

                Gson gson = new Gson();
                Course aCourse = gson.fromJson(stringCourse, Course.class);
                if (singleCourse.has("name") && singleCourse.has("id"))
                {
                    String nameOfACourse,idOfACourse;
                    nameOfACourse = singleCourse.getString("name");
                    idOfACourse = singleCourse.getString("id");
                    ListOfCourses.add(aCourse);

                    Log.d("RECORD ADDED: COURSE",
                            (" Name: " + nameOfACourse +
                                    " Id: " + idOfACourse));
                }
                else {
                    Log.d("WARNING - EXPECTED", "A JSON Course Object  did not have a Name and was ignored");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("ERROR","Could not trim some object from the JSONArray of Courses");
            }

        }

        return ListOfCourses;
    }
    //API Assignments Request [ Asynchronously ]
    void GetCanvasAssignments(Course aCourse)
    {
        Log.d("VERIFICATION", "GetCanvasAssignments");
        String coursesURL = "https://canvas.instructure.com/api/v1/courses/";
        String assignmentsURL = coursesURL + aCourse.GetId() + "/assignments";
        Log.d("VERIFICATION", assignmentsURL);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        // Request a string response from the provided URL.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,assignmentsURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                Log.d("API-SUCCESS", "API responded with JSONArray for Assignments");
                //Obtaining Courses
                LinkedList<Assignment> temp;
                temp = AssignmentJSONArrayToList(response);
                ListOfAssignments.addAll(temp);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), "ERROR: Volley could not satisfy the request for ASSIGNMENTS", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders () throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        queue.add(request);

        Toast. makeText(requireContext(), "Assignments", Toast.LENGTH_SHORT).show();

    }
    LinkedList<Assignment> AssignmentJSONArrayToList (JSONArray response)
    {
        LinkedList<Assignment> ListOfAssignments = new LinkedList<Assignment>();

        //Going over all the courses in the JSONArray
        for(int i=0; i<response.length(); i++)
        {
            try {
                JSONObject singleAssignment = response.getJSONObject(i);
                String stringAssignment = singleAssignment.toString();

                Gson gson = new Gson();
                Assignment aAssignment = gson.fromJson(stringAssignment, Assignment.class);
                ListOfAssignments.add(aAssignment);

                String nameOfAAssignment,idOfAAssignment,dueDateOfAAssignment,courseIdOfAAssignment;
                nameOfAAssignment = singleAssignment.getString("name");
                idOfAAssignment = singleAssignment.getString("id");
                dueDateOfAAssignment = singleAssignment.getString("due_at");
                courseIdOfAAssignment = singleAssignment.getString("course_id");
                Log.d("RECORD ADDED: ASSIGNMENT",
                        (" Name: " + nameOfAAssignment +
                                " Id: " + idOfAAssignment +
                                " Due Date: " + dueDateOfAAssignment +
                                " Course Id: " + courseIdOfAAssignment));

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("ERROR","Could not trim some object from the JSONArray of Assignments");
            }

        }
        //All the Assignments are stored in this list.
        return ListOfAssignments;
    }




}