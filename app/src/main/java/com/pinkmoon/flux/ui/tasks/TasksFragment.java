package com.pinkmoon.flux.ui.tasks;

import android.os.Bundle;
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
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pinkmoon.flux.R;
import com.pinkmoon.flux.db.category.Category;
import com.pinkmoon.flux.db.category.CategoryViewModel;
import com.pinkmoon.flux.db.task.Task;
import com.pinkmoon.flux.db.task.TaskViewModel;

public class TasksFragment extends Fragment {

    // Widgets
    private TextView tvTasksPlaceholder;
    private FloatingActionButton fabAddNewTask;

    // local vars
    private TasksFragmentViewModel tasksFragmentViewModel;
    private CategoryViewModel categoryViewModel;
    private TaskViewModel taskViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        defineWidgets(view);
        defineObservers();
        setOnClickListeners();

        //runDBTest();
        return view;
    }

    /**
     * Define all of the widgets you add for each fragment here.
     * @param view instance of the inflated view within the fragment
     */
    private void defineWidgets(View view) {
        tvTasksPlaceholder = view.findViewById(R.id.tv_tasks_placeholder);

        tasksFragmentViewModel =
                new ViewModelProvider(this).get(TasksFragmentViewModel.class);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        fabAddNewTask = view.findViewById(R.id.fab_fragment_tasks_add_new_task);
    }

    private void defineObservers() {
        tasksFragmentViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvTasksPlaceholder.setText(s);
            }
        });
    }

    private void setOnClickListeners() {
        fabAddNewTask.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_navigation_tasks_to_addEditTaskFragment);
        });
    }

    // TODO remove this function, it is just meant to test the DB without UI input
    private void runDBTest() {
        
        categoryViewModel.insertCategory(new Category("Assignments"));
        taskViewModel.insertTask(new Task(1,
                "COP4521 - Assignment 3",
                "Database set up with Python.",
                "09-12-2021 22:59:59",
                false));

        Toast.makeText(getContext(), "Tests complete. Check DB.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}