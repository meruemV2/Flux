package com.pinkmoon.flux;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pinkmoon.flux.ui.tasks.AddEditTaskFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity
        implements AddEditTaskFragment.AddEditTaskFragmentListener{

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations (to clarify,
        // this is the reason why in our mobile_navigation graph, there are no
        // arrows indicating navigation destinations, since again, the 3 fragments
        // currently on the graph are considered "top" level (global) destinations).
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_tasks, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    // We override the below for the implementation of the back button to navigate back
    // to the previous place in the stack
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}