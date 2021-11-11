package com.pinkmoon.flux.ui.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.pinkmoon.flux.FluxDate;
import com.pinkmoon.flux.R;
import com.pinkmoon.flux.db.category.Category;
import com.pinkmoon.flux.db.category.CategoryViewModel;
import com.pinkmoon.flux.db.task.Task;
import com.pinkmoon.flux.db.task.TaskViewModel;

import com.pinkmoon.flux.ui.notifications.FluxReminderHelper;

import java.util.ArrayList;

import java.util.List;

public class AddEditTaskFragment extends Fragment {

    // local vars
    private AddEditTaskFragmentViewModel addEditTaskFragmentViewModel;
    private AddEditTaskFragmentListener mListener;
    private CategoryViewModel categoryViewModel;
    private TaskViewModel taskViewModel;
    private List<Category> listOfCategories = new ArrayList<>();
    private Category selectedCategory;
    private String selectedDateTime = "";
    private FluxReminderHelper reminderHelper;

    // widgets
    private Spinner spnrCategory;
    private TextInputLayout etTaskName;
    private TextInputLayout etTaskDescription;
    private Button btnSetDueDate;
    private Button btnReminder;
    private View view; // global for navigation components
    private TextView tvAddNewCategory, tvEditCategory;

    public AddEditTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        reminderHelper = FluxReminderHelper.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        // insert or update the new category name when we resume this fragment
        String newCatName = AddEditTaskFragmentArgs.fromBundle(getArguments()).getNewCategoryName();
        boolean isEdit = AddEditTaskFragmentArgs.fromBundle(getArguments()).getIsCategoryEdit();
        int selectedCategoryId = AddEditTaskFragmentArgs.fromBundle(getArguments()).getSelectedCategoryId();
        if(!newCatName.trim().isEmpty() && !isEdit){
            // insert a new category
            categoryViewModel.insertCategory(new Category(newCatName));
        }else if(!newCatName.trim().isEmpty() && isEdit && selectedCategoryId != -1){
            Category updatedCategory = new Category(newCatName);
            updatedCategory.setCategoryId(selectedCategoryId);
            categoryViewModel.updateCategory(updatedCategory);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // save the state of views
        addEditTaskFragmentViewModel.getTaskName().setValue(etTaskName.getEditText().getText().toString().trim());
        addEditTaskFragmentViewModel.getTaskDescription().setValue(etTaskDescription.getEditText().getText().toString().trim());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_options_general_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_menu_options_general_save) {
            if(!anyFieldsAreEmpty()){
                if(!selectedDateTime.isEmpty()){
                    Task insertedTask = insertNewTask();
                    scheduleReminder(insertedTask);
                    Toast.makeText(getContext(), "Reminder scheduled.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "You must select a due date & time first.", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }else{
                Toast.makeText(getContext(), "No fields can be empty.", Toast.LENGTH_SHORT).show();
                return true;
            }

            Navigation.findNavController(view)
                    .navigate(R.id.action_addEditTaskFragment_to_navigation_dashboard);
            return true;
        }
        return false;
    }

    private boolean anyFieldsAreEmpty() {
        return etTaskName.getEditText().getText().toString().trim().isEmpty() ||
                etTaskDescription.getEditText().getText().toString().trim().isEmpty();
    }

    private Task insertNewTask() {
        String taskName = etTaskName.getEditText().getText().toString().trim();
        String taskDescription = etTaskDescription.getEditText().getText().toString().trim();

        Task task = new Task(selectedCategory.getCategoryId(),
                taskName,
                taskDescription,
                selectedDateTime,
                false);
        task.setTaskId((int) taskViewModel.insertTask(task));
        return task;
    }

    public void scheduleReminder(Task insertedTask){
        reminderHelper.createSingleReminder(
                FluxDate.convertToDateTime(selectedDateTime).getTimeInMillis(),
                insertedTask.getTaskName(),
                insertedTask.getTaskDescription(),
                insertedTask.getTaskId()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_edit_task, container, false);

        defineViews(view);
        loadDBData();
        setOnClickListeners();
        defineObservers();

        return view;
    }

    private void defineViews(View view) {
        spnrCategory = view.findViewById(R.id.spnr_fragment_add_edit_task_category_holder);
        etTaskName = view.findViewById(R.id.et_fragment_add_edit_task_name);
        etTaskDescription = view.findViewById(R.id.et_fragment_add_edit_task_description);
        btnSetDueDate = view.findViewById(R.id.btn_fragment_add_edit_task_set_due_date);
        btnReminder = view.findViewById(R.id.btn_fragment_add_edit_task_reminders);

        tvAddNewCategory = view.findViewById(R.id.tv_fragment_add_edit_task_add_new_category);
        tvEditCategory = view.findViewById(R.id.tv_fragment_add_edit_task_edit_category);

        addEditTaskFragmentViewModel = new ViewModelProvider(this).get(AddEditTaskFragmentViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
    }

    private void loadDBData() {
        categoryViewModel.getAllCategoriesAlphabetically().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                fillCategorySpinner(categories);
                listOfCategories = categories; // stored globally to be able to use within spinner
            }
        });
    }

    private void fillCategorySpinner(List<Category> categories) {
        ArrayList<String> listOfCatNames = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            listOfCatNames.add(categories.get(i).getCategoryName());
        }
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listOfCatNames);
        arrayAdapter.insert("Select a category...", 0);
        spnrCategory.setAdapter(arrayAdapter);
    }

    private void setOnClickListeners() {
        btnSetDueDate.setOnClickListener(v -> {
            callDatePickerDialog();
        });

        btnReminder.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_addEditTaskFragment_to_reminderDetailFragment);
        });
        
        // Category spinner
        spnrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // set the Fragment ViewModel val
                addEditTaskFragmentViewModel.getCategorySpinnerPosition().setValue(i);
                if(i == 0){
                    // Add new...
                    tvEditCategory.setVisibility(View.GONE);
                    tvAddNewCategory.setVisibility(View.VISIBLE);
                } else {
                    // Edit existing category
                    tvEditCategory.setVisibility(View.VISIBLE);
                    tvAddNewCategory.setVisibility(View.GONE);
                    if (!listOfCategories.isEmpty()) {
                        selectedCategory = listOfCategories.get(i - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvAddNewCategory.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_addEditTaskFragment_to_addEditCategoryFragment);
        });

        tvEditCategory.setOnClickListener(v -> {
             // passing it as a SafeArgs argument
                    AddEditTaskFragmentDirections.ActionAddEditTaskFragmentToAddEditCategoryFragment action =
                            AddEditTaskFragmentDirections.actionAddEditTaskFragmentToAddEditCategoryFragment();
                    action.setEditCategoryName(selectedCategory.getCategoryName());
                    action.setEditCategoryId(selectedCategory.getCategoryId());
                    Navigation.findNavController(view).navigate(action);
        });
    }

    private void defineObservers(){
        addEditTaskFragmentViewModel.getTaskName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                etTaskName.getEditText().setText(s);
            }
        });

        addEditTaskFragmentViewModel.getTaskDescription().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                etTaskDescription.getEditText().setText(s);
            }
        });

        addEditTaskFragmentViewModel.getCategorySpinnerPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                spnrCategory.setSelection(integer, true);
            }
        });
    }

    private void callDatePickerDialog(){
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker mdp = materialDateBuilder.build();
        mdp.show(getChildFragmentManager(),"Material date picker from AddEditTaskFragment.java");
        mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                callTimePickerDialog(mdp.getHeaderText());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void callTimePickerDialog(String selectedDate){
        MaterialTimePicker mtp = new MaterialTimePicker.Builder()
                .build();

        mtp.show(getChildFragmentManager(), "Material time picker from AddEditTaskFragment.java");
        mtp.addOnPositiveButtonClickListener(v -> {
            String hour = (FluxDate.isSingleDigitTime(String.valueOf(mtp.getHour()))) ?
                    FluxDate.singleDigitToDouble(mtp.getHour()) : String.valueOf(mtp.getHour());
            String minute = (FluxDate.isSingleDigitTime(String.valueOf(mtp.getMinute()))) ?
                    FluxDate.singleDigitToDouble(mtp.getMinute()) : String.valueOf(mtp.getMinute());

            // sets the date to be attached to the Task
            selectedDateTime = FluxDate.
                    formatDateTimeFromMDPToDBFormat(
                            selectedDate + " " + hour + ":" + minute + ":00"
                    );
            btnSetDueDate.setText("Due date: " + selectedDate + " " + hour + ":" + minute + ":00");
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