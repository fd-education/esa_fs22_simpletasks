package com.example.simpletasks.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.EditTaskActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.adapters.EditTaskStepsListAdapter;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;
import com.example.simpletasks.domain.dragdrop.TaskStepsItemTouchHelperCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment for the task steps list on the edit tasks screen.
 */
public class EditTaskStepsListFragment extends Fragment {
    private static final String TAG = "EditTaskListFragment";

    private View view;
    private TaskStepViewModel taskStepViewModel;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<TaskStep> taskSteps;
    private List<TaskStep> updateSteps;
    private List<TaskStep> insertSteps;
    private List<TaskStep> deleteSteps;
    private EditTaskStepsListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity().getBaseContext());

//        sharedPreferences = requireContext().getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//
        if(sharedPreferences.contains(EditTaskActivity.SHARED_PREF_STEP_IDS)){
            editor.remove(EditTaskActivity.SHARED_PREF_STEP_IDS).apply();
        }

        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate the fragments layout and set the adapter for the task steps list.
     *
     * @param inflater           layout inflater to inflate the views in the fragment
     * @param container          parent view of the fragments ui
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
        String taskId = sharedPreferences.getString(EditTaskActivity.SHARED_PREF_TASK_ID, null);

        taskStepViewModel = new ViewModelProvider(this).get(TaskStepViewModel.class);

        updateSteps = new ArrayList<>();
        insertSteps = new ArrayList<>();
        deleteSteps = new ArrayList<>();

        LiveData<List<TaskStep>> taskSteps = taskStepViewModel.getStepsOfTaskById(taskId);

        final Observer<List<TaskStep>> taskStepsObserver = this::setAdapterWithTaskSteps;
        taskSteps.observe(requireActivity(), taskStepsObserver);

        super.onResume();
    }

    @Override
    public void onPause() {
        String order = getStepsListJsonString();
        Log.d(TAG, "OnPause: Putting String order into shared preferences. " + order);
        editor.putString(EditTaskActivity.SHARED_PREF_STEP_IDS, order).commit();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        editor.remove(EditTaskActivity.SHARED_PREF_STEP_IDS).apply();

        super.onDestroy();
    }

    public List<TaskStep> getTaskSteps(){
        return this.taskSteps;
    }

    public void addTaskStep(TaskStep taskStep){
        taskStep.setIndex(taskSteps.size());
        taskSteps.add(taskStep);
        adapter.notifyItemInserted(taskSteps.size() - 1);
    }

    // Get the task steps from the database and set the adapter for the view
    private void setAdapterWithTaskSteps(List<TaskStep> taskSteps) {
        final RecyclerView recyclerView = view.findViewById(R.id.tasks_list);

        adapter = new EditTaskStepsListAdapter(getContext());

        adapter.registerAdapterDataObserver(getDataObserver());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.Callback callback = new TaskStepsItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        if (sharedPreferences.contains(EditTaskActivity.SHARED_PREF_STEP_IDS)) {
            String jsonList = sharedPreferences.getString(EditTaskActivity.SHARED_PREF_STEP_IDS, "");
//            editor.remove(EditTaskActivity.SHARED_PREF_STEP_IDS).apply();
            List<String> order = getListOfStepIds(jsonList);

            if (order != null && !order.isEmpty()) {
                Log.d(TAG, "Restoring order from list.");
//                this.taskSteps = getSortedTaskSteps(order, taskSteps);
                updateStepIndeces();
            }
        }
        if (this.taskSteps == null){
            this.taskSteps = taskSteps;

            String stepsList = getStepsListJsonString();
            Log.e(TAG, "Setting Steps List: " + getStepsListJsonString());
            editor.putString(EditTaskActivity.SHARED_PREF_STEP_IDS, stepsList);
        }

        adapter.setTaskSteps(this.taskSteps);

        Log.d(TAG, "Set task steps.");
    }

    private RecyclerView.AdapterDataObserver getDataObserver() {
        return new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);

                TaskStep taskStep = taskSteps.get(positionStart);

                List<TaskStep> steps = new ArrayList<>();
                steps.add(taskStep);

                taskStepViewModel.deleteTaskSteps(steps);

                super.onItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);

                Log.d(TAG, String.format("Moved item (%s, %s)", fromPosition, toPosition));

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(taskSteps, i, i + 1);
                    }
                    Log.d(TAG, "Inserted moved item and changed items before accordingly.");
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(taskSteps, i, i - 1);
                    }
                    Log.d(TAG, "Inserted moved item and changed items after accordingly.");
                }

                updateStepIndeces();
            }
        };
    }

    private void updateStepIndeces() {
        for (int i = 0; i < taskSteps.size(); i++) {
            taskSteps.get(i).setIndex(i);
        }
    }

    private List<TaskStep> getSortedTaskSteps(List<String> order, List<TaskStep> taskSteps) {
        List<TaskStep> steps = new ArrayList<>();

        for (String id : order) {
            for (TaskStep step : taskSteps) {
                if (step.getId().equals(id)) {
                    steps.add(step);
                    taskSteps.remove(step);
                    break;
                }
            }
        }

        return steps;
    }

    private List<String> getListOfStepIds(String listOfSortedTaskStepIdsJson) {

        if (!listOfSortedTaskStepIdsJson.isEmpty()) {
            Gson gson = new Gson();
            return gson.fromJson(listOfSortedTaskStepIdsJson, new TypeToken<List<String>>() {
            }.getType());
        }

        return null;
    }

    private String getStepsListJsonString() {
        List<String> listOfSortedTaskStepIds = new ArrayList<>();

        for (TaskStep taskStep : taskSteps) {
            listOfSortedTaskStepIds.add(taskStep.getId());
        }

        Gson gson = new Gson();
        return gson.toJson(listOfSortedTaskStepIds);
    }
}