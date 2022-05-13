package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.domain.login.User;

import java.util.List;
import java.util.Objects;

public class ManageTasksActivity extends AppCompatActivity {
    private static final String TAG = "ManageTaskActivity";
    private static List<TaskWithSteps> tasks;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tasks);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

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
     * logs the user out and go back to the normal view
     *
     * @param view the view where the button was clicked from
     */
    public void onLogoutClicked(View view) {
        user.logOut();
        Log.d(TAG, "Logout clicked. User Login State: " + user.isLoggedIn());
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        //TODO implement dialog to ask if user really wants to log out
        super.onBackPressed();
    }

    public void onAddTaskClicked(View view) {
        //TODO
        Toast.makeText(this, "clicked on add task", Toast.LENGTH_SHORT).show();

    }

    public void onSettingsClicked(View view) {
        //TODO
        Toast.makeText(this, "on settings button clicked", Toast.LENGTH_SHORT).show();
    }
}