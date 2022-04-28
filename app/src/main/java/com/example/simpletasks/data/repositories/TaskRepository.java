package com.example.simpletasks.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.dao.TaskDao;
import com.example.simpletasks.data.entity.Task;

import java.util.Date;
import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application){
        AppDatabase db = AppDatabase.getAppDb(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAll();
    }

    public LiveData<List<Task>> getAllTasks(){return allTasks;}

    public LiveData<List<Task>> getTasksByDate(final Date date){
        Date startDate = new Date(date.getYear(), date.getMonth(), date.getDay(), 0, 0, 0);
        Date endDate = new Date(date.getYear(), date.getMonth(), date.getDay() + 1, 0, 0, 0);

        return taskDao.getByDate(startDate, endDate);
    }

    public LiveData<Task> getTaskById(int id) {
        return taskDao.getById(id);
    }

    public void insertTasks(final Task... tasks){
        AppDatabase.databaseWriteExecutor.execute(() -> taskDao.insertTasks(tasks));
    }

    public void updateTasks(final Task... tasks){
        AppDatabase.databaseWriteExecutor.execute(() -> taskDao.updateTasks(tasks));
    }

    public void deleteTasks(final Task... tasks){
        AppDatabase.databaseWriteExecutor.execute(() -> taskDao.deleteTasks(tasks));
    }
}
