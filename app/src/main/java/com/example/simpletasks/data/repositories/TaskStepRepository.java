package com.example.simpletasks.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.TaskStepDao;
import com.example.simpletasks.data.entities.TaskStep;

import java.util.List;

/**
 * Handles all the interactions with the taskStep table through its DAO.
 */
public class TaskStepRepository {
    private final TaskStepDao taskStepDao;

    /**
     * Initialize the repository using a context.
     *
     * @param context the context
     */
    public TaskStepRepository(Context context) {
        // TODO Uncomment for final submission and delete seed version
        AppDatabase db = AppDatabase.getAppDb(context);
        // AppDatabase db = AppDatabase.getSeededAppDb(context, true, true);
        taskStepDao = db.taskStepDao();
    }


    /**
     * Fetch all task step entities of a tasks specified by its id ordered by their index
     *
     * @param taskId the taskId to lookup
     * @return LiveData<List < TaskStep>> observable with all task step entities of a task
     */
    public LiveData<List<TaskStep>> getByTaskId(String taskId) {
        return taskStepDao.getByTaskId(taskId);
    }

    /**
     * Insert a task step into the task step table
     *
     * @param taskStep the taskStep to insert
     */
    public void insertTaskStep(final TaskStep taskStep) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.insertTaskStep(taskStep));
    }

    /**
     * Insert a list of taskSteps into the tasks table.
     *
     * @param taskSteps the taskSteps to insert
     */
    public void insertTaskSteps(final List<TaskStep> taskSteps) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.insertTaskSteps(taskSteps));
    }

    /**
     * Update a list of taskSteps in the tasks table.
     *
     * @param taskSteps the taskSteps to update
     */
    public void updateTaskSteps(final List<TaskStep> taskSteps) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.updateTaskSteps(taskSteps));
    }

    public void deleteTaskStep(final TaskStep taskStep) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.deleteTaskStep(taskStep));
    }

    /**
     * Delete a list of task steps from the taskSteps table.
     *
     * @param taskSteps the tasks to delete
     */
    public void deleteTaskSteps(final List<TaskStep> taskSteps) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.deleteTaskSteps(taskSteps));
    }
}
