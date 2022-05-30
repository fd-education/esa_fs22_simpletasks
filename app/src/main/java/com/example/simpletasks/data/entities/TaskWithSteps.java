package com.example.simpletasks.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity that models a task with its steps.
 */
public class TaskWithSteps implements Serializable {
    public static final long ONE_MINUTE_INTERVAL = 60 * 1000L;
    public static final long ONE_HOUR_INTERVAL = 60 * ONE_MINUTE_INTERVAL;
    public static final long ONE_DAY_INTERVAL = 24 * ONE_HOUR_INTERVAL;
    public static final long ONE_WEEK_INTERVAL = 7 * ONE_DAY_INTERVAL;

    // Embed the task entity to get its fields
    @Embedded
    private Task task;

    // define the relation between the tasks and their steps
    @Relation(
            parentColumn = "id",
            entityColumn = "fk_task_id"
    )
    private List<TaskStep> taskSteps;

    /**
     * constructor for a new task with steps object, which has to be filled with data yet
     */
    public TaskWithSteps() {
        Date today = new Date();
        Date todayInAWeek = new Date(today.getTime() + ONE_WEEK_INTERVAL);
        this.task = new Task("", "", today, ONE_DAY_INTERVAL, 3 * ONE_HOUR_INTERVAL, todayInAWeek);
        this.taskSteps = new ArrayList<>();
    }

    /**
     * constructor for a valid task with steps object
     *
     * @param task      the task object
     * @param taskSteps the list with the task steps
     */
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
    public List<TaskStep> getSteps() {
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
