package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.domain.login.User;

import java.util.List;

public class ManageTasksActivity extends AppCompatActivity {
    private static final String TAG = "ManageTaskActivity";
    private static List<TaskWithSteps> tasks;
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

        // Initialize the user
        user = User.getUser();

        Log.d(TAG, "ManageTasks created. User Login State: " + user.isLoggedIn());
    }

    /**
     * Set the tasks of the manage task list view.
     *
     * @param tasks all tasks to be listed in the fragment
     */
    public static void setTasks(List<TaskWithSteps> tasks) {
        ManageTasksActivity.tasks = tasks;
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
        //TODO
        Toast.makeText(this, "clicked on add task", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle click events on the settings button
     *
     * @param view the view that triggered the event
     */
    public void onSettingsClicked(View view) {
        //TODO
        Toast.makeText(this, "on settings button clicked", Toast.LENGTH_SHORT).show();
    }
}