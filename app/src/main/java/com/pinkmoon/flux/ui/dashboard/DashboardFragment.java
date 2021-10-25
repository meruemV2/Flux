package com.pinkmoon.flux.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.pinkmoon.flux.db.canvas_classes.assignment.AssignmentViewModel;
import com.pinkmoon.flux.db.canvas_classes.course.CourseViewModel;
import com.pinkmoon.flux.ui.dashboard.calendar.CalendarAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {
    // Widgets
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private TextView tvCourseTest, tvAssignmentTest;
    private ProgressBar progAPILoad;

    // top calendar controls
    private Button btnPrevMonth, btnNextMonth;

    // Local vars
    private DashboardViewModel dashboardViewModel;
    public static final String accessToken = "10284~nqtIdmzKxZtdTw324H6HQ3zZlG9TJSPNqagCfIlgjPwiErZttFv5Yj1ticxUT0xN";
    private LocalDate selectedDate;

    private CourseViewModel courseViewModel;
    private AssignmentViewModel assignmentViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        defineWidgets(view);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        selectedDate = LocalDate.now();
        setCalendarViews();
        setOnClickListeners();

        //Starts the Asynchronous API Calls
        loadCanvasCourses();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progAPILoad.setVisibility(View.VISIBLE);
    }

    private void loadCanvasCourses() {
        StringBuilder courseTest = new StringBuilder();
        courseViewModel.getListOfCanvasCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                for (int i = 0; i < courses.size(); i++) {
                    courseTest.append("RECORD ADDED: COURSE Name: ")
                            .append(courses.get(i).getCourseName())
                            .append(" Id: ")
                            .append(courses.get(i).getCourseId())
                            .append("\n");
                    tvCourseTest.setText(courseTest);

                    loadCanvasAssignments(courses);
                }
            }
        });
    }

    private void loadCanvasAssignments(List<Course> courses){
        StringBuilder assignmentTest = new StringBuilder();
        for (Course course: courses) {
            assignmentViewModel.getListOfCanvasAssignments(course).observe(getViewLifecycleOwner(),
                    new Observer<List<Assignment>>() {
                        @Override
                        public void onChanged(List<Assignment> assignments) {
                            for (Assignment assignment:
                                 assignments) {
                                assignmentTest.append("RECORD ADDED: COURSE Name: ")
                                        .append(assignment.getAssignmentName())
                                        .append(" Id: ")
                                        .append(assignment.getAssignmentCourseId())
                                        .append("\n");
                                tvAssignmentTest.setText(assignmentTest);
                            }
                            progAPILoad.setVisibility(View.GONE);
                        }
                    });
        }
    }

    /**
     * Define all of the widgets you add for each fragment here.
     * @param view instance of the inflated view within the fragment
     */
    private void defineWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        tvCourseTest = view.findViewById(R.id.tv_fragment_dashboard_course_test);
        tvAssignmentTest = view.findViewById(R.id.tv_fragment_dashboard_course_assignment);

        btnPrevMonth = view.findViewById(R.id.btn_fragment_dashboard_prev_month);
        btnNextMonth = view.findViewById(R.id.btn_fragment_dashboard_next_month);

        progAPILoad = view.findViewById(R.id.prog_fragment_dashboard_api_load);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        assignmentViewModel = new ViewModelProvider(this).get(AssignmentViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setCalendarViews() {
        // month view
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth);

        // rv stuff
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7); // 7 columns in the rv
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String dayText) {
                if(!dayText.equals(" ")){
                    String m = dayText + " " + monthYearFromDate(selectedDate);
                    Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ArrayList<String> daysInMonthArray(LocalDate selectedDate) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1); // get first day of the month
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i < 42; i++){
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add(" "); // we add a blank
            }else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private void setOnClickListeners() {
        btnPrevMonth.setOnClickListener(view -> {
            selectedDate = selectedDate.minusMonths(1);
            setCalendarViews();
        });

        btnNextMonth.setOnClickListener(view -> {
            selectedDate = selectedDate.plusMonths(1);
            setCalendarViews();
        });
    }
}