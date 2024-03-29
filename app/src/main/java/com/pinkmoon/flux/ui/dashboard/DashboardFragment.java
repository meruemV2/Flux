package com.pinkmoon.flux.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pinkmoon.flux.API.Quiz;
import com.pinkmoon.flux.FluxDate;


import com.pinkmoon.flux.R;
import com.pinkmoon.flux.API.Assignment;
import com.pinkmoon.flux.API.Course;
import com.pinkmoon.flux.db.canvas_classes.assignment.AssignmentViewModel;
import com.pinkmoon.flux.db.canvas_classes.course.CourseViewModel;
import com.pinkmoon.flux.db.canvas_classes.join_course_assignment.CourseAssignmentJoin;
import com.pinkmoon.flux.db.canvas_classes.join_course_assignment.CourseAssignmentJoinAdapter;
import com.pinkmoon.flux.db.canvas_classes.join_course_assignment.CourseAssignmentJoinViewModel;
import com.pinkmoon.flux.db.canvas_classes.quiz.QuizViewModel;
import com.pinkmoon.flux.ui.dashboard.calendar.CalendarAdapter;

import com.pinkmoon.flux.ui.notifications.FluxReminderHelper;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private QuizViewModel quizViewModel;

    private List<Course> localCourses = new ArrayList<>();
    private List<Assignment> localAssignments = new ArrayList<>();
    private boolean localCoursesLoaded, localAssignmentsLoaded = false;

    private List<CourseAssignmentJoin> courseAssignmentJoinList = new ArrayList<>();

    private FluxReminderHelper reminderHelper;

    // screen's dimensions
    int sWidth;
    int sHeight;

    private CourseAssignmentJoinAdapter courseAssignmentJoinAdapter;


    // onCreate.

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        assignmentViewModel = new ViewModelProvider(this).get(AssignmentViewModel.class);
        courseAssignmentJoinViewModel = new ViewModelProvider(this).get(CourseAssignmentJoinViewModel.class);
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);


        // get the device dimensions to change the display constraints dynamically
        sWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        sHeight = getContext().getResources().getDisplayMetrics().heightPixels;

        courseAssignmentJoinAdapter = new CourseAssignmentJoinAdapter();

        reminderHelper = FluxReminderHelper.getInstance(getActivity());
    }

    // onCreate VIEW.
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
            }
        });

        assignmentViewModel.getAllAssignments().observe(getViewLifecycleOwner(), new Observer<List<Assignment>>() {
            @Override
            public void onChanged(List<Assignment> assignments) {
                localAssignments = assignments;
                localAssignmentsLoaded = true;
                checkToEnableSwipeRefresh();
                setAssignmentReminders();
            }
        });

        return view;
    }


    // ##########################
    public void reminderSetUp(List<CourseAssignmentJoin> courseAssignmentJoins)
    {
        int i = 0;
        int listSize = courseAssignmentJoins.size();
        for(; i < listSize; i++)
        {
            if(!courseAssignmentJoins.get(i).isComplete())
            {
                if (courseAssignmentJoins.get(i).getAssignmentDueDate() != null &&
                    !courseAssignmentJoins.get(i).getAssignmentDueDate().equals("")) {
                    Calendar c = FluxDate.convertToDateTime(courseAssignmentJoins.get(i).getAssignmentDueDate());
                    // Make a reminder?
                    long miliTime = c.getTimeInMillis();

                    reminderHelper.createSingleReminder(
                            miliTime,
                            courseAssignmentJoins.get(i).getCourseName(),
                            courseAssignmentJoins.get(i).getAssignmentName(),
                            courseAssignmentJoins.get(i).getAssignmentId()
                            );
                }

            }

        }

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
                    loadCanvasQuizzes(courses);
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
                }
            }
        });
    }

    private void loadCanvasQuizzes(List<Course> courses) {
        quizViewModel.getListOfCanvasQuizzes(courses).observe(getViewLifecycleOwner(),
                new Observer<List<Quiz>>() {
                    @Override
                    public void onChanged(List<Quiz> quizzes) {
                        quizViewModel.insertQuiz(quizzes.toArray(quizzes.toArray(new Quiz[0])));
                        srlRefreshHolder.setRefreshing(false);
                    }
                });
    }

    public void setAssignmentReminders(){
        // set reminders for incomplete Canvas assignments
        Calendar now = Calendar.getInstance();
        String date = FluxDate.formatDateForDB(now);
        courseAssignmentJoinViewModel
                .getAllCourseAssignmentsByCompletedStatus(false, FluxDate.formatDateForDB(now))
                .observe(getViewLifecycleOwner(), new Observer<List<CourseAssignmentJoin>>() {
                    @Override
                    public void onChanged(List<CourseAssignmentJoin> courseAssignmentJoins) {
                        reminderSetUp(courseAssignmentJoins);
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
        calendarAdapter = new CalendarAdapter(daysInMonth, FluxDate.monthFromDate(selectedDate));

        // sets the date for the selected month, in order to set the
        // correct due date indicators on the calendar
        assignmentViewModel.getAssignmentsByYearMonth(FluxDate.yearMonthFromDate(selectedDate))
                .observe(getViewLifecycleOwner(), new Observer<List<Assignment>>() {
                    @Override
                    public void onChanged(List<Assignment> assignments) {
                        calendarAdapter.setAllAssignments(assignments);
                    }
                });

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
                                    courseAssignmentJoinList = courseAssignmentJoins;
                                }
                            });
                }
            }
        });

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