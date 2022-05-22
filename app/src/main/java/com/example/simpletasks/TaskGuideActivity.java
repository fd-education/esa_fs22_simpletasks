package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.fragments.TaskGuideFragment;

import java.util.Date;
import java.util.List;

/**
 * Activity for the task guide screen.
 */
public class TaskGuideActivity extends AppCompatActivity {
    private static final String TAG = "TaskGuideActivity";
    private TaskWithSteps taskWithSteps;
    private Task task;
    private List<TaskStep> taskSteps;
    private int currentStep;

    /**
     * Set and adjust the view and set its fragment.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_guide);

        // Remove the action bar at the top of the screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setInstanceVariables();

        setFragment();
        Log.d(TAG, "finished initialisation");
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
     * Handle click events on the back button
     */
    @Override
    public void onBackPressed() {
        if (currentStep > 0) {
            currentStep--;
            replaceFragment();
            Log.d(TAG, "moved back a step");
        } else if (currentStep == 0) {
            //TODO: user should confirm exit
            Log.d(TAG, "going back to previous screen");
            super.onBackPressed();
        }
    }

    /**
     * Handle click events on the next button
     *
     * @param view the view that triggered the event
     */
    public void onNextClicked(View view) {
        if (currentStep < taskSteps.size() - 1) {
            currentStep++;
            replaceFragment();
            Log.d(TAG, "moved a step forward");
        } else if (currentStep == taskSteps.size() - 1) {
            //TODO dialog which asks if user really wants to finish the task
            //calculate new nextStartDate
            Date nextStartDate = new Date(task.getNextStartDate().getTime() + task.getInterval());
            task.setNextStartDate(nextStartDate);
            //save the changes in the database
            TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
            taskViewModel.updateTask(taskWithSteps);
            Log.d(TAG, "finished steps");
            //go back to the home screen (not local back because this would go to the last step)
            super.onBackPressed();
        }
    }

    /**
     * Handle click events on the problem button
     *
     * @param view the view that triggered the event
     */
    public void onProblemClicked(View view) {
        //TODO to implement a dialog
    }

    // Sets the instance variables
    private void setInstanceVariables() {
        taskWithSteps = getTask();
        task = taskWithSteps.getTask();
        taskSteps = taskWithSteps.getSteps();
        setTaskTitleOnUi();

        currentStep = 0;

        // if task has no steps, go back to the last activity and throw an error
        if(taskSteps.size() == 0) {
            super.onBackPressed();
            //todo implement error message
        }
    }

    // Get the task from the intent
    private TaskWithSteps getTask() {
        return (TaskWithSteps) getIntent().getExtras().getSerializable(MainActivity.TASK_INTENT_EXTRA);
    }

    // Set the title of the task for all steps
    private void setTaskTitleOnUi() {
        TextView taskTitle = findViewById(R.id.taskTitle_TaskGuide);
        taskTitle.setText(task.getTitle());
    }

    // Add the fragment which displays the step details
    private void setFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerTaskStep_taskGuide, getTaskGuideFragmentWithArguments()).commit();
    }

    // Replace the fragment which displays the step details
    private void replaceFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerTaskStep_taskGuide, getTaskGuideFragmentWithArguments()).commit();
    }

    // Get the fragment with details
    @NonNull
    private TaskGuideFragment getTaskGuideFragmentWithArguments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.CURRENT_TASK_STEP_INTENT_EXTRA, taskSteps.get(currentStep));
        TaskGuideFragment fragment = new TaskGuideFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}