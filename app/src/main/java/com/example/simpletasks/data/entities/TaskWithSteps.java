package com.example.simpletasks.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class TaskWithSteps implements Serializable {
    @Embedded
    private Task task;

    @Relation(
            parentColumn = "id",
            entityColumn = "fk_task_id"
    )
    private List<TaskStep> taskSteps;
    
    public TaskWithSteps(Task task, List<TaskStep> taskSteps) {
        this.task = task;
        this.taskSteps = taskSteps;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<TaskStep> getSteps(){
        return taskSteps;
    }

    public void setTaskSteps(List<TaskStep> taskSteps) {
        this.taskSteps = taskSteps;
    }
}
