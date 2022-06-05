package com.example.simpletasks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.simpletasks.domain.fileSystem.*;
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
    public static String RESULT_KEY = "VIDEO_CAPTURE_RESULT";

    private final String TAG = "VideoCaptureActivity";
    private final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private VideoCapture videoCapture;
    private ImageButton backButton;
    private VideoView showView;
    private PreviewView previewView;
    private FloatingActionButton fabStartRecording, fabStopRecording, fabPlay, fabPause, fabStop;
    private Button save;
    private FileSystemUtility fsUtils;
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
        cameraProviderFuture.addListener(()->{
            try{
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch(ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
        }, getExecutor());

    }

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

        Collections.addAll(videoControls, fabPlay, fabPause, fabStop);
        Collections.addAll(recordingControls, fabStartRecording, fabStopRecording);

        if(videoFile == null){
            ButtonUtils.disableFABs(videoControls);
            ButtonUtils.disableButton(save);
        }

        fsUtils = new FileSystemUtilityController();
    }

    private void initializeUi() {
        backButton.setOnClickListener(view -> handleBackClick());

        fabStartRecording.setOnClickListener(view -> handleStartRecording());
        fabStopRecording.setOnClickListener(view -> handleStopRecording());

        fabPlay.setOnClickListener(view -> startPlaying());
        fabPause.setOnClickListener(view -> pausePlaying());
        fabStop.setOnClickListener(view -> stopPlaying());

        save.setOnClickListener(view -> saveRecording());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handleBackClick(){
        if(videoFile != null && videoFile.exists()) {
            videoFile.delete();
        }

        super.onBackPressed();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handleStartRecording() {
        // Remove previous recording from this session
        if(videoFile != null && videoFile.exists()) {
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

    @SuppressLint("RestrictedApi")
    private void handleStopRecording() {
        fabStartRecording.setVisibility(View.VISIBLE);
        fabStopRecording.setVisibility(View.GONE);

        ButtonUtils.enableFABs(videoControls);
        ButtonUtils.enableButton(save);

        videoCapture.stopRecording();
    }

    private void startPlaying() {
        if(showView.getCurrentPosition() > 0) {
            showView.start();
            return;
        }

        if(videoFile != null && videoFile.exists()) {
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

    private void pausePlaying() {
        if(showView.canPause()){
            showView.pause();

            fabPause.setVisibility(View.GONE);
            fabPlay.setVisibility(View.VISIBLE);

            ButtonUtils.disableFAB(fabPause);
            ButtonUtils.enableFAB(fabPlay);
        }
    }

    private void stopPlaying() {
        if(showView.canPause()){
            showView.pause();
            showView.seekTo(0);
        }
    }

    private void saveRecording() {
        Intent result = new Intent();
        if(videoFile != null && videoFile.exists()){
            result.putExtra(RESULT_KEY, videoFile.getAbsolutePath());
            setResult(RESULT_OK, result);
        } else {
            result.putExtra(RESULT_KEY, "");
            setResult(RESULT_CANCELED, result);
        }

        finish();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 1);
    }

    private Executor getExecutor(){
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider){
        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageCapture imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        videoCapture = new VideoCapture.Builder()
                .setVideoFrameRate(30)
                .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, videoCapture);
    }

    @SuppressLint("RestrictedApi")
    private void recordVideo(){
        if(videoCapture != null){
            try{
                videoFile = fsUtils.createVideoFile(getExternalFilesDir(FileSystemConstants.VIDEO_DIR));
            } catch(IOException e){
                String message = "Could not create video file.";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Log.e(TAG, e.toString());
                return;
            }

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                ButtonUtils.disableFABs(recordingControls);
                String message = "Permission for audio must be granted in order to capture a video.";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                return;
            }

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
