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

public class TaskGuideFragment extends Fragment {
    private static final String TAG = "TaskGuideFragment";
    private View view;
    private Task task;
    private TaskStep taskStep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_guide, container, false);

        setTaskStepFromArguments();
        return view;
    }

    /**
     * sets the task step from the arguments in the given bundle
     */
    private void setTaskStepFromArguments() {
        if (getArguments() != null) {
            taskStep = (TaskStep) getArguments().getSerializable(MainActivity.CURRENT_TASK_STEP_INTENT_EXTRA);
            Log.d(TAG, "successfully loaded task step from fragment start");
            setTaskStepDetailsOnUi();
        }
    }

    /**
     * sets the text views on the fragment
     */
    private void setTaskStepDetailsOnUi() {
        TextView taskStepTitle = view.findViewById(R.id.titleTaskStep_TaskGuide);
        taskStepTitle.setText(taskStep.getTitle());
        //TODO status bar
        ImageView taskImage = view.findViewById(R.id.taskStepImage_TaskGuide);
        taskImage.setImageURI(Uri.parse(taskStep.getImagePath()));
        TextView taskDecription = view.findViewById(R.id.titleTaskStepDescription_TaskGuide);
        taskDecription.setText(taskStep.getDescription());

    }
}