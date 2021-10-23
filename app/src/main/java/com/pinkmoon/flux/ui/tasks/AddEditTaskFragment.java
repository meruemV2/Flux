package com.pinkmoon.flux.ui.tasks;

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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.pinkmoon.flux.R;
import com.pinkmoon.flux.db.category.Category;
import com.pinkmoon.flux.db.category.CategoryViewModel;
import com.pinkmoon.flux.db.task.Task;
import com.pinkmoon.flux.db.task.TaskViewModel;
import com.pinkmoon.flux.ui.categories.AddEditCategoryFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddEditTaskFragment extends Fragment {

    // local vars
    private AddEditTaskFragmentListener mListener;
    private CategoryViewModel categoryViewModel;
    private TaskViewModel taskViewModel;
    private List<Category> listOfCategories = new ArrayList<>();
    private Category selectedCategory;

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
            if(!anyFieldsAreEmpty()){
                insertNewTask();
            }else{
                Toast.makeText(getContext(), "No fields can be empty.", Toast.LENGTH_SHORT).show();
                return true;
            }

            Navigation.findNavController(view)
                    .navigate(R.id.action_addEditTaskFragment_to_navigation_tasks);
            return true;
        }
        return false;
    }

    private boolean anyFieldsAreEmpty() {
        return etTaskName.getEditText().getText().toString().trim().isEmpty() ||
                etTaskDescription.getEditText().getText().toString().trim().isEmpty();
    }

    private void insertNewTask() {
        String taskName = etTaskName.getEditText().getText().toString().trim();
        String taskDescription = etTaskDescription.getEditText().getText().toString().trim();

        // TODO due date must be changed to something picked by the user, this is just to test
        taskViewModel.insertTask(new Task(selectedCategory.getCategoryId(),
                                            taskName,
                                            taskDescription,
                                            LocalDate.now().toString(),
                                            false)
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // insert or update the new category name when we resume this fragment
        String newCatName = AddEditTaskFragmentArgs.fromBundle(getArguments()).getNewCategoryName();
        if(!newCatName.trim().isEmpty()){
            categoryViewModel.insertCategory(new Category(newCatName));
        }
    }

    private void defineViews(View view) {
        spnrCategory = view.findViewById(R.id.spnr_fragment_add_edit_task_category_holder);
        etTaskName = view.findViewById(R.id.et_fragment_add_edit_task_name);
        etTaskDescription = view.findViewById(R.id.et_fragment_add_edit_task_description);
        btnReminder = view.findViewById(R.id.btn_fragment_add_edit_task_reminders);

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
        arrayAdapter.insert("Add new...", listOfCatNames.size());
        spnrCategory.setAdapter(arrayAdapter);
    }

    private void setOnClickListeners() {
        btnReminder.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_addEditTaskFragment_to_reminderDetailFragment);
        });
        
        // Category spinner
        spnrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == adapterView.getCount() - 1 && adapterView.getCount() > 1){
                    // Add new...
                    Navigation.findNavController(view).navigate(R.id.action_addEditTaskFragment_to_addEditCategoryFragment);
                } else {
                    selectedCategory = listOfCategories.get(i);
                    // passing it as a SafeArgs argument
//                    AddEditTaskFragmentDirections.ActionAddEditTaskFragmentToAddEditCategoryFragment action =
//                            AddEditTaskFragmentDirections.actionAddEditTaskFragmentToAddEditCategoryFragment(selectedCategory.getCategoryName());
//                    action.setEditCategoryName(selectedCategory.getCategoryName()); // think this is unecessary
//                    Navigation.findNavController(view).navigate(action);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
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