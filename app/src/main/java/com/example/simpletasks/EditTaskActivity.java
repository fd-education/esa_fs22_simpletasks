package com.example.simpletasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.types.TaskStepTypes;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.fragments.EditTaskStepsListFragment;

/**
 * Activity for the edit tasks screen.
 */
public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "EditTaskActivity";
    public static final String SHARED_PREF_TASK_ID = "TaskId";

    private Task currentEditTask;

    // UI element to read from/ write to
    private EditText taskTitle;
    private ImageView taskImageView;

    /**
     * Set and adjust the view and set fragments.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
    }

    /**
     * gets called each time the activity gets back to focus
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Get the task from the intent
        fetchCurrentEditTask();
    }

    public void onAddTaskStepClicked(View view) {
        getChoseFormatDialog().show();
    }

    /**
     * Handle click events on the save button. Save the current task to the database.
     *
     * @param view the view that triggered the event
     */
    public void onSaveTaskClicked(View view) {
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
            onBackPressed();
        } else {
            //TODO implement error message
            Toast.makeText(this, "Error when validating the data - check again", Toast.LENGTH_SHORT).show();
        }
    }

    public void onTitleImageClicked(View v) {
        Intent intent = new Intent(this, ImageCaptureActivity.class);
        intent.putExtra("image_path", currentEditTask.getTitleImagePath());
        chooseTitleImage.launch(intent);
    }

    final ActivityResultLauncher<Intent> chooseTitleImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        currentEditTask.setTitleImagePath(uri.getPath());
                        taskImageView.setImageURI(uri);
                    }
                }
            });

    /**
     * Handle click events on the back button
     *
     * @param view the view that triggered the event
     */
    public void onBackClicked(View view) {
        onBackPressed();
    }

    /**
     * Handle click events on the back button with a dialog
     */
    @Override
    public void onBackPressed() {
        //todo ask the user if he really wants to discard his changes
        super.onBackPressed();
    }

    /**
     * gets called when the plan task button is clicked
     *
     * @param view the view that triggered the call
     */
    public void onPlanTaskClicked(View view) {
        Intent intent = new Intent(this, ScheduleTaskActivity.class);
        intent.putExtra(MainActivity.TASK_INTENT_EXTRA, currentEditTask);
        startActivity(intent);
    }

    // Set the fragment that displays the step details
    private void setFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerTaskStepList_editTask, new EditTaskStepsListFragment()).commit();

        Log.e(TAG, "FRAGMENT SET");
    }

    // Set the task title in the UI
    private void fillValuesOnUi() {
        taskTitle = findViewById(R.id.taskTitle_editTask);
        taskTitle.setText(currentEditTask.getTitle());
    }

    // Get the task from the intent
    private void fetchCurrentEditTask() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String taskId = sharedPreferences.getString(SHARED_PREF_TASK_ID, "");

        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getTaskById(taskId).observe(this, fetchedTask -> {
            currentEditTask = fetchedTask;
            //initialize the edit task activity
            initializeEditTaskActivity();
        });
    }

    //initializes the edit task activity
    private void initializeEditTaskActivity() {
        taskImageView = findViewById(R.id.imageView_taskImage);

        if (currentEditTask.getTitleImagePath().isEmpty()) {
            taskImageView.setImageResource(R.drawable.image_placeholder);
        } else {
            taskImageView.setImageURI(Uri.parse(currentEditTask.getTitleImagePath()));
        }

        // Set the fragments in the activity
        fillValuesOnUi();
        setFragment();

        Log.d(TAG, "finished initialisation");
    }

    // TODO JAN FRAGEN
    // TODO - position drag
    private AlertDialog getChoseFormatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Step Types");

        builder.setItems(new CharSequence[]
                        {"TEXT STEP", "AUDIO STEP", "VIDEO STEP", "CANCEL"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            TaskStep newTextStep = new TaskStep(currentEditTask.getId(), TaskStepTypes.TEXT, currentEditTask.getSteps().size() + 1, "", "", "", "", "");

                            Intent textIntent = new Intent(getBaseContext(), EditTextStepActivity.class);
                            textIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, newTextStep);
                            startActivity(textIntent);
                            break;
                        case 1:
                            TaskStep audioStep = new TaskStep(currentEditTask.getId(), TaskStepTypes.TEXT, currentEditTask.getSteps().size() + 1, "", "", "", "", "");

                            Intent audioIntent = new Intent(getBaseContext(), EditAudioStepActivity.class);
                            audioIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, audioStep);
                            startActivity(audioIntent);
                            break;
                        case 2:
                            TaskStep videoStep = new TaskStep(currentEditTask.getId(), TaskStepTypes.TEXT, currentEditTask.getSteps().size() + 1, "", "", "", "", "");

                            Intent videoIntent = new Intent(getBaseContext(), EditVideoStepActivity.class);
                            videoIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, videoStep);
                            startActivity(videoIntent);
                            Log.d(TAG, "VIDEO STEP");
                            break;
                        case 3:
                            Log.d(TAG, "CANCEL");
                            break;
                    }
                });

        return builder.create();
    }

    //validates the data of the ui, which was not validated before.
    private boolean validateData() {
        boolean isValid = true;
        if (taskTitle.getText().length() <= 1) {
            isValid = false;
        }
        //more validating could be inserted here
        return isValid;
    }
}