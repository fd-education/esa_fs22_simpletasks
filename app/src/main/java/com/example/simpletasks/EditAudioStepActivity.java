package com.example.simpletasks;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class EditAudioStepActivity extends AppCompatActivity {
    final String TAG = "EditAudioStepActivity";

    private Context context;
    private FileSystemUtility fileSystemUtility;

    private TaskStepViewModel taskStepViewModel;

    private TaskStep step;
    private EditText stepTitleInput;
    private ImageView stepImageView;
    private LinearLayout audioPlayer;
    private TextView noAudioWarning;

    private ImageButton backButton;
    private ImageButton captureTitleImage;
    private SeekBar audioSeekBar;
    private FloatingActionButton fabPlay;
    private FloatingActionButton fabPause;
    private FloatingActionButton fabStop;
    private Button recordAudio;
    private Button saveStep;

    private Uri audioPath;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_audio_step);

        initializeFields();
        initializeUi();

        Bundle bundle = getIntent().getExtras();
        if(!bundle.isEmpty()){
            handleTaskStepExtras(bundle);
        } else {
            Log.e(TAG, "Bundle is empty!");
        }
    }

    private void initializeFields(){
        fileSystemUtility = new FileSystemUtilityController();
        taskStepViewModel = new TaskStepViewModel(this.getApplication());

        stepTitleInput = findViewById(R.id.et_editaudiostep_title);
        stepImageView = findViewById(R.id.iv_editaudiostep_image);
        audioSeekBar = findViewById(R.id.sb_editaudiostep_progress);

        backButton = findViewById(R.id.ib_editaudiostep_back_button);
        captureTitleImage = findViewById(R.id.ib_editaudiostep_capture_image);
        audioSeekBar = findViewById(R.id.sb_editaudiostep_progress);
        fabPlay = findViewById(R.id.fab_editaudiostep_play);
        fabPause = findViewById(R.id.fab_editaudiostep_pause);
        fabStop = findViewById(R.id.fab_editaudiostep_stop);

        noAudioWarning = findViewById(R.id.tv_editaudiostep_no_recording);
        audioPlayer = findViewById(R.id.ll_editaudiostep_player);
        recordAudio = findViewById(R.id.b_editaudiostep_record_audio);
        saveStep = findViewById(R.id.b_editaudiostep_save);

        context = this;
    }

    private void initializeUi(){
        backButton.setOnClickListener(view -> EditAudioStepActivity.super.onBackPressed());

        captureTitleImage.setOnClickListener(view -> setTitleImage());

        recordAudio.setOnClickListener(view -> recordAudio());

        saveStep.setOnClickListener(view -> {
            persistStep();
            finish();
        });
    }

    private void recordAudio(){
        Intent captureAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

        if(captureAudioIntent.resolveActivity(context.getPackageManager()) != null){
            File audioFile;

            try{
                audioFile = fileSystemUtility.createAudioFile(getExternalFilesDir(FileSystemConstants.AUDIO_DIR));
            } catch (IOException e) {
                String message = "Unable to record audio.";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Log.e(TAG, e.toString());
                return;
            }

            if(audioFile != null){
                Log.d(TAG, "Launching intent to capture audio.");
                audioPath = FileProvider.getUriForFile(context, FileSystemConstants.FILEPROVIDER_AUTHORITY, audioFile);
                captureAudioIntent.putExtra(MediaStore.EXTRA_OUTPUT, audioPath);
                captureAudio.launch(captureAudioIntent);
            }
        } else{
            String message = "Your device does not support audio capture.";
            recordAudio.setEnabled(false);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    final ActivityResultLauncher<Intent> captureAudio = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){

                        noAudioWarning.setVisibility(View.GONE);
                        audioPlayer.setVisibility(View.VISIBLE);
                        step.setAudioPath(audioPath.toString());
                        playAudio(audioPath);
                    }
                }
            });

    private void playAudio(Uri audioPath){
        controlAudio(audioPath);
    }

    private void playAudio(String audioPath){
        controlAudio(Uri.parse(audioPath));
    }

    private void controlAudio(Uri uri){
        fabPlay.setOnClickListener(view -> {
            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(context, uri);

                initializeSeekbar();
            }

            mediaPlayer.start();
        });

        fabPause.setOnClickListener(view -> {
            if(mediaPlayer != null){
                mediaPlayer.pause();
            }
        });

        fabStop.setOnClickListener(view -> {
            if(mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });

        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initializeSeekbar(){
        audioSeekBar.setMax(mediaPlayer.getDuration());

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                } catch(NullPointerException nex){
                    audioSeekBar.setProgress(0);
                }
            }
        }, 0);
    }

    private void setTitleImage(){
        Intent intent = new Intent(this, ImageCaptureActivity.class);
        intent.putExtra("image_path", step.getImagePath());
        chooseTitleImage.launch(intent);
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

    private void persistStep(){
        if(isEmpty(stepTitleInput)){
            stepTitleInput.setError(getString(R.string.empty_step_title));
            return;
        }

        step.setTitle(stepTitleInput.getText().toString().trim());

        ArrayList<TaskStep> steps = new ArrayList<>();
        steps.add(step);

        taskStepViewModel.updateTaskSteps(steps);

        super.onBackPressed();
    }

    private boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
    }

    private void handleTaskStepExtras(Bundle bundle){
        if(!bundle.isEmpty() && bundle.containsKey(MainActivity.TASK_INTENT_EXTRA)){
            step = (TaskStep) bundle.get(MainActivity.TASK_INTENT_EXTRA);

            stepTitleInput.setText(step.getTitle());

            if(step.getImagePath() != null){
                Uri uri = Uri.parse(step.getImagePath());
                stepImageView.setImageURI(uri);
            }

            if(!step.getAudioPath().isEmpty()){
                noAudioWarning.setVisibility(View.GONE);
                audioPlayer.setVisibility(View.VISIBLE);
                playAudio(step.getAudioPath());
            }
        }
    }
}