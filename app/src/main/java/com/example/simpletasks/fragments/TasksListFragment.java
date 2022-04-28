package com.example.simpletasks.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.MainActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.adapters.TaskListAdapter;
import com.example.simpletasks.data.entity.Task;
import com.example.simpletasks.data.entity.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class TasksListFragment extends Fragment {
    private static final String TAG = "TaskListFragment";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        setAdapterWithTasks();
        return view;
    }

    /**
     * gets the tasks from the db and sets the adapter for the view
     */
    private void setAdapterWithTasks() {
        final RecyclerView recyclerView = view.findViewById(R.id.tasks_list);
        TaskListAdapter adapter = new TaskListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            Log.d(TAG, "Tasks successfully fetched from db");

            //TODO change this after steps was implemented
            for (Task task : tasks) {
                List<TaskStep> list = new ArrayList<>();
                list.add(new TaskStep("15", "Type", 0, "Titel Step1", "", "beschreibung step1", "", ""));
                list.add(new TaskStep("15", "Type", 1, "Titel Step2", "", "beschreibung step2", "", ""));
                task.setSteps(list);
            }

            adapter.setTasks(tasks);
            MainActivity.setTasks(tasks);
        });
    }
}