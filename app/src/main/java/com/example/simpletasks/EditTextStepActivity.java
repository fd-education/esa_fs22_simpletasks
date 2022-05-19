package com.example.simpletasks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;

import java.util.ArrayList;

public class EditTextStepActivity extends AppCompatActivity {
    private final String TAG = "EditTextStepActivity";

    private TaskStepViewModel taskStepViewModel;
    private TaskStep step;
    private EditText stepTitleInput;
    private EditText stepDescriptionInput;


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

        Bundle bundle = getIntent().getExtras();
        if(!bundle.isEmpty() && bundle.containsKey(MainActivity.TASK_INTENT_EXTRA)){
            step = (TaskStep) bundle.get(MainActivity.TASK_INTENT_EXTRA);

            stepTitleInput.setText(step.getTitle());
            stepDescriptionInput.setText(step.getDescription());
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