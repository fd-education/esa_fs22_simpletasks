package com.example.simpletasks.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.TaskStepDao;
import com.example.simpletasks.data.entities.TaskStep;

import java.util.List;

public class TaskStepRepository {
    private final TaskStepDao taskStepDao;

    public TaskStepRepository(Application application){
        // TODO Uncomment for final submission and delete seed version
        // AppDatabase db = AppDatabase.getAppDb(application);
        AppDatabase db = AppDatabase.getSeededAppDb(application, true, true);
        taskStepDao = db.taskStepDao();
    }

    public LiveData<List<TaskStep>> getByTaskId(String taskId){
        return taskStepDao.getByTaskId(taskId);
    }

    public void insertTaskSteps(final List<TaskStep> taskSteps){
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.insertTaskSteps(taskSteps));
    }

    public void updateTaskSteps(final List<TaskStep> taskSteps){
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.updateTaskSteps(taskSteps));
    }

    public void deleteTaskSteps(final List<TaskStep> taskSteps){
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.deleteTaskSteps(taskSteps));
    }
}
