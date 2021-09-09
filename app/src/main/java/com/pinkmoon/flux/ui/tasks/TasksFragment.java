package com.pinkmoon.flux.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pinkmoon.flux.R;
import com.pinkmoon.flux.databinding.FragmentTasksBinding;

public class TasksFragment extends Fragment {

    // Widgets
    private TextView tvTasksPlaceholder;

    // local vars
    private TasksViewModel tasksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        defineWidgets(view);

        tasksViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvTasksPlaceholder.setText(s);
            }
        });

        return view;
    }

    /**
     * Define all of the widgets you add for each fragment here.
     * @param view instance of the inflated view within the fragment
     */
    private void defineWidgets(View view) {
        tvTasksPlaceholder = view.findViewById(R.id.tv_tasks_placeholder);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}