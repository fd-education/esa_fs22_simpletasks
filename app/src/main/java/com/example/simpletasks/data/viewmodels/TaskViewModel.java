package com.example.simpletasks.data.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.entity.Task;
import com.example.simpletasks.data.entity.TaskStep;
import com.example.simpletasks.data.repositories.TaskRepository;
import com.example.simpletasks.data.repositories.TaskStepRepository;

import java.util.Calendar;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository taskRepo;
    private final TaskStepRepository taskStepRepo;

    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> tasksToday;

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

    public LiveData<List<Task>> getAllTasks(){
        if(allTasks == null){
            allTasks = taskRepo.getAllTasks();
        }

        return allTasks;
    }

    public LiveData<List<TaskStep>>  getStepsOfTask(final Task task){
        return taskStepRepo.getByTaskId(task.getId());
    }

    public void insertTasks(final Task... tasks){
        taskRepo.insertTasks(tasks);
    }

    public void updateTasks(final Task... tasks){
        taskRepo.updateTasks(tasks);
    }

    public void deleteTasks(final Task... tasks){
        taskRepo.deleteTasks(tasks);
    }
}
