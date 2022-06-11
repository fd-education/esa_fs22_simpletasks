package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.fragments.AudioStepFragment;
import com.example.simpletasks.fragments.TextStepFragment;
import com.example.simpletasks.fragments.VideoStepFragment;
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
    private Task task;
    private List<TaskStep> taskSteps;
    private int currentStep;

    private LinearLayout progressBar;
    private HorizontalScrollView progressScroll;

    /**
     * Set and adjust the view and set its fragment.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_guide);

        setInstanceVariables();
        setProgressBar();

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
        TaskWithSteps taskWithSteps = getTask();

        task = taskWithSteps.getTask();
        taskSteps = taskWithSteps.getSteps();
        setTaskTitleOnUi();

        progressBar = findViewById(R.id.ll_task_progress);
        progressScroll = findViewById(R.id.sv_task_progress_container);

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

    // Set the title of the task for all steps
    private void setTaskTitleOnUi() {
        TextView taskTitle = findViewById(R.id.tv_taskstep_tasktitle);
        taskTitle.setText(task.getTitle());
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
        setActiveStep(currentStep);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerTaskStep_taskGuide, getTaskGuideFragmentWithArguments()).commit();
    }

    // Replace the fragment which displays the step details
    private void replaceFragment() {
        setActiveStep(currentStep);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerTaskStep_taskGuide, getTaskGuideFragmentWithArguments()).commit();
    }

    // Get the fragment with details
    @NonNull
    private Fragment getTaskGuideFragmentWithArguments() {
        TaskStep step = taskSteps.get(currentStep);
        return getFragmentForStep(step);
    }

    private Fragment getFragmentForStep(TaskStep step){
        switch(step.getTypeAsTaskStepType()) {
            case VIDEO:
                return VideoStepFragment.getNewInstance(step.getTitle(), step.getVideoPath());
            case AUDIO:
                return AudioStepFragment.getNewInstance(step.getTitle(), step.getImagePath(), step.getAudioPath());
            default:
                return TextStepFragment.getNewInstance(step.getTitle(), step.getImagePath(), step.getDescription());
        }
    }

    // Change the progressbar to be at the current step and handle the scrolling in the progress bar accordingly
    private void setActiveStep(int currentStep){
        int stepIndex = currentStep + 1;
        if(stepIndex > 1){
            // Set previous step to be done
            progressBar.findViewById(stepIndex - 1).setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.progress_done));
        }

        if(stepIndex < taskSteps.size()){
            progressBar.findViewById(stepIndex + 1).setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.progress_open));
        }

        // Set current step to be active
        TextView active = progressBar.findViewById(stepIndex);
        active.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.progress_active));

        // Scroll automatically after a certain threshold of steps is reached
        int scrollThreshold = 3;
        if(stepIndex > scrollThreshold){
            int leftMargin = 20;
            int scrollPositionX = active.getLeft() - scrollThreshold * active.getWidth() - leftMargin;
            progressScroll.post(() -> progressScroll.smoothScrollTo(scrollPositionX, 0));
        } else {
            progressScroll.post(() -> progressScroll.smoothScrollTo(0, 0));
        }
    }

    // Set the progressbar to display as many steps as there are in the task
    private void setProgressBar(){
        for(int i = 1; i <= taskSteps.size(); i++){
            progressBar.addView(getProgressStep(i));
        }
    }

    // Get the progressbar step with the number of a step
    private TextView getProgressStep(int stepNumber){
        TextView textView;

        if(stepNumber == 1){
            textView = (TextView) getLayoutInflater().inflate(R.layout.textview_progress_first_step, progressBar, false);
        } else if(stepNumber == taskSteps.size()){
            textView = (TextView) getLayoutInflater().inflate(R.layout.textview_progress_last_step, progressBar, false);
        } else {
            textView = (TextView) getLayoutInflater().inflate(R.layout.textview_progress_step, progressBar, false);
        }

        textView.setText(String.valueOf(stepNumber));
        textView.setId(stepNumber);

        return textView;
    }
}