package com.example.simpletasks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.simpletasks.domain.fileSystem.FileSystemConstants;
import com.example.simpletasks.domain.fileSystem.FileSystemUtils;
import com.example.simpletasks.domain.fileSystem.FileSystemUtilsController;
import com.example.simpletasks.domain.ui.ButtonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class VideoCaptureActivity extends AppCompatActivity {
    public static final String RESULT_KEY = "VIDEO_CAPTURE_RESULT";

    private final String TAG = "VideoCaptureActivity";
    private final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private VideoCapture videoCapture;
    private ImageButton backButton;
    private VideoView showView;
    private PreviewView previewView;
    private ViewPort viewPort;
    private FloatingActionButton fabStartRecording, fabStopRecording, fabPlay, fabPause, fabStop;
    private Button save;
    private FileSystemUtils fsUtils;
    private File videoFile;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private List<FloatingActionButton> videoControls, recordingControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_capture);

        initializeFields();
        initializeUi();
        requestPermissions();

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

    }

    // Initialize the fields of the edit audio step activity
    private void initializeFields() {
        backButton = findViewById(R.id.ib_videocapture_backbutton);
        showView = findViewById(R.id.vv_videocapture_show);
        previewView = findViewById(R.id.vv_videocapture_preview);
        fabStartRecording = findViewById(R.id.fab_videocapture_start_recording);
        fabStopRecording = findViewById(R.id.fab_videocapture_stop_recording);
        fabPlay = findViewById(R.id.fab_videocapture_play);
        fabPause = findViewById(R.id.fab_videocapture_pause);
        fabStop = findViewById(R.id.fab_videocapture_stop);
        save = findViewById(R.id.b_videocapture_save);

        videoControls = new ArrayList<>();
        recordingControls = new ArrayList<>();
        viewPort = previewView.getViewPort();

        Collections.addAll(videoControls, fabPlay, fabPause, fabStop);
        Collections.addAll(recordingControls, fabStartRecording, fabStopRecording);

        if (videoFile == null) {
            ButtonUtils.disableFABs(videoControls);
            ButtonUtils.disableButton(save);
        }

        fsUtils = new FileSystemUtilsController();
    }

    // Initialize the state of the edit audio step activity
    private void initializeUi() {
        backButton.setOnClickListener(view -> handleBackClick());

        fabStartRecording.setOnClickListener(view -> handleStartRecording());
        fabStopRecording.setOnClickListener(view -> handleStopRecording());

        fabPlay.setOnClickListener(view -> startPlaying());
        fabPause.setOnClickListener(view -> pausePlaying());
        fabStop.setOnClickListener(view -> stopPlaying());

        save.setOnClickListener(view -> saveRecording());
    }

    // Start and configure cameraX
    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        // Unbind all previously bound use cases
        cameraProvider.unbindAll();

        // Require the back facing camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Build the preview use case and bind it to the preview view in the layout
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Build the video capture use case with a frame rate of 30 fps
        videoCapture = new VideoCapture.Builder()
                .setVideoFrameRate(30)
                .build();

        // Create use case group to set the viewport for the video
        UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                .addUseCase(videoCapture)
                .addUseCase(preview)
                .setViewPort(viewPort)
                .build();

        // Bind the camera to the lifecycle and set the use cases
        cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup);
    }

    // Handle the back click and remove the file with the recorded video
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handleBackClick() {
        if (videoFile != null && videoFile.exists()) {
            videoFile.delete();
        }

        super.onBackPressed();
    }

    // Handle the start of the recording
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handleStartRecording() {
        // Remove previous recording from this session if exists
        if (videoFile != null && videoFile.exists()) {
            videoFile.delete();
        }

        // Change button visibility
        previewView.setVisibility(View.VISIBLE);
        showView.setVisibility(View.GONE);
        fabStartRecording.setVisibility(View.GONE);
        fabStopRecording.setVisibility(View.VISIBLE);

        // Start recording the video
        recordVideo();
    }

    // Handle the stopping of the recording
    @SuppressLint("RestrictedApi")
    private void handleStopRecording() {
        fabStartRecording.setVisibility(View.VISIBLE);
        fabStopRecording.setVisibility(View.GONE);

        ButtonUtils.enableFABs(videoControls);
        ButtonUtils.enableButton(save);

        videoCapture.stopRecording();
    }

    // Handle the starting of the video recording
    private void startPlaying() {
        if (showView.getCurrentPosition() > 0) {
            showView.start();
            return;
        }

        if (videoFile != null && videoFile.exists()) {
            ButtonUtils.enableFABs(videoControls);
            showView.setVisibility(View.VISIBLE);
            previewView.setVisibility(View.GONE);

            showView.setVideoPath(videoFile.getAbsolutePath());

            showView.start();

            fabPause.setVisibility(View.VISIBLE);
            fabPlay.setVisibility(View.GONE);

            ButtonUtils.disableFAB(fabPlay);
            ButtonUtils.enableFAB(fabPause);

            showView.setOnClickListener(view -> {
                showView.seekTo(0);
                ButtonUtils.disableFAB(fabPause);
                ButtonUtils.enableFAB(fabPlay);
            });
        }
    }

    // Handle the pausing of the video recording
    private void pausePlaying() {
        if (showView.canPause()) {
            showView.pause();

            fabPause.setVisibility(View.GONE);
            fabPlay.setVisibility(View.VISIBLE);

            ButtonUtils.disableFAB(fabPause);
            ButtonUtils.enableFAB(fabPlay);
        }
    }

    // Handle the stopping of the play
    private void stopPlaying() {
        if (showView.canPause()) {
            showView.pause();
            showView.seekTo(0);
        }
    }

    // Save the captured video, release bound resources and finish the activity.
    private void saveRecording() {
        Intent result = new Intent();
        if (videoFile != null && videoFile.exists()) {
            result.putExtra(RESULT_KEY, Uri.fromFile(videoFile));
            setResult(RESULT_OK, result);
        } else {
            result.putExtra(RESULT_KEY, "");
            setResult(RESULT_CANCELED, result);
        }

        finish();
    }

    // Request the required permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 1);
    }

    // Get the executor for this activity
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void recordVideo() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Inform the user about required permissions
            ButtonUtils.disableFABs(recordingControls);
            String message = "Permission for audio and camera must be granted in order to capture a video.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }

        // If video capture was initialized successfully
        if (videoCapture != null) {
            // Create the file to store the video
            try {
                videoFile = fsUtils.createVideoFile(getExternalFilesDir(FileSystemConstants.VIDEO_DIR));
            } catch (IOException e) {
                String message = "Could not create video file.";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Log.e(TAG, e.toString());
                return;
            }

            // Enable the buttons to control the recording
            ButtonUtils.enableFABs(recordingControls);

            // Start the recording and handle callbacks
            videoCapture.startRecording(
                    new VideoCapture.OutputFileOptions.Builder(videoFile).build(),
                    getExecutor(),
                    new VideoCapture.OnVideoSavedCallback() {
                        @Override
                        public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                            Log.d(TAG, "Video successfully saved. Uri: " + outputFileResults.getSavedUri());
                        }

                        @Override
                        public void onError(int videoCaptureError, @NonNull String errorMessage, @Nullable Throwable cause) {
                            String message = "Video could not be saved!";
                            Toast.makeText(VideoCaptureActivity.this, message, Toast.LENGTH_LONG).show();
                            Log.e(TAG, errorMessage);
                        }
                    }
            );
        }
    }
}
