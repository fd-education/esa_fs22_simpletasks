package com.example.simpletasks.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "taskSteps",
        foreignKeys = {
            @ForeignKey(entity = Task.class,
                parentColumns = "id",
                childColumns = "fk_task_id",
                onDelete = ForeignKey.CASCADE)},
                indices = {@Index(value="fk_task_id")}
        )

public class TaskStep {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int index;

    private String type;

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

    public TaskStep(String taskId, String type, int index, String title, String imageUri, String description, String videoUri, String audioUri){
        this.taskId = taskId;
        this.type = type;
        this.index = index;
        this.title = title;
        this.imageUri = imageUri;
        this.description = description;
        this.videoUri = videoUri;
        this.audioUri = audioUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
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
}
