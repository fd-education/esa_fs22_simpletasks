package com.example.simpletasks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;
import com.example.simpletasks.fragments.AudioPlayerFragment;

import java.util.ArrayList;

public class EditAudioStepActivity extends AppCompatActivity {
    final String TAG = "EditAudioStepActivity";

    private TaskStepViewModel taskStepViewModel;

    private TaskStep step;
    private EditText stepTitleInput;
    private ImageView stepImageView;
    private FragmentContainerView audioPlayer;
    private TextView noAudioWarning;

    private ImageButton backButton;
    private ImageButton captureTitleImage;
    private Button recordAudio;
    private Button saveStep;

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
            Log.d(TAG, "Bundle is empty!");
        }
    }

    private void initializeFields(){
        taskStepViewModel = new TaskStepViewModel(this.getApplication());

        stepTitleInput = findViewById(R.id.et_editaudiostep_title);
        stepImageView = findViewById(R.id.iv_editaudiostep_image);
        audioPlayer = findViewById(R.id.frag_editaudiostep_audioplayer);

        backButton = findViewById(R.id.ib_editaudiostep_back_button);
        captureTitleImage = findViewById(R.id.ib_editaudiostep_capture_image);

        noAudioWarning = findViewById(R.id.tv_editaudiostep_no_recording);
        recordAudio = findViewById(R.id.b_editaudiostep_record_audio);
        saveStep = findViewById(R.id.b_editaudiostep_save);
    }

    private void initializeUi(){
        backButton.setOnClickListener(view -> EditAudioStepActivity.super.onBackPressed());

        captureTitleImage.setOnClickListener(view -> setTitleImage());

        recordAudio.setOnClickListener(view -> recordAudio());

        saveStep.setOnClickListener(view -> {
            persistStep();
            setResult();
            finish();
        });
    }

    private void recordAudio(){
        Intent captureAudioIntent = new Intent(this, AudioCaptureActivity.class);
        captureAudio.launch(captureAudioIntent);
    }

    final ActivityResultLauncher<Intent> captureAudio = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Uri uri = (Uri) result.getData().getExtras().get(AudioCaptureActivity.RESULT_KEY);
                        noAudioWarning.setVisibility(View.GONE);
                        audioPlayer.setVisibility(View.VISIBLE);
                        step.setAudioPath(uri.getPath());
                        setAudioPlayer(uri.getPath());
                    }
                }
            });

    private void setAudioPlayer(String audioPath){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_editaudiostep_audioplayer, AudioPlayerFragment.getNewInstance(audioPath)).commit();
    }

    private void setTitleImage(){
        Intent intent = new Intent(this, ImageCaptureActivity.class);
        chooseTitleImage.launch(intent);
    }

    final ActivityResultLauncher<Intent> chooseTitleImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = (Uri) result.getData().getExtras().get(ImageCaptureActivity.RESULT_KEY);
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

    private void setResult(){
        Intent result = new Intent();
        result.putExtra(EditTaskActivity.NEW_STEP, step);
        setResult(RESULT_OK, result);
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
                setAudioPlayer(step.getAudioPath());
            } else {
                audioPlayer.setVisibility(View.GONE);
                noAudioWarning.setVisibility(View.VISIBLE);
            }
        }
    }
}