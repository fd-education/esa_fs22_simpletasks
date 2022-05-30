package com.example.simpletasks.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity that models a task.
 */
@Entity(tableName = "tasks")
public class Task implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    private String title;

    @NonNull
    private String description;

    // The Date of the next occurrence.
    @NonNull
    @ColumnInfo(name = "next_start_date")
    private Date nextStartDate;

    // The interval between to occurrences of the same task.
    @NonNull
    private Long interval;

    // How much before the actual occurrence to notify the user.
    @NonNull
    @ColumnInfo(name = "notification_delta")
    private Long notificationDelta;

    // The Date of the last occurrence.
    @NonNull
    @ColumnInfo(name = "end_date")
    private Date endDate;

    @Ignore
    private List<TaskStep> steps;

    public Task(@NonNull String title, @NonNull String description, @NonNull Date nextStartDate, @NonNull Long interval, @NonNull Long notificationDelta, @NonNull Date endDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.nextStartDate = nextStartDate;
        this.interval = interval;
        this.notificationDelta = notificationDelta;
        this.endDate = endDate;
    }

    /**
     * Get the id of the task.
     *
     * @return the id of the task
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * Set the id of the task.
     *
     * @param id the id to set
     */
    public void setId(@NonNull String id) {
        this.id = id;
    }

    /**
     * Get the title of the task
     *
     * @return the title of the task
     */
    @NonNull
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the task.
     *
     * @param title the title to set
     */
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    /**
     * Get the description of the task.
     *
     * @return the description of the task.
     */
    @NonNull
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the task.
     *
     * @param description the description to set
     */
    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    /**
     * Get the date of the next occurrence.
     *
     * @return the date of the next occurrence
     */
    @NonNull
    public Date getNextStartDate() {
        return nextStartDate;
    }

    /**
     * Set the date of the next occurrence.
     *
     * @param nextStartDate the date to set
     */
    public void setNextStartDate(@NonNull Date nextStartDate) {
        this.nextStartDate = nextStartDate;
    }

    /**
     * Get the interval between two occurrences of this task.
     *
     * @return the interval between two tasks
     */
    @NonNull
    public Long getInterval() {
        return interval;
    }

    /**
     * Set the interval between two occurrences of this task.
     *
     * @param interval the interval to set
     */
    public void setInterval(@NonNull Long interval) {
        this.interval = interval;
    }

    /**
     * Get the interval between the notification and the start of the task.
     *
     * @return the interval of the notification
     */
    @NonNull
    public Long getNotificationDelta() {
        return notificationDelta;
    }

    /**
     * Set the interval between the notification and the start of the task.
     *
     * @param notificationDelta the interval to set
     */
    public void setNotificationDelta(@NonNull Long notificationDelta) {
        this.notificationDelta = notificationDelta;
    }

    /**
     * Get the date of the last occurrence of this tasks.
     *
     * @return the date of the last occurrence
     */
    @NonNull
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Set the date of the last occurrence of this task.
     *
     * @param endDate the date to set
     */
    public void setEndDate(@NonNull Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Get the steps of this task.
     *
     * @return a list of steps of this task
     */
    @Ignore
    public List<TaskStep> getSteps() {
        if (steps == null) {
            return new ArrayList<>();
        }
        return steps;
    }

    /**
     * Set the steps of this task.
     *
     * @param steps the steps to set
     */
    @Ignore
    public void setSteps(List<TaskStep> steps) {
        this.steps = steps;
    }

    /**
     * Compare two tasks for equality.
     *
     * @param o the task to compare with this task.
     * @return true if the tasks are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) && title.equals(task.title) && description.equals(task.description) && nextStartDate.equals(task.nextStartDate) && Objects.equals(interval, task.interval) && notificationDelta.equals(task.notificationDelta) && endDate.equals(task.endDate) && Objects.equals(steps, task.steps);
    }

    /**
     * Return the hashcode for this task.
     *
     * @return a hash consisting of all fields of the task object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, nextStartDate, interval, notificationDelta, endDate, steps);
    }
}
