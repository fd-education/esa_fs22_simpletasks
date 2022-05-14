package com.example.simpletasks.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

/**
 * Entity that models a task with its steps.
 */
public class TaskWithSteps implements Serializable {
    // Embed the task entity to get its fields
    @Embedded
    private Task task;

    // define the relation between the tasks and their steps
    @Relation(
            parentColumn = "id",
            entityColumn = "fk_task_id"
    )
    private List<TaskStep> taskSteps;
    
    public TaskWithSteps(Task task, List<TaskStep> taskSteps) {
        this.task = task;
        this.taskSteps = taskSteps;
    }

    /**
     * Get the task to whom the steps belong.
     *
     * @return the task Object
     */
    public Task getTask() {
        return task;
    }

    /**
     * Set the task to whom the steps belong.
     *
     * @param task the task to set
     */
    public void setTask(final Task task) {
        this.task = task;
    }

    /**
     * Get a list of the steps of this task.
     *
     * @return a list of the task steps
     */
    public List<TaskStep> getSteps(){
        return taskSteps;
    }

    /**
     * Set the list of the steps of this task.
     *
     * @param taskSteps the list of the task steps to set
     */
    public void setTaskSteps(final List<TaskStep> taskSteps) {
        this.taskSteps = taskSteps;
    }
}
