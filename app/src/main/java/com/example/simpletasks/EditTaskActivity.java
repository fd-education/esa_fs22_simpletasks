package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.fragments.EditTaskStepsListFragment;

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
        Log.d(TAG, "finished initialisation");
    }

    /**
     * Handle click events on the save button. Save the current task to the database.
     *
     * @param view the view that triggered the event
     */
    public void onSaveTaskClicked(View view) {
        // Fetch data from the ui
        currentEditTask.getTask().setTitle(taskTitle.getText().toString());

        boolean isValid = validateData();

        if (isValid) {
            // Save the data into the database
            TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
            // using insert rather than update, so new and updated tasks will be saved
            taskViewModel.insertTask(currentEditTask);
            Log.d(TAG, "inserting or updating task finished");
            // Go back to the last screen
            super.onBackPressed();
        } else {
            new DialogBuilder().setDescriptionText(R.string.error_title_empty)
                    .setCenterButtonLayout(R.string.correct)
                    .setContext(this)
                    .build().show();
        }
    }

    /**
     * handle the click event of the add task step button. adds a new task step at the end of the
     * list
     *
     * @param view the view that triggered the event
     */
    public void onAddTaskStepClicked(View view) {
        //TODO implement adding new Task step
        Toast.makeText(this, "on add task step clicked", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle click events on the back button
     *
     * @param view the view that triggered the event
     */
    public void onBackClicked(View view) {
        onBackPressed();
    }

    /**
     * Handle click events on the back button with a dialog
     */
    @Override
    public void onBackPressed() {
        new DialogBuilder()
                .setDescriptionText(R.string.discard_changes_text)
                .setContext(this)
                .setTwoButtonLayout(R.string.cancel_popup, R.string.discard_changes_button)
                .setAction(super::onBackPressed).build().show();
    }

    public void onPlanTaskClicked(View view) {
        Intent intent = new Intent(this, ScheduleTaskActivity.class);
        intent.putExtra(MainActivity.TASK_INTENT_EXTRA, currentEditTask);
        startActivity(intent);
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
        return (TaskWithSteps) getIntent().getSerializableExtra(MainActivity.TASK_INTENT_EXTRA);
    }

    //validates the data of the ui, which was not validated before.
    private boolean validateData() {
        boolean isValid = true;
        if (taskTitle.getText().length() <= 1) {
            isValid = false;
        }
        //more validating could be inserted here
        return isValid;
    }
}