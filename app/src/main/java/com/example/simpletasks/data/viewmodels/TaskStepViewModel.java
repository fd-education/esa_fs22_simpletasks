package com.example.simpletasks.data.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.entities.Task;
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
     * @param application the app context
     */
    public TaskStepViewModel(Application application){
        super(application);
        taskStepRepo = new TaskStepRepository(application);
    }

    /**
     * Fetch all task step entities of a task ordered by their index
     *
     * @param task the task to lookup
     * @return LiveData<List<TaskStep>> observable with all task step entities of a task
     */
    public LiveData<List<TaskStep>>  getStepsOfTask(final Task task){
        return taskStepRepo.getByTaskId(task.getId());
    }

    /**
     * Fetch all task step entities of a task specified by its id ordered by their index
     *
     * @param taskId the task id to lookup
     * @return LiveData<List<TaskStep>> observable with all task step entities of a task
     */
    public LiveData<List<TaskStep>>  getStepsOfTaskById(final String taskId){
        return taskStepRepo.getByTaskId(taskId);
    }

    /**
     * Insert a list of taskSteps into the tasks table.
     *
     * @param taskSteps the taskSteps to insert
     */
    public void insertTaskSteps(final List<TaskStep> taskSteps){
        taskStepRepo.insertTaskSteps(taskSteps);
    }

    /**
     * Delete a list of task steps from the taskSteps table.
     *
     * @param taskSteps the tasks to delete
     */
    public void updateTaskSteps(final List<TaskStep> taskSteps){
        taskStepRepo.updateTaskSteps(taskSteps);
    }

    /**
     * Delete a list of task steps from the taskSteps table.
     *
     * @param taskSteps the tasks to delete
     */
    public void deleteTaskSteps(final List<TaskStep> taskSteps){
        taskStepRepo.deleteTaskSteps(taskSteps);
    }
}
