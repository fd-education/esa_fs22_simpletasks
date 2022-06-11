package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.List;

/**
 * Activity for the main screen
 */
public class MainActivity extends AppCompatActivity {
    // Keys for the intent extras
    public static final String TASK_INTENT_EXTRA = "task_intent_extra";
    public static final String TASK_ID_INTENT_EXTRA = "task_id_intent_extra";
    public static final String SHARED_PREF_KEY = "SIMPLE_TASK_SHARED_PREF";
    public static final String CURRENT_TASK_STEP_INTENT_EXTRA = "current_task_step_intent_extra";

    private static final String TAG = "MainActivity";
    private static List<TaskWithSteps> tasks;
    private static ViewModelStoreOwner owner;

    /**
     * Set and adjust the view
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        owner = this;

        Log.d(TAG, "finished initialisation");
    }

    /**
     * Handle click events of the login button
     *
     * @param view the view whose click event was triggered
     */
    public void onLoginClicked(View view) {
        // TODO go to manage tasks and change button text if no pin set
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Handle click events of the settings button
     *
     * @param view the view whose click event was triggered
     */
    public void onSettingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Set the tasks in the task list view
     *
     * @param tasks all tasks to be listed in the fragment
     */
    public static void setTasks(List<TaskWithSteps> tasks) {
        MainActivity.tasks = tasks;
    }

    /**
     * Updates the tasks in the database
     *
     * @param tasks the list with the tasks
     */
    public static void updateTasksInDatabase(List<TaskWithSteps> tasks) {
        Log.d(TAG, "updating tasks");
        TaskViewModel taskViewModel = new ViewModelProvider(MainActivity.owner).get(TaskViewModel.class);
        taskViewModel.updateTasksWithSteps(tasks);
        Log.d(TAG, "updating tasks finished");
    }
}