package com.example.simpletasks;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.simpletasks.domain.ui.AnimationUtils;
import com.example.simpletasks.domain.fileSystem.FileSystemConstants;
import com.example.simpletasks.domain.fileSystem.FileSystemUtils;
import com.example.simpletasks.domain.fileSystem.FileSystemUtilsController;
import com.example.simpletasks.domain.ui.ButtonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioCaptureActivity extends AppCompatActivity {
    public static final String RESULT_KEY = "AUDIO_CAPTURE_RESULT";

    private final String TAG = "AudioCaptureActivity";
    private final String[] REQUIRED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private File audioFile;
    private FileSystemUtils fsUtils;
    private Animation blinkAnimation;
    List<FloatingActionButton> controlFabs;

    private ImageButton backButton;

    private FloatingActionButton fabStartRecording;
    private FloatingActionButton fabStopRecording;

    private FloatingActionButton fabPlay;
    private FloatingActionButton fabStop;
    private FloatingActionButton fabPause;

    private Button save;

    private SeekBar progressBar;
    private ImageView playing;
    private ImageView recording;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_capture);

        initializeFields();
        initializeUi();
        requestPermissions();

        setupMediaRecorder();
    }

    // Initialize the fields of the audio capture activity
    private void initializeFields() {
        fsUtils = new FileSystemUtilsController();
        blinkAnimation = AnimationUtils.getBlinkingAnimation();
        controlFabs = new ArrayList<>();

        backButton = findViewById(R.id.ib_audiocapture_backbutton);

        fabStartRecording = findViewById(R.id.fab_audiocapture_start_recording);
        fabStopRecording = findViewById(R.id.fab_audiocapture_stop_recording);

        fabPlay = findViewById(R.id.fab_audiocapture_play);
        fabPause = findViewById(R.id.fab_audiocapture_pause);
        fabStop = findViewById(R.id.fab_audiocapture_stop);
        Collections.addAll(controlFabs, fabPlay, fabPause, fabStop);

        save = findViewById(R.id.b_audiocapture_save);

        progressBar = findViewById(R.id.sb_audiocapture_progress);
        playing = findViewById(R.id.iv_audiocapture_note);
        recording = findViewById(R.id.iv_audiocapture_mic);
    }

    // Initialize the state of the audio capture activity
    private void initializeUi() {
        ButtonUtils.disableFAB(fabPlay);
        ButtonUtils.disableFAB(fabPause);
        ButtonUtils.disableFAB(fabStop);
        ButtonUtils.disableButton(save);
        progressBar.setEnabled(false);

        backButton.setOnClickListener(view -> handleBackClick());

        fabStartRecording.setOnClickListener(view -> handleStartRecording());
        fabStopRecording.setOnClickListener(view -> handleStopRecording());

        progressBar.setOnSeekBarChangeListener(handleSeekBarChange());

        fabPlay.setOnClickListener(view -> startPlaying());
        fabPause.setOnClickListener(view -> pausePlaying());
        fabStop.setOnClickListener(view -> stopPlaying());

        save.setOnClickListener(view -> saveRecording());
    }

    // Request the required permissions to be given by the user
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 1);
    }

    // Handle the back click and remove the file with the recorded audio
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handleBackClick() {
        if (audioFile != null && audioFile.exists()) {
            audioFile.delete();
        }

        releaseResources();

        super.onBackPressed();
    }

    // Handle the start of the recording
    private void handleStartRecording() {
        // Tell the user which permissions are required for the audio to work and disable fabs to avoid crashing
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ButtonUtils.disableFAB(fabStartRecording);
            ButtonUtils.disableFAB(fabStopRecording);
            String message = "Permission for audio must be granted in order to make a recording.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }

        // Setup the media recorder if not already done
        if (mediaRecorder == null) {
            setupMediaRecorder();
        }

        try {
            // Create the audio file and set it to be the targeted output for the media recorder
            audioFile = fsUtils.createAudioFile(getExternalFilesDir(FileSystemConstants.AUDIO_DIR));
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
        } catch (IOException e) {
            String message = "Could not create audio file.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Log.e(TAG, e.toString());
            return;
        }

        /* If all the preparations are done, make the controls visible and enabled
        and start the recording with a visual cue */
        ButtonUtils.enableFAB(fabStartRecording);
        ButtonUtils.enableFAB(fabStopRecording);
        fabStartRecording.setVisibility(View.GONE);
        fabStopRecording.setVisibility(View.VISIBLE);

        recording.setVisibility(View.VISIBLE);
        recording.startAnimation(blinkAnimation);

        mediaRecorder.start();
    }

    // Handle the stopping of the recording
    private void handleStopRecording() {
        fabStartRecording.setVisibility(View.VISIBLE);
        fabStopRecording.setVisibility(View.GONE);

        mediaRecorder.stop();

        // Enable video controls and save button
        ButtonUtils.enableFABs(controlFabs);
        ButtonUtils.enableButton(save);
        progressBar.setEnabled(true);

        recording.setVisibility(View.GONE);
        recording.clearAnimation();
    }

    // Setup media recorder with audio source, output format and encoder
    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    // Initialize the seekbar
    private SeekBar.OnSeekBarChangeListener handleSeekBarChange() {
        return new SeekBar.OnSeekBarChangeListener() {
            /**
             * Handle changes made to the seekbar
             *
             * @param seekBar the seekbar
             * @param progress the current progress
             * @param fromUser true if the change was made by the user, false otherwise
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Change the progress of the media player if the seekbar is changed
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No action intended
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No action intended
            }
        };
    }

    // Handle the starting of the audio recording
    private void startPlaying() {
        if(audioFile == null){
            // Tell the user that there is no audio source to be played
            String message = "No audio file to play.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Log.e(TAG, message);
            return;
        }

        if(mediaPlayer == null){
            /*
            Instantiate new media player, set its source, the controls and
            explicitly clear any animation of previous playings.
             */
            mediaPlayer = new MediaPlayer();

            try{
                mediaPlayer.setDataSource(audioFile.getAbsolutePath());

                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    fabPlay.setVisibility(View.VISIBLE);
                    fabPause.setVisibility(View.GONE);

                    playing.setVisibility(View.GONE);
                    playing.clearAnimation();

                    mediaPlayer.seekTo(0);
                });

                mediaPlayer.prepare();

            } catch(IOException e){
                String message = "No audio file to play.";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Log.e(TAG, message);
                return;
            }
        }

        // Initialize the seekbar, enable the controls and play the recording with a visual cue.
        initializeSeekbar();

        fabPlay.setVisibility(View.GONE);
        fabPause.setVisibility(View.VISIBLE);

        playing.setVisibility(View.VISIBLE);
        playing.startAnimation(blinkAnimation);

        mediaPlayer.start();
    }

    // Handle the pausing of the audio
    private void pausePlaying() {
        if(mediaPlayer.isPlaying()){
            fabPlay.setVisibility(View.VISIBLE);
            fabPause.setVisibility(View.GONE);

            playing.clearAnimation();

            mediaPlayer.pause();
        }
    }

    // Handle the stopping of the play
    private void stopPlaying() {
        playing.setVisibility(View.GONE);
        playing.clearAnimation();

        mediaPlayer.stop();
    }

    // Initialize the seekbar so that it runs with the playing audio.
    private void initializeSeekbar(){
        progressBar.setMax(mediaPlayer.getDuration());

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                } catch(NullPointerException e){
                    progressBar.setProgress(0);
                }
            }
        }, 0);
    }

    // Save the captured audio, release bound resources and finish the activity.
    private void saveRecording() {
        Intent result = new Intent();

        if (audioFile != null && audioFile.exists()) {
            result.putExtra(RESULT_KEY, Uri.fromFile(audioFile));
            setResult(RESULT_OK, result);
        } else {
            result.putExtra(RESULT_KEY, "");
            setResult(RESULT_CANCELED, result);
        }

        releaseResources();

        finish();
    }

    // Release media recorder and media player if bound.
    private void releaseResources(){
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if(mediaRecorder != null){
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
