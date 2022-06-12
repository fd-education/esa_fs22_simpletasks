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

import com.example.simpletasks.R;
import com.example.simpletasks.domain.adapters.TaskListAdapter;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

/**
 * Fragment for the task list..
 */
public class TasksListFragment extends Fragment {
    private static final String TAG = "TaskListFragment";
    private View view;

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
        view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    /**
     * gets called each time the fragment gets back to focus
     */
    @Override
    public void onResume() {
        super.onResume();
        setAdapterWithTasks();
        Log.d(TAG, "finished initialisation");
    }

    // Get the tasks from the database and set the adapter for the view
    private void setAdapterWithTasks() {
        final RecyclerView recyclerView = view.findViewById(R.id.tasks_list);
        TaskListAdapter adapter = new TaskListAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getTodaysTasksWithSteps().observe(getViewLifecycleOwner(), tasks -> {
            Log.d(TAG, "today's Tasks successfully fetched from db");

            adapter.setTasks(tasks);
        });
    }
}