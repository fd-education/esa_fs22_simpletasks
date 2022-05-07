package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entities.TaskWithSteps;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //getter in intent extras
    public static final String TASK_INTENT_EXTRA = "task_intent_extra";
    public static final String CURRENT_TASK_STEP_INTENT_EXTRA = "current_task_step_intent_extra";

    private static final String TAG = "MainActivity";
    private static List<TaskWithSteps> tasks;

    /**
     * set the tasks of the task list view
     *
     * @param tasks all tasks to be listed in the fragment
     */
    public static void setTasks(List<TaskWithSteps> tasks) {
        MainActivity.tasks = tasks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void onSkipClicked(View view) {
        //TODO
    }

    public void onLoginClicked(View view) {
        //TODO change intent to login/pin form
        Intent intent = new Intent(this, ManageTaskActivity.class);
        startActivity(intent);
    }

    public void onSettingsClicked(View view) {
        //TODO
    }

}