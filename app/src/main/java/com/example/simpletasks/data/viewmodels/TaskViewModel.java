package com.example.simpletasks.data.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.repositories.TaskRepository;
import com.example.simpletasks.data.repositories.TaskStepRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository taskRepo;
    private final TaskStepRepository taskStepRepo;

    private LiveData<List<Task>> allTasks;
    private LiveData<List<TaskWithSteps>> allTasksWithSteps;

    private LiveData<List<Task>> tasksToday;
    private LiveData<List<TaskWithSteps>> tasksTodayWithSteps;

    /**
     * Initialize the repository using the application context.
     * Initialize the observable lists for all tasks and all tasks with their steps,
     * as well as todays tasks and todays tasks with their steps.
     *
     * @param application the context
     */
    public TaskViewModel(Application application){
        super(application);
        taskRepo = new TaskRepository(application);
        taskStepRepo = new TaskStepRepository(application);
    }

    /**
     * Fetch all task entities from the tasks table
     *
     * @return LiveData<List<Task>> observable with all task entities
     */
    public LiveData<List<Task>> getAllTasks(){
        if(allTasks == null){
            allTasks = taskRepo.getAllTasks();
        }

        return allTasks;
    }

    /**
     * Fetch all task entities with their steps from the tasks table
     *
     * @return LiveData<List<TaskWithSteps>> observable with all task entities and their steps
     */
    public LiveData<List<TaskWithSteps>> getAllTasksWithSteps(){
        if(allTasksWithSteps == null){
            allTasksWithSteps = taskRepo.getAllTasksWithSteps();
        }

        return allTasksWithSteps;
    }

    /**
     * Fetch all task entities of today.
     *
     * @return LiveData<List<Task>> observable with all task entities of today
     */
    public LiveData<List<Task>> getTodaysTasks(){
        if(tasksToday == null){
            tasksToday = taskRepo.getTasksByDate(Calendar.getInstance().getTime());
        }

        return tasksToday;
    }

    /**
     * Fetch all task entities with their steps of today.
     *
     * @return LiveData<List<Task>> observable with all task entities of today
     */
    public LiveData<List<TaskWithSteps>> getTodaysTasksWithSteps(){
        if(tasksTodayWithSteps == null){
            tasksTodayWithSteps = taskRepo.getTasksByDateWithSteps(Calendar.getInstance().getTime());
        }

        return tasksTodayWithSteps;
    }

    /**
     * Fetch all task step entities of a tasks ordered by their index
     *
     * @param task the task to lookup
     * @return LiveData<List<TaskStep>> observable with all task step entities of a task
     */
    public LiveData<List<TaskStep>>  getStepsOfTask(final Task task){
        return taskStepRepo.getByTaskId(task.getId());
    }

    /**
     * Insert a list of tasks with their steps into the tasks and taskSteps table.
     *
     * @param tasksWithSteps the tasks with their steps to insert
     */
    public void insertTasksWithSteps(final List<TaskWithSteps> tasksWithSteps){
        taskRepo.insertTaskWithSteps(tasksWithSteps);
    }

    public void insertTask(final Task task){
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        taskRepo.insertTasks(tasks);
    }

    /**
     * Update a list of tasks with their steps in the tasks and taskSteps table.
     *
     * @param tasksWithSteps the tasks with their steps to update
     */
    public void updateTasksWithSteps(final List<TaskWithSteps> tasksWithSteps){
        taskRepo.updateTasksWithSteps(tasksWithSteps);
    }

    /**
     * Update a task in the tasks table.
     *
     * @param task the task to update
     */
    public void updateTask(final Task task){
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        taskRepo.updateTasks(tasks);
    }

    /**
     * Delete a task with its steps from the tasks and taskSteps table.
     *
     * @param task the task with its steps to delete
     */
    public void deleteTask(final TaskWithSteps task) {
        List<TaskWithSteps> list = new ArrayList<>();
        list.add(task);
        deleteTasks(list);
    }

    /**
     * Delete a list of tasks with their steps from the tasks and taskSteps table.
     *
     * @param tasks the tasks with their steps to delete
     */
    public void deleteTasks(final List<TaskWithSteps> tasks){
        taskRepo.deleteTasks(tasks);
    }
}
