package com.example.simpletasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.domain.popups.ChooseTypeDialog;
import com.example.simpletasks.domain.popups.DialogBuilder;
import com.example.simpletasks.fragments.EditTaskStepsListFragment;

/**
 * Activity for the edit tasks screen.
 */
public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "EditTaskActivity";
    private static final String TASK_TITLE = "TASK_TITLE";
    public static final String SHARED_PREF_TASK_ID = "TaskId";
    public static final String SHARED_PREF_STEP_IDS = "STEP_IDS";
    public static final String NEW_STEP = "NEW_STEP";


    private Task currentEditTask;

    // UI element to read from/ write to
    private EditText taskTitle;
    private ImageView taskImageView;
    private ActivityResultLauncher<Intent> chooseTitleImage;
    private TaskWithSteps currentEditTaskWithSteps;

    private TaskViewModel taskViewModel;
    private SharedPreferences.Editor editor;

    /**
     * Set and adjust the view and set fragments.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        initializeFields();
    }

    /**
     * gets called each time the activity gets back to focus
     */
    @Override
    protected void onResume() {
        super.onResume();

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Get the task from the intent
        handleTaskIntent();

        if (currentEditTask.getTitleImagePath().isEmpty()) {
            taskImageView.setImageResource(R.drawable.image_placeholder);
        } else {
            taskImageView.setImageURI(Uri.parse(currentEditTask.getTitleImagePath()));
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_TASK_ID, currentEditTask.getId()).apply();

        if(sharedPreferences.contains(TASK_TITLE)){
            taskTitle.setText(sharedPreferences.getString(TASK_TITLE, ""));
        }

        // Set the fragments in the activity
        fillValuesOnUi();
        setFragment();

        Log.d(TAG, "finished initialisation");
    }

    @Override
    protected void onPause(){
        if(taskTitle != null && taskTitle.getText().toString().trim().length() > 0){
            editor.putString(TASK_TITLE, taskTitle.getText().toString());
        }

        super.onPause();
    }

    private void initializeFields() {
        taskImageView = findViewById(R.id.imageView_taskImage);
        taskTitle = findViewById(R.id.taskTitle_editTask);

        chooseTitleImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = (Uri) result.getData().getExtras().get(ImageCaptureActivity.RESULT_KEY);
                        currentEditTask.setTitleImagePath(uri.getPath());
                        taskImageView.setImageURI(uri);
                    }
                });
    }

    /**
     * Handle click events on the add task step button
     * Ask the user, which kind of a task they want to create
     *
     * @param view ignored, not needed by the handler
     */
    public void onAddTaskStepClicked(@SuppressWarnings("unused") View view) {
        String stepId = currentEditTaskWithSteps.getTask().getId();
        int index = currentEditTaskWithSteps.getSteps().size() + 1;
        new ChooseTypeDialog(this).showDialog(stepId, index);
    }

    /**
     * Handle click events on the save button. Save the current task to the database.
     *
     * @param view the view that triggered the event
     */
    public void onSaveTaskClicked(@SuppressWarnings("unused") View view) {
        // Fetch data from the ui
        currentEditTask.setTitle(taskTitle.getText().toString());

        boolean isValid = validateData();

        if (isValid) {
            // Save the data into the database
            TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
            // using insert rather than update, so new and updated tasks will be saved
            taskViewModel.updateTask(currentEditTask);
            Log.d(TAG, "inserting or updating task finished");
            // Go back to the last screen
            super.onBackPressed();
        } else {
            new DialogBuilder().setDescriptionText(R.string.error_title_empty)
                    .setCenterButtonLayout(R.string.correct)
                    .setContext(this)
                    .build().show();
        }
    }

    /**
     * Launch the activity to capture the title image
     *
     * @param v ignored because not required for the handler
     */
    public void onTitleImageClicked(@SuppressWarnings("unused") View v) {
        Intent intent = new Intent(this, ImageCaptureActivity.class);
        chooseTitleImage.launch(intent);
    }

    /**
     * Handle click events on the back button
     *
     * @param view the view that triggered the event
     */
    public void onBackClicked(@SuppressWarnings("unused") View view) {
        new DialogBuilder()
                .setContext(this)
                .setDescriptionText(R.string.discard_changes_text)
                .setTwoButtonLayout(R.string.cancel_popup, R.string.discard_changes_button)
                .setAction(this::onBackPressed).build().show();
    }

    @Override
    public void onBackPressed() {
        if(currentEditTaskWithSteps.getTask().getTitle().isEmpty()){
            taskViewModel.deleteTask(currentEditTaskWithSteps);
        }

        super.onBackPressed();
    }

    /**
     * gets called when the plan task button is clicked
     *
     * @param view the view that triggered the call
     */
    public void onPlanTaskClicked(@SuppressWarnings("unused") View view) {
        Intent intent = new Intent(this, ScheduleTaskActivity.class);
        intent.putExtra(MainActivity.TASK_INTENT_EXTRA, currentEditTask);
        startActivity(intent);
    }

    // Set the fragment that displays the step details
    private void setFragment() {
        EditTaskStepsListFragment taskStepsListFragment = new EditTaskStepsListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerTaskStepList_editTask, taskStepsListFragment).commit();

        Log.d(TAG, "TaskStepList Fragment set.");
    }

    // Set the task title in the UI
    private void fillValuesOnUi() {
        taskTitle.setText(currentEditTask.getTitle());
    }

    private void handleTaskIntent() {
        currentEditTaskWithSteps = (TaskWithSteps) getIntent().getSerializableExtra(MainActivity.TASK_INTENT_EXTRA);

        currentEditTask = currentEditTaskWithSteps.getTask();

        if(currentEditTask.getTitle().isEmpty()){
            taskViewModel.insertTask(currentEditTask);
        }
    }

    //validates the data of the ui, which was not validated before.
    private boolean validateData() {
        return taskTitle.getText().length() > 1;
    }
}