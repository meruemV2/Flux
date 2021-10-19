package com.pinkmoon.flux.ui.tasks;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.pinkmoon.flux.R;

public class AddEditTaskFragment extends Fragment {

    // local vars
    private AddEditTaskFragmentListener mListener;

    // widgets
    private Spinner spnrCategory;
    private TextInputLayout etTaskName;
    private TextInputLayout etTaskDescription;
    private Button btnReminder;
    private View view; // global for navigation components

    public AddEditTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_options_general_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_menu_options_general_save) {
            Navigation.findNavController(view)
                    .navigate(R.id.action_addEditTaskFragment_to_navigation_tasks);
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_edit_task, container, false);

        defineViews(view);
        setOnClickListeners();

        return view;
    }

    private void defineViews(View view) {
        spnrCategory = view.findViewById(R.id.spnr_fragment_add_edit_task_category_holder);
        etTaskName = view.findViewById(R.id.et_fragment_add_edit_task_name);
        etTaskDescription = view.findViewById(R.id.et_fragment_add_edit_task_description);
        btnReminder = view.findViewById(R.id.btn_fragment_add_edit_task_reminders);
    }

    private void setOnClickListeners() {
        btnReminder.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_addEditTaskFragment_to_reminderDetailFragment);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // define the interface
        if(context instanceof AddEditTaskFragmentListener) {
            mListener = (AddEditTaskFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    " must implement AddEditTaskFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface AddEditTaskFragmentListener {
        // interface methods here, if we end up needing them--otherwise, feel free to delete
        // interface all together and its overriding methods
    }
}