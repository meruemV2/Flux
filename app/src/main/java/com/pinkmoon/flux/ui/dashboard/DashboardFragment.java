package com.pinkmoon.flux.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.pinkmoon.flux.FluxDate;
import com.pinkmoon.flux.R;
import com.pinkmoon.flux.API.Assignment;
import com.pinkmoon.flux.API.Course;
import com.pinkmoon.flux.db.canvas_classes.assignment.AssignmentViewModel;
import com.pinkmoon.flux.db.canvas_classes.course.CourseViewModel;
import com.pinkmoon.flux.db.canvas_classes.join_course_assignment.CourseAssignmentJoin;
import com.pinkmoon.flux.db.canvas_classes.join_course_assignment.CourseAssignmentJoinAdapter;
import com.pinkmoon.flux.db.canvas_classes.join_course_assignment.CourseAssignmentJoinViewModel;
import com.pinkmoon.flux.ui.dashboard.calendar.CalendarAdapter;
import com.pinkmoon.flux.ui.tasks.TasksFragmentDirections;

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
    private RecyclerView calendarRecyclerView, rvDayDetails;
    private TextView tvCourseTest, tvAssignmentTest;
    private ProgressBar progAPILoad;
    private SwipeRefreshLayout srlRefreshHolder;

    private FloatingActionButton fabAddNewTask;

    // top calendar controls
    private CalendarAdapter calendarAdapter;
    private Button btnPrevMonth, btnNextMonth;
    // calendar holder
    private LinearLayout llCalendarHolder;

    // Local vars
    private DashboardViewModel dashboardViewModel;
    private LocalDate selectedDate;

    private CourseViewModel courseViewModel;
    private AssignmentViewModel assignmentViewModel;
    private CourseAssignmentJoinViewModel courseAssignmentJoinViewModel;

    private List<Course> localCourses = new ArrayList<>();
    private List<Assignment> localAssignments = new ArrayList<>();
    private boolean localCoursesLoaded, localAssignmentsLoaded = false;

    // screen's dimensions
    int sWidth;
    int sHeight;

    private CourseAssignmentJoinAdapter courseAssignmentJoinAdapter;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        assignmentViewModel = new ViewModelProvider(this).get(AssignmentViewModel.class);
        courseAssignmentJoinViewModel = new ViewModelProvider(this).get(CourseAssignmentJoinViewModel.class);

        // get the device dimensions to change the display constraints dynamically
        sWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        sHeight = getContext().getResources().getDisplayMetrics().heightPixels;

        courseAssignmentJoinAdapter = new CourseAssignmentJoinAdapter();
    }

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

        srlRefreshHolder.setEnabled(false);
        courseViewModel.getAllCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                localCourses = courses;
                localCoursesLoaded = true;
                checkToEnableSwipeRefresh();
                Toast.makeText(getContext(), "Local courses updated.", Toast.LENGTH_SHORT).show();
            }
        });

        assignmentViewModel.getAllAssignments().observe(getViewLifecycleOwner(), new Observer<List<Assignment>>() {
            @Override
            public void onChanged(List<Assignment> assignments) {
                localAssignments = assignments;
                localAssignmentsLoaded = true;
                checkToEnableSwipeRefresh();
                calendarAdapter.setAllAssignments(assignments); // set the indicators on the calendar
                Toast.makeText(getContext(), "Local assignments updated.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void checkToEnableSwipeRefresh() {
        if(localCoursesLoaded && localAssignmentsLoaded){
            srlRefreshHolder.setEnabled(true);
        }
    }

    private void loadCanvasCourses() {
        courseViewModel.getListOfCanvasCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                    courseViewModel.insertCourse(courses.toArray(courses.toArray(new Course[0])));
                    loadCanvasAssignments(courses);
                }
            }
        });
    }

    private void loadCanvasAssignments(List<Course> courses){
        assignmentViewModel.getListOfCanvasAssignments(courses).observe(getViewLifecycleOwner(),
                new Observer<List<Assignment>>() {
            @Override
            public void onChanged(List<Assignment> assignments) {
                if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                    assignmentViewModel.insertAssignment(assignments.toArray(new Assignment[0]));
                    srlRefreshHolder.setRefreshing(false);
                }
            }
        });
    }

    /**
     * Define all of the widgets you add for each fragment here.
     * @param view instance of the inflated view within the fragment
     */
    private void defineWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        rvDayDetails = view.findViewById(R.id.rv_fragment_dashboard_day_details);
        monthYearText = view.findViewById(R.id.monthYearTV);

        btnPrevMonth = view.findViewById(R.id.btn_fragment_dashboard_prev_month);
        btnNextMonth = view.findViewById(R.id.btn_fragment_dashboard_next_month);

        llCalendarHolder = view.findViewById(R.id.ll_fragment_dashboard_calendar_holder);
        llCalendarHolder.setLayoutParams(new LinearLayout.LayoutParams(sWidth, sHeight));

        progAPILoad = view.findViewById(R.id.prog_fragment_dashboard_api_load);

        srlRefreshHolder = view.findViewById(R.id.srl_fragment_dashboard_refresh_holder);

        fabAddNewTask = view.findViewById(R.id.fab_fragment_dashboard_add_new_task);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setCalendarViews() {
        // month view
        monthYearText.setText(FluxDate.monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = FluxDate.daysInMonthArray(selectedDate);
        calendarAdapter = new CalendarAdapter(daysInMonth, FluxDate.monthYearFromDate(selectedDate));

        // rv stuff
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7); // 7 columns in the rv
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String dayText) {
                if(!dayText.equals(" ")){
                    courseAssignmentJoinViewModel
                            .getAssignmentsByDueDate(FluxDate.formatDateForDB(selectedDate, dayText))
                            .observe(getViewLifecycleOwner(), new Observer<List<CourseAssignmentJoin>>() {
                                @Override
                                public void onChanged(List<CourseAssignmentJoin> courseAssignmentJoins) {
                                    courseAssignmentJoinAdapter
                                            .setAssignmentsByDueDate(courseAssignmentJoins);
                                }
                            });
                }
            }
        });
        // update the due items.
        //calendarAdapter.setAllAssignments(localAssignments);

        rvDayDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDayDetails.setHasFixedSize(true);
        rvDayDetails.setAdapter(courseAssignmentJoinAdapter);
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
        
        srlRefreshHolder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Starts the Asynchronous API Calls
                loadCanvasCourses();
            }
        });

        fabAddNewTask.setOnClickListener(view -> {
            DashboardFragmentDirections.ActionNavigationDashboardToAddEditTaskFragment action =
                    DashboardFragmentDirections.actionNavigationDashboardToAddEditTaskFragment();
            Navigation.findNavController(view).navigate(action);
        });
    }
}