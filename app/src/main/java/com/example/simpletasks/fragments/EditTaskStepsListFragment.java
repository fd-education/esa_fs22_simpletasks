package com.example.simpletasks.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.MainActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.adapters.EditTaskStepsListAdapter;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for the task steps list on the edit tasks screen.
 */
public class EditTaskStepsListFragment extends Fragment {
    private static final String TAG = "EditTaskListFragment";
    private View view;
    private List<TaskStep> taskSteps;

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

        setAdapterWithTaskSteps();

        Log.d(TAG, "finished initialisation");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Get the task steps from the database and set the adapter for the view
    private void setAdapterWithTaskSteps() {
        final RecyclerView recyclerView = view.findViewById(R.id.tasks_list);
        EditTaskStepsListAdapter adapter = new EditTaskStepsListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setTaskSteps(getTaskStepsFromArguments());
        Log.d(TAG, "set task steps");
    }

    // Set the task steps from the arguments in the bundle
    private List<TaskStep> getTaskStepsFromArguments() {
        if (getArguments() != null) {
            Log.d(TAG, "successfully loaded task steps from fragment start");
            TaskWithSteps task = (TaskWithSteps) getArguments().getSerializable(MainActivity.TASK_INTENT_EXTRA);
            return task.getSteps();
        }
        return new ArrayList<>();
    }
}