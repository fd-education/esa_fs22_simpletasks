package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.domain.login.User;

import java.util.Date;

public class ManageTasksActivity extends AppCompatActivity {
    private static final String TAG = "ManageTaskActivity";
    private User user;

    /**
     * Set and adjust the view and the user instance
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tasks);

        // Remove the action bar at the top of the screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialize the user
        user = User.getUser();

        Log.d(TAG, "ManageTasks created. User Login State: " + user.isLoggedIn());
    }

    /**
     * Log the user out and go back to the main view.
     *
     * @param view the view whose click event was triggered
     */
    public void onLogoutClicked(View view) {
        user.logOut();
        Log.d(TAG, "Logout clicked. User Login State: " + user.isLoggedIn());
        super.onBackPressed();
    }

    /**
     * Handle click events on the add task button
     *
     * @param view the view that triggered the event
     */
    public void onAddTaskClicked(View view) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(MainActivity.TASK_INTENT_EXTRA, new TaskWithSteps());
        startActivity(intent);
    }

    /**
     * Handle click events on the settings button
     *
     * @param view the view that triggered the event
     */
    public void onSettingsClicked(View view) {
        Log.d(TAG, "on settings button clicked, launching settings activity");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}