package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "EditTaskActivity";
    private TaskWithSteps currentEditTask;

    //ui elements to write to/read from
    private EditText taskTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Objects.requireNonNull(getSupportActionBar()).hide();
        currentEditTask = getTask();

        fillValuesOnUi();
    }

    private void fillValuesOnUi() {
        taskTitle = findViewById(R.id.taskTitle_editTask);
        taskTitle.setText(currentEditTask.getTask().getTitle());
    }

    /**
     * gets called when the user clicks on the save button. saves the current edit task into the
     * database
     *
     * @param view the view from where the button was clicked
     */
    public void onSaveTaskClicked(View view) {
        //fetch data from ui
        currentEditTask.getTask().setTitle(taskTitle.getText().toString());

        //save data into database
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        //todo add in data layer another method for singular task update so i dont have to create a list
        List<TaskWithSteps> list = new ArrayList<>();
        list.add(currentEditTask);
        taskViewModel.updateTasks(list);
        Log.d(TAG, "updating task finished");

        //go back to the last screen
        onBackPressed();
    }

    /**
     * gets called when the user clicks on the back icon
     *
     * @param view the view from where the button was clicked
     */
    public void onBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        //todo ask the user if he really wants to discard his changes
        super.onBackPressed();
    }

    /**
     * gets the task from the intent
     *
     * @return the fetched task from the intent
     */
    private TaskWithSteps getTask() {
        return (TaskWithSteps) getIntent().getExtras().getSerializable(MainActivity.TASK_INTENT_EXTRA);
    }
}