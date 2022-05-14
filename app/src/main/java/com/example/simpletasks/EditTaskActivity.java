package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.fragments.EditTaskStepsListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for the edit tasks screen.
 */
public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "EditTaskActivity";
    private TaskWithSteps currentEditTask;

    // UI element to read from/ write to
    private EditText taskTitle;

    /**
     * Set and adjust the view and set fragments.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Remove the action bar at the top of the screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Get the task from the intent
        currentEditTask = getTask();

        // Set the fragments in the activity
        fillValuesOnUi();
        setFragment();
    }

    /**
     * Handle click events on the save button.
     * Save the current task to the database.
     *
     * @param view the view that triggered the event
     */
    public void onSaveTaskClicked(View view) {
        // Fetch data from the ui
        currentEditTask.getTask().setTitle(taskTitle.getText().toString());

        // Save the data into the database
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        //todo add in data layer another method for singular task update so i don't have to create a list
        List<TaskWithSteps> list = new ArrayList<>();
        list.add(currentEditTask);
        taskViewModel.updateTasks(list);
        Log.d(TAG, "updating task finished");

        // Go back to the last screen
        onBackPressed();
    }

    /**
     * Handle click events on the back button
     *
     * @param view the view that triggered the event
     */
    public void onBackClicked(View view) {
        //todo ask the user if he really wants to discard his changes
        super.onBackPressed();
    }

    // Make a bundle of the task with its steps so that the fragment has the required data
    private EditTaskStepsListFragment getFragmentWithTaskStepList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.TASK_INTENT_EXTRA, currentEditTask);
        EditTaskStepsListFragment fragment = new EditTaskStepsListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    // Set the fragment that displays the step details
    private void setFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerTaskStepList_editTask, getFragmentWithTaskStepList()).commit();
    }

    // Set the task title in the UI
    private void fillValuesOnUi() {
        taskTitle = findViewById(R.id.taskTitle_editTask);
        taskTitle.setText(currentEditTask.getTask().getTitle());
    }

    // Get the task from the intent
    private TaskWithSteps getTask() {
        return (TaskWithSteps) getIntent().getExtras().getSerializable(MainActivity.TASK_INTENT_EXTRA);
    }
}