package com.example.simpletasks.data.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.repositories.TaskRepository;
import com.example.simpletasks.data.repositories.TaskStepRepository;

import java.util.Calendar;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository taskRepo;
    private final TaskStepRepository taskStepRepo;

    private LiveData<List<Task>> allTasks;
    private LiveData<List<TaskWithSteps>> allTasksWithSteps;

    private LiveData<List<Task>> tasksToday;
    private LiveData<List<TaskWithSteps>> tasksTodayWithSteps;

    public TaskViewModel(Application application){
        super(application);
        taskRepo = new TaskRepository(application);
        taskStepRepo = new TaskStepRepository(application);
    }

    public LiveData<List<Task>> getTodaysTasks(){
        if(tasksToday == null){
            tasksToday = taskRepo.getTasksByDate(Calendar.getInstance().getTime());
        }

        return tasksToday;
    }

    public LiveData<List<TaskWithSteps>> getTodaysTasksWithSteps(){
        if(tasksTodayWithSteps == null){
            tasksTodayWithSteps = taskRepo.getTasksByDateWithSteps(Calendar.getInstance().getTime());
        }

        return tasksTodayWithSteps;
    }

    public LiveData<List<Task>> getAllTasks(){
        if(allTasks == null){
            allTasks = taskRepo.getAllTasks();
        }

        return allTasks;
    }

    public LiveData<List<TaskWithSteps>> getAllTasksWithSteps(){
        if(allTasksWithSteps == null){
            allTasksWithSteps = taskRepo.getAllTasksWithSteps();
        }

        return allTasksWithSteps;
    }

    public LiveData<List<TaskStep>>  getStepsOfTask(final Task task){
        return taskStepRepo.getByTaskId(task.getId());
    }

    public void insertTasks(final List<Task> tasks){
        taskRepo.insertTasks(tasks);

        for(Task task: tasks){
            List<TaskStep> taskSteps = task.getSteps();

            taskStepRepo.insertTaskSteps(taskSteps);
        }
    }

    public void updateTasks(final List<Task> tasks){
        taskRepo.updateTasks(tasks);

        for(Task task: tasks){
            List<TaskStep> taskSteps = task.getSteps();

            taskStepRepo.insertTaskSteps(taskSteps);
        }
    }

    public void deleteTasks(final List<Task> tasks){
        taskRepo.deleteTasks(tasks);
    }
}
