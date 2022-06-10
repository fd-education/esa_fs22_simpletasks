package com.example.simpletasks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;
import com.example.simpletasks.fragments.VideoPlayerFragment;

import java.util.ArrayList;

public class EditVideoStepActivity extends AppCompatActivity {
    final String TAG = "EditVideoStepActivity";

    private TaskStepViewModel taskStepViewModel;
    private TaskStep step;
    private EditText stepTitleInput;
    private FragmentContainerView videoPlayer;
    private TextView noVideoWarning;

    private ImageButton backButton;
    private Button recordVideo;
    private Button saveStep;
    private ActivityResultLauncher<Intent> captureVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video_step);

        initializeFields();
        initializeUi();

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            handleTaskStepExtras(bundle);
        }
    }

    // Initialize the fields of the edit audio step activity
    private void initializeFields() {
        taskStepViewModel = new TaskStepViewModel(this.getApplication());

        stepTitleInput = findViewById(R.id.et_editvideostep_step_title);
        videoPlayer = findViewById(R.id.frag_editvideostep_videoplayer);

        noVideoWarning = findViewById(R.id.tv_editvideostep_no_video);
        backButton = findViewById(R.id.ib_editvideostep_back_button);
        recordVideo = findViewById(R.id.b_editvideostep_start_recording);
        saveStep = findViewById(R.id.b_editvideostep_save_step);

        // Listener for the result of the video capture activity
        captureVideo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = (Uri) result.getData().getExtras().get(VideoCaptureActivity.RESULT_KEY);
                        step.setVideoPath(uri.getPath());
                        showVideo(uri.getPath());
                    }
                });
    }

    // Initialize the state of the edit audio step activity
    private void initializeUi(){
        backButton.setOnClickListener(view -> {
            //TODO ask the user if he really wants to discard his changes
            super.onBackPressed();
        });

        recordVideo.setOnClickListener(view -> captureVideo());

        saveStep.setOnClickListener(view -> {
            if(!persistStep()){
                return;
            }

            setResult();
            finish();
        });
    }

    // Launch the video capture activity
    private void captureVideo(){
        Intent captureVideoIntent = new Intent(this, VideoCaptureActivity.class);
        captureVideo.launch(captureVideoIntent);
    }

    // Show the video in the player
    private void showVideo(String videoPath) {
        noVideoWarning.setVisibility(View.GONE);
        videoPlayer.setVisibility(View.VISIBLE);
        setVideoPlayer(videoPath);
    }

    // Set the video player fragment
    private void setVideoPlayer(String videoPath){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_editvideostep_videoplayer, VideoPlayerFragment.getNewInstance(videoPath)).commit();
    }

    // Set the activity result
    private void setResult(){
        Intent result = new Intent();
        result.putExtra(EditTaskActivity.NEW_STEP, step);
        setResult(RESULT_OK, result);
    }

    // Persist the changes to the step
    private boolean persistStep(){
        if (isEmpty(stepTitleInput)) {
            stepTitleInput.setError(getString(R.string.empty_step_title));
            return false;
        }

        // No user created steps with no recording allowed
        if(step.getVideoPath() == null || step.getVideoPath().isEmpty()){
            recordVideo.setError(getString(R.string.no_video));
            return false;
        }

        step.setTitle(stepTitleInput.getText().toString().trim());

        ArrayList<TaskStep> steps = new ArrayList<>();
        steps.add(step);
        taskStepViewModel.updateTaskSteps(steps);

        return true;
    }

    // Check if an edit test input is empty
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    // Unpack the taskstep from the extras bundle
    private void handleTaskStepExtras(Bundle bundle) {
        if (bundle.containsKey(MainActivity.TASK_INTENT_EXTRA)) {
            step = (TaskStep) bundle.get(MainActivity.TASK_INTENT_EXTRA);

            stepTitleInput.setText(step.getTitle());

            if (step.getVideoPath() != null && !step.getVideoPath().isEmpty()) {
                noVideoWarning.setVisibility(View.GONE);
                showVideo(step.getVideoPath());
            } else {
                videoPlayer.setVisibility(View.GONE);
            }
        }
    }
}