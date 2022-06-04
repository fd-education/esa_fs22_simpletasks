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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.types.TaskStepTypes;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.fragments.EditTaskStepsListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for the edit tasks screen.
 */
public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "EditTaskActivity";
    public static final String SHARED_PREF_TASK_ID = "TaskId";
    public static final String SHARED_PREF_STEP_IDS = "STEP_IDS";
    public static final String NEW_STEP = "NEW_STEP";

    private Task currentEditTask;
    private List<TaskStep> currentEditTaskSteps;

    private EditTaskStepsListFragment taskStepsListFragment;

    // UI element to read from/ write to
    private EditText taskTitle;
    private ImageView taskImageView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TaskStepViewModel taskStepViewModel;


    /**
     * Set and adjust the view and set fragments.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Get the task from the intent
        handleTaskIntent();

        taskImageView = findViewById(R.id.imageView_taskImage);
        if (currentEditTask.getTitleImagePath().isEmpty()) {
            taskImageView.setImageResource(R.drawable.image_placeholder);
        } else {
            taskImageView.setImageURI(Uri.parse(currentEditTask.getTitleImagePath()));
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        taskStepViewModel = new ViewModelProvider(this).get(TaskStepViewModel.class);

        editor = sharedPreferences.edit();

        editor.putString(SHARED_PREF_TASK_ID, currentEditTask.getId()).apply();

        // Set the fragments in the activity
        fillValuesOnUi();
        setFragment();

        Log.d(TAG, "finished initialisation");
    }

    public void onAddTaskStepClicked(View view) {
        getChoseFormatDialog().show();
    }

    /**
     * Handle click events on the save button.
     * Save the current task to the database.
     *
     * @param view the view that triggered the event
     */
    public void onSaveTaskClicked(View view) {
        // Fetch data from the ui
        currentEditTask.setTitle(taskTitle.getText().toString());

        // Save the data into the database
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        //todo add in data layer another method for singular task update so i don't have to create a list
        taskViewModel.updateTask(currentEditTask);
        persistTaskStepChanges();

        Log.d(TAG, "updating task finished");

//        editor.remove(SHARED_PREF_STEP_IDS);

        // Go back to the last screen
        onBackPressed();
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
        //todo ask the user if he really wants to discard his changes
//        editor.remove(SHARED_PREF_STEP_IDS);

        super.onBackPressed();
    }

    // Set the fragment that displays the step details
    private void setFragment() {
        taskStepsListFragment = new EditTaskStepsListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerTaskStepList_editTask, taskStepsListFragment).commit();

        Log.d(TAG, "TaskStepList Fragment set.");
    }

    // Set the task title in the UI
    private void fillValuesOnUi() {
        taskTitle = findViewById(R.id.taskTitle_editTask);
        taskTitle.setText(currentEditTask.getTitle());
    }

    // Get the task from the intent
    private void handleTaskIntent() {
        TaskWithSteps taskWithSteps = (TaskWithSteps) getIntent().getExtras().getSerializable(MainActivity.TASK_INTENT_EXTRA);
        this.currentEditTask = taskWithSteps.getTask();
        this.currentEditTaskSteps = taskWithSteps.getSteps();
    }

    private AlertDialog getChoseFormatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Step Types");

        builder.setItems(new CharSequence[]
                {"TEXT STEP", "AUDIO STEP", "VIDEO STEP", "CANCEL"},
                (dialog, which) -> {
                    switch(which){
                        case 0:
                            TaskStep newTextStep = new TaskStep(currentEditTask.getId(), TaskStepTypes.TEXT, currentEditTaskSteps.size() + 1, "", "", "", "", "");

                            Intent textIntent = new Intent(getBaseContext(), EditTextStepActivity.class);
                            textIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, newTextStep);
                            createNewStep.launch(textIntent);

                            break;
                        case 1:
                            TaskStep audioStep = new TaskStep(currentEditTask.getId(), TaskStepTypes.TEXT, currentEditTaskSteps.size() + 1, "", "", "", "", "");

                            Intent audioIntent = new Intent(getBaseContext(), EditAudioStepActivity.class);
                            audioIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, audioStep);
                            createNewStep.launch(audioIntent);

                            break;
                        case 2:
                            TaskStep videoStep = new TaskStep(currentEditTask.getId(), TaskStepTypes.TEXT, currentEditTaskSteps.size() + 1, "", "", "", "", "");

                            Intent videoIntent = new Intent(getBaseContext(), EditVideoStepActivity.class);
                            videoIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, videoStep);
                            createNewStep.launch(videoIntent);

                            break;
                        case 3:
                            Log.d(TAG, "CANCEL");
                            break;
                    }
                });

        return builder.create();
    }

    final ActivityResultLauncher<Intent> createNewStep = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        TaskStep taskStep = (TaskStep) result.getData().getExtras().getSerializable(NEW_STEP);
                        taskStepsListFragment.addTaskStep(taskStep);
                    }
                }
            });

    private void persistTaskStepChanges(){
        Log.e(TAG, "Persisting Changes");

        List<TaskStep> originalStepList = currentEditTaskSteps;
        List<TaskStep> updatedStepList = taskStepsListFragment.getTaskSteps();

        List<TaskStep> updateSteps = new ArrayList<>();
        List<TaskStep> insertSteps;
        List<TaskStep> deleteSteps;

        if(updatedStepList == null || updatedStepList.isEmpty()){
            deleteSteps = originalStepList;

        } else {
            for(TaskStep updStep: updatedStepList){
                for(TaskStep origStep: originalStepList){
                    if(origStep.getId().equals(updStep.getId())){
                        updateSteps.add(updStep);
                        originalStepList.remove(origStep);
                        break;
                    }
                }
            }

            updatedStepList.removeAll(updateSteps);
//            insertSteps = updatedStepList;
//            deleteSteps = originalStepList;
//
//            taskStepViewModel.insertTaskSteps(insertSteps);
            taskStepViewModel.updateTaskSteps(updateSteps);
        }

//        taskStepViewModel.deleteTaskSteps(deleteSteps);
    }

    private List<String> getUpdatedTaskStepList(){
        if(!sharedPreferences.contains(SHARED_PREF_STEP_IDS)){
            Log.e(TAG, "ERROR: SHARED PREFERENCES BROKEN.");
        }

        String listOfSortedTaskStepIdsJson = sharedPreferences.getString(SHARED_PREF_STEP_IDS, "");

        if (!listOfSortedTaskStepIdsJson.isEmpty()) {
            Log.e(TAG, "Returning updated task step list.");
            Gson gson = new Gson();
            return gson.fromJson(listOfSortedTaskStepIdsJson, new TypeToken<List<String>>() {
            }.getType());
        }

        Log.e(TAG, "Updated task step list.");
        return null;
    }
}