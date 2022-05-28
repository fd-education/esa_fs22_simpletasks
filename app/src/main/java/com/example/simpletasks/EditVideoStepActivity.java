package com.example.simpletasks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;
import com.example.simpletasks.domain.fileSystem.FileSystemConstants;
import com.example.simpletasks.domain.fileSystem.FileSystemUtility;
import com.example.simpletasks.domain.fileSystem.FileSystemUtilityController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EditVideoStepActivity extends AppCompatActivity {
    final String TAG = "EditVideoStepActivity";

    private Context context;

    private FileSystemUtility fileSystemUtility;

    private TaskStepViewModel taskStepViewModel;
    private TaskStep step;
    private EditText stepTitleInput;
    private VideoView stepVideo;
    private LinearLayout videoPlayer;
    private TextView noVideoWarning;

    private ImageButton backButton;
    private Button recordVideo;
    private Button saveStep;
    private FloatingActionButton fabPlay;
    private FloatingActionButton fabPause;
    private FloatingActionButton fabStop;

    private Uri videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video_step);

        // Remove the action bar at the top of the screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initializeFields();
        initializeUi();

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            handleTaskStepExtras(bundle);
        }
    }

    private void initializeFields() {
        fileSystemUtility = new FileSystemUtilityController();
        taskStepViewModel = new TaskStepViewModel(this.getApplication());

        stepTitleInput = findViewById(R.id.et_editvideostep_step_title);
        stepVideo = findViewById(R.id.vv_editvideostep_step_video);
        videoPlayer = findViewById(R.id.ll_editvideostep_player);

        fabPlay = findViewById(R.id.fab_editvideostep_play);
        fabPause = findViewById(R.id.fab_editvideostep_pause);
        fabPause.setVisibility(View.GONE);
        fabStop = findViewById(R.id.fab_editvideostep_stop);

        noVideoWarning = findViewById(R.id.tv_editvideostep_no_video);
        backButton = findViewById(R.id.ib_editvideostep_back_button);
        recordVideo = findViewById(R.id.b_editvideostep_start_recording);
        saveStep = findViewById(R.id.b_editvideostep_save_step);

        context = this;
    }

    private void initializeUi(){
        backButton.setOnClickListener(view -> {
            //TODO ask the user if he really wants to discard his changes
            EditVideoStepActivity.super.onBackPressed();
        });

        recordVideo.setOnClickListener(view -> captureVideo());

        saveStep.setOnClickListener(view -> {
            persistStep();
            finish();
        });
    }

    private void captureVideo(){
        Intent captureVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File videoFile;

        try {
            videoFile = fileSystemUtility.createVideoFile(getExternalFilesDir(FileSystemConstants.VIDEO_DIR));
        } catch (IOException e) {
            String message = "Unable to capture video.";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Log.e(TAG, e.toString());
            return;
        }

        if (videoFile != null) {
            Log.d(TAG, "Launching intent to capture video.");
            videoPath = FileProvider.getUriForFile(context, FileSystemConstants.FILEPROVIDER_AUTHORITY, videoFile);
            captureVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoPath);
            captureVideo.launch(captureVideoIntent);
        }
    }

    final ActivityResultLauncher<Intent> captureVideo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        step.setVideoPath(videoPath.toString());
                        showVideo(videoPath);
                    }
                }
            });

    private void showVideo(Uri videoPath) {
        noVideoWarning.setVisibility(View.GONE);
        videoPlayer.setVisibility(View.VISIBLE);
        stepVideo.setVideoURI(videoPath);
        stepVideo.seekTo(1);
        controlVideo();
    }

    private void showVideo(String videoPath) {
        noVideoWarning.setVisibility(View.GONE);
        videoPlayer.setVisibility(View.VISIBLE);
        stepVideo.setVideoPath(videoPath);
        stepVideo.seekTo(1);
        controlVideo();
    }

    private void controlVideo() {
        fabPlay.setOnClickListener(view -> {
                    if(step.getVideoPath() != null && !step.getVideoPath().isEmpty()){
                        stepVideo.start();
                        fabPlay.setVisibility(View.GONE);
                        fabPause.setVisibility(View.VISIBLE);
                    }
                }
        );

        fabPause.setOnClickListener(view -> {
            if (stepVideo.canPause()) {
                stepVideo.pause();
                fabPause.setVisibility(View.GONE);
                fabPlay.setVisibility(View.VISIBLE);
            }
        });

        fabStop.setOnClickListener(view -> {
            if (stepVideo.canPause()) {
                stepVideo.pause();
                stepVideo.seekTo(0);
            }
        });

        stepVideo.setOnCompletionListener(mediaPlayer -> {
            stepVideo.seekTo(0);
            fabPause.setVisibility(View.GONE);
            fabPlay.setVisibility(View.VISIBLE);
        });
    }

    private void persistStep(){
        if (isEmpty(stepTitleInput)) {
            stepTitleInput.setError(getString(R.string.empty_step_title));
            return;
        }

        step.setTitle(stepTitleInput.getText().toString().trim());

        ArrayList<TaskStep> steps = new ArrayList<>();
        steps.add(step);

        taskStepViewModel.updateTaskSteps(steps);
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private void handleTaskStepExtras(Bundle bundle) {
        if (bundle.containsKey(MainActivity.TASK_INTENT_EXTRA)) {
            step = (TaskStep) bundle.get(MainActivity.TASK_INTENT_EXTRA);

            stepTitleInput.setText(step.getTitle());

            if (step.getVideoPath() != null && !step.getVideoPath().isEmpty()) {
                Log.e(TAG, "VIDEO EXISTS! " + step.getVideoPath());
                noVideoWarning.setVisibility(View.GONE);
                showVideo(step.getVideoPath());
            } else {
                videoPlayer.setVisibility(View.GONE);
            }
        }
    }
}