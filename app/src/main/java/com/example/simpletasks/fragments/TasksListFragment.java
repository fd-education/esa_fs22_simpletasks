package com.example.simpletasks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.simpletasks.R;
import com.example.simpletasks.TaskStepTestToDelete;
import com.example.simpletasks.TaskTestToDelete;
import com.example.simpletasks.adapters.TaskListAdapter;

import java.util.ArrayList;

public class TasksListFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        fillList();
        return view;
    }

    private void fillList() {
        //TODO change to get the task step from the database
        final ListView list = view.findViewById(R.id.tasks_list);
        ArrayList<TaskTestToDelete> arrayList = new ArrayList<>();
        ArrayList<TaskStepTestToDelete> steps1 = new ArrayList<>();
        steps1.add(new TaskStepTestToDelete(0, "title", "description"));
        steps1.add(new TaskStepTestToDelete(1, "title2", "description2"));
        ArrayList<TaskStepTestToDelete> steps2 = new ArrayList<>();
        arrayList.add(new TaskTestToDelete("Task 1", steps1));
        arrayList.add(new TaskTestToDelete("Task 2", steps2));
        steps2.add(new TaskStepTestToDelete(0, "title", "description"));

        TaskListAdapter adapter = new TaskListAdapter(getContext(), arrayList);
        list.setAdapter(adapter);
    }
}