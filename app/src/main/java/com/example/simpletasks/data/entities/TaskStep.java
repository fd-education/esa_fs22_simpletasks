package com.example.simpletasks.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.simpletasks.data.domains.TaskStepTypes;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "taskSteps",
        foreignKeys = {
            @ForeignKey(entity = Task.class,
                parentColumns = "id",
                childColumns = "fk_task_id",
                onDelete = ForeignKey.CASCADE)},
                indices = {@Index(value="fk_task_id")}
        )
public class TaskStep implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;

    private int index;

    @NonNull
    private String type;

    @NonNull
    private String title;

    @ColumnInfo(name="image_uri")
    private String imageUri;

    private String description;

    @ColumnInfo(name="video_uri")
    private String videoUri;

    @ColumnInfo(name="audio_uri")
    private String audioUri;

    @ColumnInfo(name="fk_task_id")
    private String taskId;

    public TaskStep(String taskId, @NonNull String type, int index, @NonNull String title, String imageUri, String description, String videoUri, String audioUri){
        this.id = UUID.randomUUID().toString();
        this.taskId = taskId;
        this.type = type;
        this.index = index;
        this.title = title;
        this.imageUri = imageUri;
        this.description = description;
        this.videoUri = videoUri;
        this.audioUri = audioUri;
    }

    @Ignore
    public TaskStep(String taskId, TaskStepTypes type, int index, @NonNull String title, String imageUri, String description, String videoUri, String audioUri){
        this.id = UUID.randomUUID().toString();
        this.taskId = taskId;
        this.type = type.toString();
        this.index = index;
        this.title = title;
        this.imageUri = imageUri;
        this.description = description;
        this.videoUri = videoUri;
        this.audioUri = audioUri;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri = audioUri;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskStep taskStep = (TaskStep) o;
        return id.equals(taskStep.id) && index == taskStep.index && Objects.equals(taskId, taskStep.taskId) && type.equals(taskStep.type) && title.equals(taskStep.title) && Objects.equals(imageUri, taskStep.imageUri) && description.equals(taskStep.description) && Objects.equals(videoUri, taskStep.videoUri) && Objects.equals(audioUri, taskStep.audioUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, index, type, title, imageUri, description, videoUri, audioUri, taskId);
    }
}
