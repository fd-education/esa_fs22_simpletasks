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

    public void insertTasks(final List<TaskWithSteps> tasksWithSteps){
        taskRepo.insertTaskWithSteps(tasksWithSteps);
    }

    public void updateTasks(final List<TaskWithSteps> tasksWithSteps){
        taskRepo.updateTasks(tasksWithSteps);
    }

    public void deleteTask(final TaskWithSteps task) {
        List<TaskWithSteps> list = new ArrayList<>();
        list.add(task);
        deleteTasks(list);
    }

    public void deleteTasks(final List<TaskWithSteps> tasks){
        taskRepo.deleteTasks(tasks);
    }
}
