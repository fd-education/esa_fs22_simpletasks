package com.example.simpletasks.data.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.repositories.TaskStepRepository;

import java.util.List;

/**
 * Store and manage TaskStep data.
 */
public class TaskStepViewModel extends AndroidViewModel {

    private final TaskStepRepository taskStepRepo;

    /**
     * Initialize the view model and the taskStep repository using the app context.
     *
     * @param application the app context
     */
    public TaskStepViewModel(Application application) {
        super(application);
        taskStepRepo = new TaskStepRepository(application);
    }

    /**
     * Fetch all task step entities of a task specified by its id ordered by their index
     *
     * @param taskId the task id to lookup
     * @return LiveData<List < TaskStep>> observable with all task step entities of a task
     */
    public LiveData<List<TaskStep>> getStepsOfTaskById(final String taskId) {
        return taskStepRepo.getByTaskId(taskId);
    }

    /**
     * Insert a list of taskSteps into the tasks table.
     *
     * @param taskSteps the taskSteps to insert
     */
    public void insertTaskSteps(final List<TaskStep> taskSteps) {
        taskStepRepo.insertTaskSteps(taskSteps);
    }

    /**
     * Delete a taskStep from the taskSteps table.
     *
     * @param taskStep the taskStep to delete
     */
    public void deleteTaskStep(TaskStep taskStep) {
        taskStepRepo.deleteTaskStep(taskStep);
    }

}
