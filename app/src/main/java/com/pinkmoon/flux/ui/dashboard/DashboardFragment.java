package com.pinkmoon.flux.ui.dashboard;

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

public class DashboardFragment extends Fragment {
    // Widgets
    private TextView tvDashboardPlaceholder;

    // Local vars
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        defineWidgets(view);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvDashboardPlaceholder.setText(s);
            }
        });

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
}