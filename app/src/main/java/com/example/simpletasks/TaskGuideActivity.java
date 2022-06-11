package com.example.simpletasks;

import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.fragments.AudioStepFragment;
import com.example.simpletasks.fragments.TextStepFragment;
import com.example.simpletasks.fragments.VideoStepFragment;

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

        initializeActivity();

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
                        taskViewModel.updateTaskWithSteps(taskWithSteps);
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
        final Intent intent = new Intent(this, ReportProblemActivity.class);
        intent.putExtra(ReportProblemActivity.TASK_INTENT_EXTRA, taskWithSteps);
        intent.putExtra(ReportProblemActivity.CURRENT_TASK_STEP_INDEX, currentStep);
        startActivity(intent);
    }

    // Sets the instance variables
    private void initializeActivity() {
        fetchTask();
        //only if the task with steps object could be set in the fetch task method
        if (taskWithSteps != null) {
            finishInitialisation();
        }
    }

    // Get the task from the intent
    private void fetchTask() {
        taskWithSteps = (TaskWithSteps) getIntent().getExtras().getSerializable(MainActivity.TASK_INTENT_EXTRA);
        if (taskWithSteps == null) {
            String taskId = getIntent().getStringExtra(MainActivity.TASK_ID_INTENT_EXTRA);
            TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
            taskViewModel.getTaskWithStepsById(taskId).observe(this, fetchedTaskWithSteps -> {
                taskWithSteps = fetchedTaskWithSteps;

                finishInitialisation();
            });
        }
    }

    //after the task is set, we can safely run the rest of the initialization
    private void finishInitialisation() {
        task = taskWithSteps.getTask();
        taskSteps = taskWithSteps.getSteps();
        setTaskTitleOnUi();

        // if task has no steps, go back to the last activity
        if (taskSteps == null || taskSteps.size() == 0) {
            new DialogBuilder().setDescriptionText(R.string.no_steps_set)
                    .setCenterButtonLayout(R.string.accept_info_popup)
                    .setContext(this)
                    .setAction(super::onBackPressed)
                    .build().show();
        } else {
            progressBar = findViewById(R.id.ll_task_progress);
            progressScroll = findViewById(R.id.sv_task_progress_container);

            currentStep = 0;

            setProgressBar();
            setFragment();
            Log.d(TAG, "finished initialisation");
        }
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

    private Fragment getFragmentForStep(TaskStep step) {
        switch (step.getTypeAsTaskStepType()) {
            case VIDEO:
                return VideoStepFragment.getNewInstance(step.getTitle(), step.getVideoPath());
            case AUDIO:
                return AudioStepFragment.getNewInstance(step.getTitle(), step.getImagePath(), step.getAudioPath());
            default:
                return TextStepFragment.getNewInstance(step.getTitle(), step.getImagePath(), step.getDescription());
        }
    }

    private void setActiveStep(int currentStep) {
        int stepIndex = currentStep + 1;
        if (stepIndex > 1) {
            // Set previous step to be done
            progressBar.findViewById(stepIndex - 1).setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.progress_done));
        }

        if (stepIndex < taskSteps.size()) {
            progressBar.findViewById(stepIndex + 1).setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.progress_open));
        }

        // Set current step to be active
        TextView active = progressBar.findViewById(stepIndex);
        active.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.progress_active));

        // Scroll automatically after a certain threshold of steps is reached
        int scrollThreshold = 3;
        if (stepIndex > scrollThreshold) {
            int leftMargin = 20;
            int scrollPositionX = active.getLeft() - scrollThreshold * active.getWidth() - leftMargin;
            progressScroll.post(() -> progressScroll.smoothScrollTo(scrollPositionX, 0));
        } else {
            progressScroll.post(() -> progressScroll.smoothScrollTo(0, 0));
        }
    }

    private void setProgressBar() {
        for (int i = 1; i <= taskSteps.size(); i++) {
            progressBar.addView(getProgressStep(i));
        }
    }

    private TextView getProgressStep(int stepNumber) {
        TextView textView;

        if (stepNumber == 1) {
            textView = (TextView) getLayoutInflater().inflate(R.layout.textview_progress_first_step, progressBar, false);
        } else if (stepNumber == taskSteps.size()) {
            textView = (TextView) getLayoutInflater().inflate(R.layout.textview_progress_last_step, progressBar, false);
        } else {
            textView = (TextView) getLayoutInflater().inflate(R.layout.textview_progress_step, progressBar, false);
        }

        textView.setText(String.valueOf(stepNumber));
        textView.setId(stepNumber);

        return textView;
    }
}