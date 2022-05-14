package com.example.simpletasks.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.simpletasks.MainActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;

/**
 * Fragment for the task guide screens.
 */
public class TaskGuideFragment extends Fragment {
    private static final String TAG = "TaskGuideFragment";
    private View view;
    private Task task;
    private TaskStep taskStep;

    /**
     * Inflate the fragments layout and set the adapter for the task steps list.
     *
     * @param inflater layout inflater to inflate the views in the fragment
     * @param container parent view of the fragments ui
     * @param savedInstanceState reconstruction of a previous state
     * @return View for the fragments ui
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_guide, container, false);

        setTaskStepFromArguments();
        return view;
    }

    // Set the task steps from the arguments in the bundle
    private void setTaskStepFromArguments() {
        if (getArguments() != null) {
            taskStep = (TaskStep) getArguments().getSerializable(MainActivity.CURRENT_TASK_STEP_INTENT_EXTRA);
            Log.d(TAG, "successfully loaded task step from fragment start");
            setTaskStepDetailsOnUi();
        }
    }

    // Set the text views on the fragment using the data of a step.
    private void setTaskStepDetailsOnUi() {
        TextView taskStepTitle = view.findViewById(R.id.titleTaskStep_TaskGuide);
        taskStepTitle.setText(taskStep.getTitle());
        //TODO status bar
        ImageView taskImage = view.findViewById(R.id.taskStepImage_TaskGuide);
        taskImage.setImageURI(Uri.parse(taskStep.getImagePath()));
        TextView taskDescription = view.findViewById(R.id.titleTaskStepDescription_TaskGuide);
        taskDescription.setText(taskStep.getDescription());

    }
}