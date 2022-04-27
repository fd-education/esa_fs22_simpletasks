package com.example.simpletasks.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.dao.TaskStepDao;
import com.example.simpletasks.data.entity.Task;
import com.example.simpletasks.data.entity.TaskStep;

import java.util.List;

public class TaskStepRepository {
    private final TaskStepDao taskStepDao;

    public TaskStepRepository(Application application){
        AppDatabase db = AppDatabase.getAppDb(application);
        taskStepDao = db.taskStepDao();
    }

    public LiveData<List<TaskStep>> getByTaskId(int taskId){
        return taskStepDao.getByTaskId(taskId);
    }

    public void insertTaskSteps(final TaskStep... taskSteps){
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.insertTaskSteps(taskSteps));
    }

    public void updateTaskSteps(final TaskStep... taskSteps){
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.updateTaskSteps(taskSteps));
    }

    public void deleteTaskSteps(final TaskStep... taskSteps){
        AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.deleteTaskSteps(taskSteps));
    }
}
