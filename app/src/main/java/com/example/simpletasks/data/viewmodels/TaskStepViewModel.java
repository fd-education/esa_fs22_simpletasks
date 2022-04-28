package com.example.simpletasks.data.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.entity.Task;
import com.example.simpletasks.data.entity.TaskStep;
import com.example.simpletasks.data.repositories.TaskStepRepository;

import java.util.List;

public class TaskStepViewModel extends AndroidViewModel {

    private final TaskStepRepository taskStepRepo;

    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> tasksToday;

    public TaskStepViewModel(Application application) {
        super(application);
        taskStepRepo = new TaskStepRepository(application);
    }

    public LiveData<List<TaskStep>> getStepsOfTask(final Task task) {
        return taskStepRepo.getByTaskId(task.getId());
    }

    public void insertTaskSteps(final TaskStep... taskSteps) {
        taskStepRepo.insertTaskSteps(taskSteps);
    }

    public void updateTaskSteps(final TaskStep... taskSteps) {
        taskStepRepo.updateTaskSteps(taskSteps);
    }

    public void deleteTaskSteps(final TaskStep... taskSteps) {
        taskStepRepo.deleteTaskSteps(taskSteps);
    }
}
