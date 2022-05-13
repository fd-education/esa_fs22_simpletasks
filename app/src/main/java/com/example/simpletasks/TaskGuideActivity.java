package com.example.simpletasks;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.fragments.TaskGuideFragment;

import java.util.List;
import java.util.Objects;

public class TaskGuideActivity extends AppCompatActivity {
    private static final String TAG = "TaskGuideActivity";
    private TaskWithSteps taskWithSteps;
    private Task task;
    private List<TaskStep> taskSteps;
    private int currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_guide);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setInstanceVariables();

        setFragment();
    }

    private void setInstanceVariables() {
        taskWithSteps = getTask();
        task = taskWithSteps.getTask();
        taskSteps = taskWithSteps.getSteps();
        setTaskTitleOnUi();


        currentStep = 0;
    }

    /**
     * gets called when the back button is clicked
     *
     * @param view the view from where the button is clicked
     */
    public void onBackClicked(View view) {
        onBackPressed();
    }

    /**
     * overrides the default back pressed method. goes to the previous step or to the main screen
     */
    @Override
    public void onBackPressed() {
        if (currentStep > 0) {
            currentStep--;
            replaceFragment();
        } else if (currentStep == 0) {
            super.onBackPressed();
        }
    }

    /**
     * gets called when the next button is clicked
     *
     * @param view the view from where the button is clicked
     */
    public void onNextClicked(View view) {
        if (currentStep < taskSteps.size() - 1) {
            currentStep++;
            replaceFragment();
        } else if (currentStep == taskSteps.size() - 1) {
            //TODO finish task
            super.onBackPressed();
        }
    }

    /**
     * gets called when the problem button is clicked
     *
     * @param view the view from where the button is clicked
     */
    public void onProblemClicked(View view) {
        //TODO to implement a dialog
    }

    /**
     * gets the task from the intent
     *
     * @return the fetched task from the intent
     */
    private TaskWithSteps getTask() {
        return (TaskWithSteps) getIntent().getExtras().getSerializable(MainActivity.TASK_INTENT_EXTRA);
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
                .add(R.id.fragmentContainerTaskStep_taskGuide, getTaskGuideFragmentWithArguments()).commit();
    }

    /**
     * replaces the fragment which displays the step details
     */
    private void replaceFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerTaskStep_taskGuide, getTaskGuideFragmentWithArguments()).commit();
    }

    /**
     * get the fragment with arguments
     *
     * @return fragment to add/replace the old one with
     */
    @NonNull
    private TaskGuideFragment getTaskGuideFragmentWithArguments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.CURRENT_TASK_STEP_INTENT_EXTRA, taskSteps.get(currentStep));
        TaskGuideFragment fragment = new TaskGuideFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}