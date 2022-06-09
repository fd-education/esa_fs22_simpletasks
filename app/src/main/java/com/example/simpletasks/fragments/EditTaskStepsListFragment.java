package com.example.simpletasks.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.EditTaskActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.adapters.EditTaskStepsListAdapter;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;
import com.example.simpletasks.domain.dragdrop.TaskStepsDrag;
import com.example.simpletasks.domain.editSteps.EditStepsUtilityController;

import java.util.Collections;
import java.util.List;

/**
 * Fragment for the task steps list on the edit tasks screen.
 */
public class EditTaskStepsListFragment extends Fragment implements TaskStepsDrag.DragHandleCallback {
    private static final String TAG = "EditTaskListFragment";

    private View view;
    private TaskStepViewModel taskStepViewModel;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<TaskStep> taskSteps;
    private EditTaskStepsListAdapter adapter;
    private ItemTouchHelper touchHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity().getBaseContext());
        editor = sharedPreferences.edit();

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
        setAdapterWithTaskSteps();

        Log.d(TAG, "Finished initialisation.");
        return view;
    }

    @Override
    public void onPause() {
        if (taskSteps != null) {
            String order = EditStepsUtilityController.getStepsListJsonString(taskSteps);
            Log.d(TAG, "OnPause: Putting String order into shared preferences. " + order);
            editor.putString(EditTaskActivity.SHARED_PREF_STEP_IDS, order).commit();
        }

        super.onPause();
    }

    private void setAdapterWithTaskSteps() {
        String taskId = sharedPreferences.getString(EditTaskActivity.SHARED_PREF_TASK_ID, null);

        adapter = new EditTaskStepsListAdapter(getContext(), this);

        final RecyclerView recyclerView = view.findViewById(R.id.tasks_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.Callback callback = new TaskStepsDrag(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.stopScroll();

        taskStepViewModel = new ViewModelProvider(this).get(TaskStepViewModel.class);
        taskStepViewModel.getStepsOfTaskById(taskId).observe(getViewLifecycleOwner(), taskSteps -> {
            Log.d(TAG, "TaskSteps of Task " + taskId + " fetched.");

            String jsonList = sharedPreferences.getString(EditTaskActivity.SHARED_PREF_STEP_IDS, null);
            List<String> order = EditStepsUtilityController.getListOfStepIds(jsonList);

            this.taskSteps = EditStepsUtilityController.getSortedTaskSteps(order, taskSteps);
            updateStepIndeces();

            adapter.setTaskSteps(taskSteps);
            adapter.registerAdapterDataObserver(getDataObserver());
        });
    }

    public List<TaskStep> getTaskSteps() {
        return this.taskSteps;
    }

    private RecyclerView.AdapterDataObserver getDataObserver() {
        return new RecyclerView.AdapterDataObserver() {
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

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    @Override
    public void updateTaskSteps(){
        taskStepViewModel.updateTaskSteps(taskSteps);
    }
}