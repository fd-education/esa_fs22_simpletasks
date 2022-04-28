package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entity.Task;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static List<Task> tasks;

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
        //TODO
    }

    public void onSettingsClicked(View view) {
        //TODO
    }

    public static void setTasks(List<Task> tasks) {
        MainActivity.tasks = tasks;
    }

    /**
     * deletes all tasks which are currently in the activity (fetched from db or created)
     *
     * @param view the view this method gets called from
     */
    public void deleteAllTasks(View view) {
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        for (Task task : tasks) {
            taskViewModel.deleteTasks(task);
        }
        Log.d(TAG, "'deleted all tasks' finished");
    }
}