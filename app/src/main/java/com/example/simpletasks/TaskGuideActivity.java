package com.example.simpletasks;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entity.Task;
import com.example.simpletasks.fragments.TaskGuideFragment;

import java.util.Objects;

public class TaskGuideActivity extends AppCompatActivity {
    private static final String TAG = "TaskGuideActivity";
    private Task task;
    private int currentStep;

    public static final String TASK_INTENT_EXTRA = "task_intent_extra";
    public static final String CURRENT_TASK_STEP_INTENT_EXTRA = "current_task_step_intent_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_guide);
        Objects.requireNonNull(getSupportActionBar()).hide();

        task = getTask();
        setTaskTitleOnUi();
        currentStep = 0;

        setFragment();
    }

    //gets called when the back button is clicked
    public void onBackClicked(View view) {
        //TODO to implement
    }

    public void onNextClicked(View view) {
        //TODO to implement
    }

    public void onProblemClicked(View view) {
        //TODO to implement
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
     * sets the fragment which displays the step details
     */
    private void setFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CURRENT_TASK_STEP_INTENT_EXTRA, task.getSteps().get(currentStep));
        TaskGuideFragment fragment = new TaskGuideFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerTaskStep, fragment).commit();
    }
}