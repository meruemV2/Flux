package com.pinkmoon.flux.ui.categories;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.pinkmoon.flux.R;

public class AddEditCategoryFragment extends DialogFragment {

    // widgets
    private TextInputLayout etCategoryName;
    private TextView btnCancel, btnOkay;

    // local vars
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(
                R.layout.fragment_add_edit_category,
                container,
                false
        );

        defineViews(rootView);
        setOnClickListeners(rootView);

        // if we are editing, set the existing name here
        if(!(AddEditCategoryFragmentArgs.fromBundle(getArguments()).getEditCategoryName().isEmpty())){
            etCategoryName.getEditText().setText(
                    AddEditCategoryFragmentArgs.fromBundle(getArguments()).getEditCategoryName()
            );
        }

        return rootView;
    }

    private void setOnClickListeners(View view) {
        btnCancel.setOnClickListener(v -> dismiss());
        btnOkay.setOnClickListener(v -> {
            if(!inputIsEmpty()){
                AddEditCategoryFragmentDirections.ActionAddEditCategoryFragmentToAddEditTaskFragment action =
                        AddEditCategoryFragmentDirections.actionAddEditCategoryFragmentToAddEditTaskFragment();
                action.setNewCategoryName(etCategoryName.getEditText().getText().toString().trim());
                NavHostFragment.findNavController(getParentFragment()).navigate(action);
            }else{
                Toast.makeText(getContext(), "Category name cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean inputIsEmpty() {
        return etCategoryName.getEditText().getText().toString().trim().isEmpty();
    }

    private void defineViews(View view) {
        etCategoryName = view.findViewById(R.id.et_fragment_add_edit_category_cat_name);

        btnCancel = view.findViewById(R.id.tv_fragment_add_edit_category_cancel);
        btnOkay = view.findViewById(R.id.tv_fragment_add_edit_category_okay);
    }

}
