package com.example.simpletasks;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class TaskGuideActivity extends AppCompatActivity {

    TaskTestToDelete task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_guide);
        Objects.requireNonNull(getSupportActionBar()).hide();

        task = getTask();
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
     * gets the current task from the database
     * @return the current task
     */
    private TaskTestToDelete getTask() {
        //TODO change to get from database
        ArrayList<TaskStepTestToDelete> steps1 = new ArrayList<>();
        steps1.add(new TaskStepTestToDelete(0, "title", "description"));
        steps1.add(new TaskStepTestToDelete(1, "title2", "description2"));
        return new TaskTestToDelete("Task 1", steps1);
    }
}