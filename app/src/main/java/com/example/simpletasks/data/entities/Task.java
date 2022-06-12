package com.example.simpletasks.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
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
    private String titleImagePath;

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
    private final Long notificationDelta;

    // The Date of the last occurrence.
    @NonNull
    @ColumnInfo(name = "end_date")
    private Date endDate;

    public Task(@NonNull String title, @NonNull String titleImagePath, @NonNull Date nextStartDate, @NonNull Long interval, @NonNull Long notificationDelta, @NonNull Date endDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.titleImagePath = titleImagePath;
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
     * Get the title image path of the task
     *
     * @return the title image path of the task
     */
    @NonNull
    public String getTitleImagePath() {
        return titleImagePath;
    }

    /**
     * Set the title image path for the task.
     *
     * @param titleImagePath the title to set
     */
    public void setTitleImagePath(@NonNull String titleImagePath) {
        this.titleImagePath = titleImagePath;
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
        return id.equals(task.id) && title.equals(task.title) && nextStartDate.equals(task.nextStartDate) && Objects.equals(interval, task.interval) && notificationDelta.equals(task.notificationDelta) && endDate.equals(task.endDate);
    }

    /**
     * Return the hashcode for this task.
     *
     * @return a hash consisting of all fields of the task object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, nextStartDate, interval, notificationDelta, endDate);
    }
}
