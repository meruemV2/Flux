package com.pinkmoon.flux.ui.notifications;

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

public class NotificationsFragment extends Fragment {

    // Widgets
    private TextView tvNotificationsPlaceholder;

    // Local vars
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        defineWidgets(view);

        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvNotificationsPlaceholder.setText(s);
            }
        });

        return view;
    }

    /**
     * Define all of the widgets you add for each fragment here.
     * @param view instance of the inflated view within the fragment
     */
    private void defineWidgets(View view) {
        tvNotificationsPlaceholder = view.findViewById(R.id.tv_notifications_placeholder);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}