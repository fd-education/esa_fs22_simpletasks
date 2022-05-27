package com.example.simpletasks;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;
import com.example.simpletasks.domain.fileSystem.FileSystemUtility;
import com.example.simpletasks.domain.fileSystem.FileSystemUtilityController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EditTextStepActivity extends AppCompatActivity {
    private final String TAG = "EditTextStepActivity";

    private TaskStepViewModel taskStepViewModel;
    private TaskStep step;
    private EditText stepTitleInput;
    private EditText stepDescriptionInput;
    private ImageView stepImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_step);

        // Remove the action bar at the top of the screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        taskStepViewModel = new TaskStepViewModel(this.getApplication());

        stepTitleInput = findViewById(R.id.editText_textStepTitle);
        stepDescriptionInput = findViewById(R.id.editText_textStepDescription);
        stepImageView = findViewById(R.id.imageView_textStepImage);

        Bundle bundle = getIntent().getExtras();
        if(!bundle.isEmpty() && bundle.containsKey(MainActivity.TASK_INTENT_EXTRA)){
            step = (TaskStep) bundle.get(MainActivity.TASK_INTENT_EXTRA);

            stepTitleInput.setText(step.getTitle());
            stepDescriptionInput.setText(step.getDescription());

            if(step.getImagePath() != null){
                Uri uri = Uri.fromFile(new File(step.getImagePath()));
                stepImageView.setImageURI(uri);
            }
        }
    }

    public void onSaveClicked(View view){
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

        taskStepViewModel.updateTaskSteps(steps);

        super.onBackPressed();
    }

    public void onTitleImageClicked(View v){
        Intent intent = new Intent(this, ImageCaptureActivity.class);
        intent.putExtra("image_path", step.getImagePath());
        chooseTitleImage.launch(intent);
    }

    ActivityResultLauncher<Intent> chooseTitleImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri uri = result.getData().getData();
                        step.setImagePath(uri.getPath());
                        stepImageView.setImageURI(uri);
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
        super.onBackPressed();
    }

    private boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
    }
}