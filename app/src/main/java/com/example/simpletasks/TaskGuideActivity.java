package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.fragments.AudioStepFragment;
import com.example.simpletasks.fragments.TextStepFragment;
import com.example.simpletasks.fragments.VideoStepFragment;

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
            //TODO finish task
            Log.d(TAG, "finished steps");
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

        progressBar = findViewById(R.id.ll_task_progress);
        progressScroll = findViewById(R.id.sv_task_progress_container);

        currentStep = 0;
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

    private void setProgressBar(){
        for(int i = 1; i <= taskSteps.size(); i++){
            progressBar.addView(getProgressStep(i));
        }
    }

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