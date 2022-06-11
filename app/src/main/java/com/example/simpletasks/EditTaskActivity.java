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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.types.TaskStepTypes;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.domain.popups.DialogBuilder;
import com.example.simpletasks.fragments.EditTaskStepsListFragment;

/**
 * Activity for the edit tasks screen.
 */
public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "EditTaskActivity";
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


    /**
     * Set and adjust the view and set fragments.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Get the task from the intent
        handleTaskIntent();
        initializeFields();

        if (currentEditTask.getTitleImagePath().isEmpty()) {
            taskImageView.setImageResource(R.drawable.image_placeholder);
        } else {
            taskImageView.setImageURI(Uri.parse(currentEditTask.getTitleImagePath()));
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_TASK_ID, currentEditTask.getId()).apply();

        // Set the fragments in the activity
        fillValuesOnUi();
        setFragment();

        Log.d(TAG, "finished initialisation");
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
    public void onAddTaskStepClicked(View view) {
        // TODO Create the correct pop-up
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
    public void onTitleImageClicked(View v) {
        Intent intent = new Intent(this, ImageCaptureActivity.class);
        chooseTitleImage.launch(intent);
    }

    /**
     * Handle click events on the back button
     *
     * @param view the view that triggered the event
     */
    public void onBackClicked(View view) {
        new DialogBuilder()
                .setDescriptionText(R.string.discard_changes_text)
                .setContext(this)
                .setTwoButtonLayout(R.string.cancel_popup, R.string.discard_changes_button)
                .setAction(this::onBackPressed).build().show();
    }

    @Override
    public void onBackPressed() {
        taskViewModel.deleteTask(currentEditTaskWithSteps);

        super.onBackPressed();
    }

    public void onPlanTaskClicked(View view) {
        Intent intent = new Intent(this, ScheduleTaskActivity.class);
        intent.putExtra(MainActivity.TASK_INTENT_EXTRA, currentEditTaskWithSteps);
        startActivity(intent);
    }

    // Set the fragment that displays the step details
    private void setFragment() {
        EditTaskStepsListFragment taskStepsListFragment = new EditTaskStepsListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerTaskStepList_editTask, taskStepsListFragment).commit();

        Log.d(TAG, "TaskStepList Fragment set.");
    }

    // Set the task title in the UI
    private void fillValuesOnUi() {
        taskTitle.setText(currentEditTask.getTitle());
    }

    // Get the task from the intent
    private void handleTaskIntent() {
        currentEditTaskWithSteps = (TaskWithSteps) getIntent().getSerializableExtra(MainActivity.TASK_INTENT_EXTRA);

        currentEditTask = currentEditTaskWithSteps.getTask();

        if(currentEditTask.getTitle().isEmpty()){
            taskViewModel.insertTask(currentEditTask);
        }
    }

    // TODO Replace with correct dialog pop-up
    private AlertDialog getChoseFormatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        return builder.setTitle("Step Types")
                .setItems(new CharSequence[]
                                {"TEXT STEP", "AUDIO STEP", "VIDEO STEP", "CANCEL"},
                        (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    TaskStep textStep = new TaskStep(currentEditTask.getId(), TaskStepTypes.TEXT, -1, "", "", "", "", "");

                                    Intent textIntent = new Intent(getBaseContext(), EditTextStepActivity.class);
                                    textIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, textStep);
                                    startActivity(textIntent);

                                    break;
                                case 1:
                                    TaskStep audioStep = new TaskStep(currentEditTask.getId(), TaskStepTypes.AUDIO, -1, "", "", "", "", "");

                                    Intent audioIntent = new Intent(getBaseContext(), EditAudioStepActivity.class);
                                    audioIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, audioStep);
                                    startActivity(audioIntent);

                                    break;
                                case 2:
                                    TaskStep videoStep = new TaskStep(currentEditTask.getId(), TaskStepTypes.VIDEO, -1, "", "", "", "", "");

                                    Intent videoIntent = new Intent(getBaseContext(), EditVideoStepActivity.class);
                                    videoIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, videoStep);
                                    startActivity(videoIntent);

                                    break;
                                case 3:
                                    Log.d(TAG, "CANCEL");
                                    break;
                            }
                        })
                .create();
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