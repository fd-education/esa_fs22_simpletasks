package com.example.simpletasks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;

import java.util.ArrayList;

/**
 * Activity to edit text steps.
 */
public class EditTextStepActivity extends AppCompatActivity {
    private final String TAG = "EditTextStepActivity";

    private TaskStepViewModel taskStepViewModel;
    private TaskStep step;

    private ImageButton backButton;
    private EditText stepTitleInput;
    private EditText stepDescriptionInput;
    private ImageView stepImageView;
    private ImageButton captureImage;
    private Button saveStep;
    private ActivityResultLauncher<Intent> chooseTitleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_step);

        initializeFields();
        initializeUi();

        if(getIntent() != null){
            handleIntent(getIntent().getExtras());
        }

        Log.d(TAG, "Created edit text step activity.");
    }

    // Initialize the fields of the edit audio step activity
    private void initializeFields(){
        Log.d(TAG, "Initializing fields");
        taskStepViewModel = new TaskStepViewModel(this.getApplication());

        backButton = findViewById(R.id.ib_edittextstep_back_button);
        stepTitleInput = findViewById(R.id.et_edittextstep_title);
        stepDescriptionInput = findViewById(R.id.et_edittextstep_description);
        stepImageView = findViewById(R.id.iv_edittextstep_image);
        captureImage = findViewById(R.id.ib_edittextstep_capture_image);
        saveStep = findViewById(R.id.b_edittextstep_save);

        // Listener for the result of the capture image activity
        chooseTitleImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = (Uri) result.getData().getExtras().get(ImageCaptureActivity.RESULT_KEY);
                        step.setImagePath(uri.getPath());
                        stepImageView.setImageURI(uri);
                    }
                });
    }

    // Initialize the state of the edit text step activity
    private void initializeUi(){
        Log.d(TAG, "Initializing UI");

        backButton.setOnClickListener(view -> {
            //todo ask the user if he really wants to discard his changes
            super.onBackPressed();
        });

        captureImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, ImageCaptureActivity.class);
            chooseTitleImage.launch(intent);
        });

        saveStep.setOnClickListener(view -> {
            if(!persistStep()){
                return;
            }

            setResult();
            finish();
        });
    }

    // Unpack the taskstep from the extras bundle
    private void handleIntent(Bundle bundle){
        Log.d(TAG, "Unpacking intent bundle");

        if(!bundle.isEmpty() && bundle.containsKey(MainActivity.TASK_INTENT_EXTRA)){
            step = (TaskStep) bundle.get(MainActivity.TASK_INTENT_EXTRA);

            stepTitleInput.setText(step.getTitle());
            stepDescriptionInput.setText(step.getDescription());

            if(step.getImagePath() != null && !step.getVideoPath().isEmpty()){
                Uri uri = Uri.parse(step.getImagePath());
                stepImageView.setImageURI(uri);
            }
        }
    }

    // Persist the changes to the step
    private boolean persistStep(){
        Log.d(TAG, "Persisting step changes");

        if(isEmpty(stepTitleInput)){
            stepTitleInput.setError(getString(R.string.empty_step_title));
            Log.e(TAG, "No title image set.");
            return false;
        }

        if(isEmpty(stepDescriptionInput)){
            stepDescriptionInput.setError(getString(R.string.empty_step_description));
            Log.e(TAG, "No step description set.");
            return false;
        }

        step.setTitle(stepTitleInput.getText().toString().trim());
        step.setDescription(stepDescriptionInput.getText().toString().trim());

        ArrayList<TaskStep> steps = new ArrayList<>();
        steps.add(step);
        taskStepViewModel.insertTaskSteps(steps);

        return true;
    }

    // Set the activity result
    private void setResult(){
        Intent result = new Intent();
        result.putExtra(EditTaskActivity.NEW_STEP, step);
        setResult(RESULT_OK, result);
    }

    // Check if an edit test input is empty
    private boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
    }
}