package com.example.simpletasks.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.EditTaskActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.adapters.EditTaskStepsListAdapter;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for the task steps list on the edit tasks screen.
 */
public class EditTaskStepsListFragment extends Fragment {
    private static final String TAG = "EditTaskListFragment";
    private View view;
    private TaskStepViewModel taskStepViewModel;

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

        Log.d(TAG, "finished initialisation");
        return view;
    }

    @Override
    public void onResume() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String taskId = sharedPreferences.getString(EditTaskActivity.SHARED_PREF_TASK_ID, null);

        taskStepViewModel = new ViewModelProvider(this).get(TaskStepViewModel.class);

        LiveData<List<TaskStep>> taskSteps = taskStepViewModel.getStepsOfTaskById(taskId);

        final Observer<List<TaskStep>> taskStepsObserver = taskSteps1 -> setAdapterWithTaskSteps(taskSteps1);

        taskSteps.observe(getActivity(), taskStepsObserver);

        super.onResume();
    }



    // Get the task steps from the database and set the adapter for the view
    private void setAdapterWithTaskSteps(List<TaskStep> taskSteps) {
        final RecyclerView recyclerView = view.findViewById(R.id.tasks_list);
        EditTaskStepsListAdapter adapter = new EditTaskStepsListAdapter(getContext());

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);

                TaskStep taskStep = taskSteps.get(positionStart);

                List<TaskStep> steps = new ArrayList<>();
                steps.add(taskStep);

                taskStepViewModel.deleteTaskSteps(steps);

                super.onItemRangeChanged(positionStart, itemCount);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setTaskSteps(taskSteps);
        Log.d(TAG, "set task steps");
    }
}