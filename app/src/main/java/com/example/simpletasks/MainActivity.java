package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //getter in intent extras
    public static final String TASK_INTENT_EXTRA = "task_intent_extra";
    public static final String CURRENT_TASK_STEP_INTENT_EXTRA = "current_task_step_intent_extra";

    private static final String TAG = "MainActivity";
    private static List<TaskWithSteps> tasks;
    private static ViewModelStoreOwner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        owner = this;
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

    /**
     * set the tasks of the task list view
     *
     * @param tasks all tasks to be listed in the fragment
     */
    public static void setTasks(List<TaskWithSteps> tasks) {
        MainActivity.tasks = tasks;
    }

    /**
     * updates the tasks in the database
     * @param tasks the list with the tasks
     */
    public static void updateTasksInDatabase(List<TaskWithSteps> tasks) {
        TaskViewModel taskViewModel = new ViewModelProvider(owner).get(TaskViewModel.class);
        taskViewModel.updateTasks(tasks);
    }

}