package com.example.simpletasks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.simpletasks.R;
import com.example.simpletasks.TaskStepTestToDelete;
import com.example.simpletasks.TaskTestToDelete;

public class TaskGuideFragment extends Fragment {

    View view;
    TaskTestToDelete task;
    TaskStepTestToDelete taskStep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_guide, container, false);
        taskStep = getTaskStep();
        setTaskStepDetails();
        return view;
    }

    /**
     * get the current task step from the database
     *
     * @return the task step which was found
     */
    private TaskStepTestToDelete getTaskStep() {
        //TODO change to get the task step from the database
        return new TaskStepTestToDelete(0, "title", "description");
    }

    /**
     * sets the text views on the fragment
     */
    private void setTaskStepDetails() {
        TextView taskTitle = view.findViewById(R.id.titleTask_TaskGuide);
        taskTitle.setText(taskStep.getTitle());
        //TODO status bar
        /*TextView taskStepTitle = view.findViewById(R.id.titleTaskStep_TaskGuide);
        taskStepTitle.setText(task.getTitle());*/

    }
}