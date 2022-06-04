package com.example.simpletasks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;

import java.util.ArrayList;

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

    private void initializeFields(){
        taskStepViewModel = new TaskStepViewModel(this.getApplication());

        backButton = findViewById(R.id.ib_edittextstep_back_button);
        stepTitleInput = findViewById(R.id.et_edittextstep_title);
        stepDescriptionInput = findViewById(R.id.et_edittextstep_description);
        stepImageView = findViewById(R.id.iv_edittextstep_image);
        captureImage = findViewById(R.id.ib_edittextstep_capture_image);
        saveStep = findViewById(R.id.b_edittextstep_save);
    }

    private void initializeUi(){
        backButton.setOnClickListener(view -> {
            //todo ask the user if he really wants to discard his changes
            super.onBackPressed();
        });

        captureImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, ImageCaptureActivity.class);
            intent.putExtra("image_path", step.getImagePath());
            chooseTitleImage.launch(intent);
        });

        saveStep.setOnClickListener(view -> {
            persistStep();
            finish();
        });
    }

    private void handleIntent(Bundle bundle){
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

    private void persistStep(){
        if(isEmpty(stepTitleInput)){
            stepTitleInput.setError(getString(R.string.empty_step_title));
            return;
        }

        if(isEmpty(stepDescriptionInput)){
            stepDescriptionInput.setError(getString(R.string.empty_step_description));
            return;
        }

        step.setTitle(stepTitleInput.getText().toString().trim());
        step.setDescription(stepDescriptionInput.getText().toString().trim());

        ArrayList<TaskStep> steps = new ArrayList<>();
        steps.add(step);

        taskStepViewModel.insertTaskSteps(steps);
    }

    final ActivityResultLauncher<Intent> chooseTitleImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        step.setImagePath(uri.getPath());
                        stepImageView.setImageURI(uri);
                    }
                }
            });

    private boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
    }
}