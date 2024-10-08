package com.example.simpletasks.data.entities;

import androidx.room.Embedded;
import androidx.room.Ignore;
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
    private final Task task;

    // define the relation between the tasks and their steps
    @Relation(
            parentColumn = "id",
            entityColumn = "fk_task_id"
    )
    private final List<TaskStep> taskSteps;

    /**
     * constructor for a new task with steps object, which has to be filled with data yet
     */
    //ignore the no arg constructor for room
    @Ignore
    public TaskWithSteps() {
        Date today = new Date();
        Date todayInAnHour = new Date(today.getTime() + ONE_HOUR_INTERVAL);
        Date todayInAWeek = new Date(today.getTime() + ONE_WEEK_INTERVAL);
        this.task = new Task("", "", todayInAnHour, ONE_DAY_INTERVAL, 3 * ONE_HOUR_INTERVAL, todayInAWeek);
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
     * Get a list of the steps of this task.
     *
     * @return a list of the task steps
     */
    public List<TaskStep> getSteps() {
        return taskSteps;
    }

}
