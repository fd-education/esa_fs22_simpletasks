package com.example.simpletasks.data.entities;

import static java.util.UUID.randomUUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "tasks")
public class Task {


    @PrimaryKey
    @NonNull
    String id;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    @ColumnInfo(name="next_start_date")
    private Date nextStartDate;

    @NonNull
    private Long interval;

    @NonNull
    @ColumnInfo(name="notification_delta")
    private Long notificationDelta;

    @NonNull
    @ColumnInfo(name="end_date")
    private Date endDate;

    @Ignore
    private List<TaskStep> steps;

    public Task(@NonNull String title, @NonNull String description, @NonNull Date nextStartDate, Long interval, @NonNull Long notificationDelta, @NonNull Date endDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.nextStartDate = nextStartDate;
        this.interval = interval;
        this.notificationDelta = notificationDelta;
        this.endDate = endDate;
    }

    @Ignore
    public Task(@NonNull String title, @NonNull String description, @NonNull Date nextStartDate, Long interval, @NonNull Long notificationDelta, @NonNull Date endDate, List<TaskStep> steps) {
        this.title = title;
        this.description = description;
        this.nextStartDate = nextStartDate;
        this.interval = interval;
        this.notificationDelta = notificationDelta;
        this.endDate = endDate;
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public Date getNextStartDate() {
        return nextStartDate;
    }

    public void setNextStartDate(@NonNull Date nextStartDate) {
        this.nextStartDate = nextStartDate;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    @NonNull
    public Long getNotificationDelta() {
        return notificationDelta;
    }

    public void setNotificationDelta(@NonNull Long notificationDelta) {
        this.notificationDelta = notificationDelta;
    }

    @NonNull
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(@NonNull Date endDate) {
        this.endDate = endDate;
    }

    @Ignore
    public List<TaskStep> getSteps() {
        return steps;
    }

    @Ignore
    public void setSteps(List<TaskStep> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && title.equals(task.title) && description.equals(task.description) && nextStartDate.equals(task.nextStartDate) && Objects.equals(interval, task.interval) && notificationDelta.equals(task.notificationDelta) && endDate.equals(task.endDate) && Objects.equals(steps, task.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, nextStartDate, interval, notificationDelta, endDate, steps);
    }
}
