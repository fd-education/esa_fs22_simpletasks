package com.example.simpletasks.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.TaskDao;
import com.example.simpletasks.data.daos.TaskStepDao;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;
    private final TaskStepDao taskStepDao;
    private final LiveData<List<Task>> allTasks;
    private final LiveData<List<TaskWithSteps>> allTasksWithSteps;

    public TaskRepository(Application application) {
        // TODO Uncomment for final submission and delete seed version
        // AppDatabase db = AppDatabase.getAppDb(application);
        AppDatabase db = AppDatabase.getSeededAppDb(application, true, true);
        taskDao = db.taskDao();
        taskStepDao = db.taskStepDao();

        allTasks = taskDao.getAll();
        allTasksWithSteps = taskDao.getAllWithSteps();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<TaskWithSteps>> getAllTasksWithSteps() {
        return allTasksWithSteps;
    }

    public LiveData<List<Task>> getTasksByDate(final Date date) {
        Date startDate = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
        Date endDate = new Date(date.getYear(), date.getMonth(), date.getDate() + 1, 0, 0, 0);

        return taskDao.getByDate(startDate, endDate);
    }

    public LiveData<List<TaskWithSteps>> getTasksByDateWithSteps(final Date date) {
        Date startDate = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
        Date endDate = new Date(date.getYear(), date.getMonth(), date.getDate() + 1, 0, 0, 0);

        return taskDao.getByDateWithSteps(startDate, endDate);
    }

    public void insertTasks(final List<Task> tasks) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskDao.insertTasks(tasks));
    }

    public void insertTaskWithSteps(final List<TaskWithSteps> tasksWithSteps) {
        List<Task> taskList = new ArrayList<>();
        List<TaskStep> stepList = new ArrayList<>();

        for (TaskWithSteps task : tasksWithSteps) {
            taskList.add(task.getTask());

            for (TaskStep step : task.getSteps()) {
                stepList.add(step);
            }
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insertTasks(taskList);
            taskStepDao.insertTaskSteps(stepList);
        });
    }

    public void updateTasks(final List<TaskWithSteps> tasksWithSteps) {
        List<Task> tasks = new ArrayList<>();
        for(TaskWithSteps task : tasksWithSteps) {
            tasks.add(task.getTask());
        }

        AppDatabase.databaseWriteExecutor.execute(() -> taskDao.updateTasks(tasks));

        for(TaskWithSteps task : tasksWithSteps) {
            AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.updateTaskSteps(task.getSteps()));
        }
    }

    public void deleteAll(){taskDao.deleteAll();}

    public void deleteTasks(final List<TaskWithSteps> tasksWithSteps) {
        List<Task> tasks = new ArrayList<>();
        for(TaskWithSteps task : tasksWithSteps) {
            tasks.add(task.getTask());
        }

        AppDatabase.databaseWriteExecutor.execute(() -> taskDao.deleteTasks(tasks));

        for(TaskWithSteps task : tasksWithSteps) {
            AppDatabase.databaseWriteExecutor.execute(() -> taskStepDao.deleteTaskSteps(task.getSteps()));
        }
    }
}
