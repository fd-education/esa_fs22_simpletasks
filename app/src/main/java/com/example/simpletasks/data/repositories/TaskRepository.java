package com.example.simpletasks.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.simpletasks.R;
import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.TaskDao;
import com.example.simpletasks.data.daos.TaskStepDao;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.domain.notifications.NotificationManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles all the interactions with the tasks table through its DAO.
 */
@SuppressWarnings("deprecation")
public class TaskRepository {
    private final TaskDao taskDao;
    private final TaskStepDao taskStepDao;
    private final Context context;

    /**
     * Initialize the repository using a context.
     * Initialize the observable lists for all tasks and all tasks with their steps.
     *
     * @param context the context
     */
    public TaskRepository(Context context) {
        // TODO Uncomment for final submission and delete seed version
        AppDatabase db = AppDatabase.getAppDb(context);
        // AppDatabase db = AppDatabase.getSeededAppDb(context, true, true);
        taskDao = db.taskDao();
        taskStepDao = db.taskStepDao();
        this.context = context;
    }

    /**
     * Fetch all task entities from the tasks table
     *
     * @return LiveData<List<Task>> observable with all task entities
     */
    public LiveData<List<Task>> getAllTasks() {
        return taskDao.getAll();
    }

    /**
     * Fetch all task entities with their steps from the tasks table
     *
     * @return LiveData<List<TaskWithSteps>> observable with all task entities and their steps
     */
    public LiveData<List<TaskWithSteps>> getAllTasksWithSteps() {
        return taskDao.getAllWithSteps();
    }

    /**
     * Fetch all task entities from a given date.
     *
     * @param date the date to look up
     * @return LiveData<List<Task>> observable with all task entities of the specified date
     */
    public LiveData<List<Task>> getTasksByDate(final Date date) {
        Date startDate = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
        Date endDate = new Date(date.getYear(), date.getMonth(), date.getDate() + 1, 0, 0, 0);

        return taskDao.getByDate(startDate, endDate);
    }

    /**
     * Fetch all task entities with their task steps from a given date.
     *
     * @param date the date to look up
     * @return LiveData<List < TaskWithSteps>> observable with all TaskWithSteps entities of the
     * specified date
     */
    public LiveData<List<TaskWithSteps>> getTasksByDateWithSteps(final Date date) {
        Date startDate = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
        Date endDate = new Date(date.getYear(), date.getMonth(), date.getDate() + 1, 0, 0, 0);

        return taskDao.getByDateWithSteps(startDate, endDate);
    }

    /**
     * Insert a list of tasks into the tasks table.
     *
     * @param tasks the tasks to insert
     */
    public void insertTasks(final List<Task> tasks) {
        for (Task task : tasks) {
            scheduleTaskNotification(task);
        }
        AppDatabase.databaseWriteExecutor.execute(() -> taskDao.insertTasks(tasks));
    }

    /**
     * Insert a list of tasks with their steps into the tasks and the taskSteps tables.
     *
     * @param tasksWithSteps the tasks with steps to insert
     */
    public void insertTaskWithSteps(final List<TaskWithSteps> tasksWithSteps) {
        List<Task> taskList = new ArrayList<>();
        List<TaskStep> stepList = new ArrayList<>();

        for (TaskWithSteps task : tasksWithSteps) {
            taskList.add(task.getTask());
            stepList.addAll(task.getSteps());
            scheduleTaskNotification(task.getTask());
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insertTasks(taskList);
            taskStepDao.insertTaskSteps(stepList);
        });
    }

    /**
     * Update a list of tasks in the tasks table
     *
     * @param tasks the list of tasks to update
     */
    public void updateTasks(final List<Task> tasks) {
        for (Task task : tasks) {
            scheduleTaskNotification(task);
        }

        AppDatabase.databaseWriteExecutor.execute(() -> taskDao.updateTasks(tasks));
    }

    /**
     * Delete a list of tasks with their steps from the tasks and the taskSteps tables.
     *
     * @param tasksWithSteps the tasks with steps to delete
     */
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

    //creates a new notification manager in the current context with the correct text
    private NotificationManager getNotificationManager() {
        return new NotificationManager(context, context.getString(R.string.task_notification_title), context.getString(R.string.task_notification_description));
    }

    //schedules a task notification
    private void scheduleTaskNotification(Task task) {
        long nextStartDateLong = task.getNextStartDate().getTime();
        getNotificationManager().scheduleNotification(nextStartDateLong, task.getId());
    }
}
