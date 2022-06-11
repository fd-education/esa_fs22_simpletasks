package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;

import java.util.Locale;

public class ReportProblemActivity extends AppCompatActivity {
    public static final String TASK_INTENT_EXTRA = "task_intent_extra";
    public static final String CURRENT_TASK_STEP_INDEX = "current_task_step_index";


    /**
     * initialises the activity
     *
     * @param savedInstanceState the previous instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove the action bar at the top of the screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
    }


    /**
     * Lets the user send a problem description with share sheet
     *
     * @param view the button that was pressed on
     */
    public void onSendProblemClicked(View view) {

        final EditText problemEditText = findViewById(R.id.problemStatementEditText);
        String problemText = problemEditText.getText().toString();

        final View problemTextErrorView = findViewById(R.id.problemStatementError);
        if (problemText.length() == 0) {
            problemTextErrorView.setVisibility(View.VISIBLE);
            return;
        } else {
            problemTextErrorView.setVisibility(View.GONE);
        }

        if (getIntent().hasExtra(TASK_INTENT_EXTRA) && getIntent().hasExtra(CURRENT_TASK_STEP_INDEX)) {
            final TaskWithSteps taskWithSteps = (TaskWithSteps) getIntent().getSerializableExtra(TASK_INTENT_EXTRA);
            final int currentStepIndex = getIntent().getIntExtra(CURRENT_TASK_STEP_INDEX, 0);
            final TaskStep currentStep = taskWithSteps.getSteps().get(currentStepIndex);
            final Task task = taskWithSteps.getTask();

            final String additionalInformation = getString(
                    R.string.problem_statement_additional_information,
                    task.getTitle(), currentStep.getTitle(), currentStepIndex + 1);
            problemText = String.format(Locale.getDefault(),
                    "%s%n%n%s", problemText, additionalInformation
            );
        }
        // TODO: Maybe explain with popup
        sendWithShareSheet(problemText);
        finish();
    }

    // Opens the share sheet with the given text
    private void sendWithShareSheet(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    /**
     * Lets the user return to the task guide
     *
     * @param view the button that was pressed on
     */
    public void onBackClicked(View view) {
        super.onBackPressed();
    }
}