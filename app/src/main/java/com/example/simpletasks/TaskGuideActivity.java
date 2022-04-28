package com.example.simpletasks;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entity.Task;
import com.example.simpletasks.data.entity.TaskStep;
import com.example.simpletasks.fragments.TaskGuideFragment;

import java.util.List;
import java.util.Objects;

public class TaskGuideActivity extends AppCompatActivity {
    private static final String TAG = "TaskGuideActivity";
    private Task task;
    private List<TaskStep> taskSteps;
    private int currentStep;

    public static final String TASK_INTENT_EXTRA = "task_intent_extra";
    public static final String CURRENT_TASK_STEP_INTENT_EXTRA = "current_task_step_intent_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_guide);
        Objects.requireNonNull(getSupportActionBar()).hide();

        task = getTask();
        taskSteps = task.getSteps();
        setTaskTitleOnUi();

        currentStep = 0;
        setFragment();
    }

    //gets called when the back button is clicked
    public void onBackClicked(View view) {
        if(currentStep > 0) {
            currentStep--;
            replaceFragment();
        } else if(currentStep == 0) {
            onBackPressed();
        }
    }

    public void onNextClicked(View view) {
        if(currentStep < taskSteps.size() - 1) {
            currentStep++;
            replaceFragment();
        } else if(currentStep == taskSteps.size() - 1) {
            //TODO finish task
            onBackPressed();
        }
    }

    public void onProblemClicked(View view) {
        //TODO to implement a dialog
    }

    /**
     * gets the task from the intent
     *
     * @return the fetched task from the intent
     */
    private Task getTask() {
        return (Task) getIntent().getExtras().getSerializable(TASK_INTENT_EXTRA);
    }

    /**
     * sets the title of the task for all steps
     */
    private void setTaskTitleOnUi() {
        TextView taskTitle = findViewById(R.id.taskTitle_TaskGuide);
        taskTitle.setText(task.getTitle());
    }

    /**
     * adds the fragment which displays the step details
     */
    private void setFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerTaskStep, getTaskGuideFragmentWithArguments()).commit();
    }

    /**
     * replaces the fragment which displays the step details
     */
    private void replaceFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerTaskStep, getTaskGuideFragmentWithArguments()).commit();
    }

    /**
     * get the fragment with arguments
     * @return fragment to add/replace the old one with
     */
    @NonNull
    private TaskGuideFragment getTaskGuideFragmentWithArguments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CURRENT_TASK_STEP_INTENT_EXTRA, taskSteps.get(currentStep));
        TaskGuideFragment fragment = new TaskGuideFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}