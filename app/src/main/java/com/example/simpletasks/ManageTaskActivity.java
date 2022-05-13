package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.List;
import java.util.Objects;

public class ManageTaskActivity extends AppCompatActivity {
    private static final String TAG = "ManageTaskActivity";
    private static List<TaskWithSteps> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_task);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    /**
     * set the tasks of the manage task list view
     *
     * @param tasks all tasks to be listed in the fragment
     */
    public static void setTasks(List<TaskWithSteps> tasks) {
        ManageTaskActivity.tasks = tasks;
    }

    /**
     * logs the user out and go back to the normal view
     *
     * @param view the view where the button was clicked from
     */
    public void onLogoutClicked(View view) {
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

    /**
     * deletes all tasks which are currently in the activity (fetched from db or created)
     *
     * @param view the view this method gets called from
     */
    public void deleteAllTasks(View view) {
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.deleteTasks(tasks);
        Log.d(TAG, "'deleted all tasks' finished");
    }
}