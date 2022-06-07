package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
            //update the next item to make it undone
            updateProgressBarAtIndex(currentStep + 1);
            //update the current item to make it current
            updateProgressBarAtIndex(currentStep);
            replaceFragment();
            Log.d(TAG, "moved back a step");
        } else if (currentStep == 0) {
            new DialogBuilder()
                .setDescriptionText(R.string.abort_task_text)
                .setContext(this)
                .setTwoButtonLayout(R.string.cancel_popup, R.string.abort_task_button)
                .setAction(() -> {
                    Log.d(TAG, "going back to previous screen");
                    super.onBackPressed();
                }).build().show();
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
            //update the last item to make it finished
            updateProgressBarAtIndex(currentStep - 1);
            //update the current item to make it current
            updateProgressBarAtIndex(currentStep);
            replaceFragment();
            Log.d(TAG, "moved a step forward");
        } else if (currentStep == taskSteps.size() - 1) {
            //asks if user really wants to finish the task
            new DialogBuilder()
                    .setDescriptionText(R.string.finish_task_text)
                    .setContext(this)
                    .setTwoButtonLayout(R.string.cancel_popup, R.string.finish_task_button)
                    .setAction(() -> {
                        //calculate new nextStartDate
                        Date nextStartDate = new Date(task.getNextStartDate().getTime() + task.getInterval());
                        task.setNextStartDate(nextStartDate);
                        //save the changes in the database
                        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
                        taskViewModel.updateTask(taskWithSteps);
                        Log.d(TAG, "finished steps");
                        //go back to the home screen (not local back because this would go to the last step)
                        super.onBackPressed();
                    }).build().show();
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

        fillUiElements();

        currentStep = 0;

        //TODO change to work after merge with feature/edit-steps!!

        // if task has no steps, go back to the last activity and throw an error
        if (taskSteps == null || taskSteps.size() == 0) {
            super.onBackPressed();
            new DialogBuilder().setDescriptionText(R.string.no_steps_set)
                    .setCenterButtonLayout(R.string.accept_info_popup)
                    .setContext(this)
                    .build().show();
        }
    }

    // Get the task from the intent
    private TaskWithSteps getTask() {
        return (TaskWithSteps) getIntent().getExtras().getSerializable(MainActivity.TASK_INTENT_EXTRA);
    }

    // fill the ui with the task details
    private void fillUiElements() {
        //task title
        TextView taskTitle = findViewById(R.id.taskTitle_TaskGuide);
        taskTitle.setText(task.getTitle());

        //custom progress bar
        for (int i = 0; i < taskSteps.size(); i++) {
            //get the text view
            TextView newTextView = getTextView(i);

            //get container
            LinearLayout customProgressBarContainer = findViewById(R.id.progressBarContainer);
            //add new view
            customProgressBarContainer.addView(newTextView);
        }
    }

    // updates the progress bar item at the given index
    private void updateProgressBarAtIndex(int i) {
        //get the text view
        TextView newTextView = getTextView(i);

        //get container
        LinearLayout customProgressBarContainer = findViewById(R.id.progressBarContainer);
        //delete old view
        customProgressBarContainer.removeViewAt(i);
        //add new view
        customProgressBarContainer.addView(newTextView, i);
    }

    @NonNull
    //returns a text view with the correct text and style attribute
    private TextView getTextView(int i) {
        //set the style attribute
        ContextThemeWrapper contextThemeWrapper;

        if (i < currentStep) {
            contextThemeWrapper = new ContextThemeWrapper(this, R.style.Theme_SimpleTasks_CustomProgressBarFinished);
        } else if (i == currentStep) {
            contextThemeWrapper = new ContextThemeWrapper(this, R.style.Theme_SimpleTasks_CustomProgressBarCurrent);
        } else {
            contextThemeWrapper = new ContextThemeWrapper(this, R.style.Theme_SimpleTasks_CustomProgressBarUndone);
        }

        //create the layout parameters object
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.halfElementPadding);
        layoutParams.setMargins(margin, 0, margin, 0);

        //create new text view
        TextView newTextView = new TextView(contextThemeWrapper);
        newTextView.setLayoutParams(layoutParams);
        //set the number
        newTextView.setText(String.valueOf(i + 1));
        return newTextView;
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