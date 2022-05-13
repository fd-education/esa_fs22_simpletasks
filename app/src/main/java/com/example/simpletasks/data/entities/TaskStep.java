package com.example.simpletasks.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.simpletasks.data.types.TaskStepTypes;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity that models a task step.
 */
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

    @ColumnInfo(name="image_path")
    private String imagePath;

    private String description;

    @ColumnInfo(name="video_path")
    private String videoPath;

    @ColumnInfo(name="audio_path")
    private String audioPath;

    @ColumnInfo(name="fk_task_id")
    private String taskId;

    public TaskStep(String taskId, @NonNull String type, int index, @NonNull String title, String imagePath, String description, String videoPath, String audioPath){
        this.id = UUID.randomUUID().toString();
        this.taskId = taskId;
        this.type = type;
        this.index = index;
        this.title = title;
        this.imagePath = imagePath;
        this.description = description;
        this.videoPath = videoPath;
        this.audioPath = audioPath;
    }

    @Ignore
    public TaskStep(String taskId, TaskStepTypes type, int index, @NonNull String title, String imagePath, String description, String videoPath, String audioPath){
        this.id = UUID.randomUUID().toString();
        this.taskId = taskId;
        this.type = type.toString();
        this.index = index;
        this.title = title;
        this.imagePath = imagePath;
        this.description = description;
        this.videoPath = videoPath;
        this.audioPath = audioPath;
    }

    /**
     * Get the id of this task step.
     *
     * @return the id of this task step object.
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * Set the id of this step
     *
     * @param id the id of this step
     */
    public void setId(@NonNull String id) {
        this.id = id;
    }

    /**
     * Get the index of this task step.
     *
     * @return the index of this task step object.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the index of this step
     *
     * @param index the index of this step
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Get the type of this task step.
     *
     * @return the type of this task step object.
     */
    @NonNull
    public String getType() {
        return type;
    }

    /**
     * Set the type of this step
     *
     * @param type the type of this step
     */
    public void setType(@NonNull String type) {
        this.type = type;
    }

    /**
     * Get the title of this task step.
     *
     * @return the title of this task step object.
     */
    @NonNull
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of this step
     *
     * @param title the description of this step
     */
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    /**
     * Get the path to find the title image of this task step.
     *
     * @return the path for the title image of this task step object.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Set the path to the title image of this step
     *
     * @param imagePath the path to the title image of this step
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Get the description of this task step.
     *
     * @return the description of this task step object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this step
     *
     * @param description the description of this step
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the path to find the video description of this task step.
     *
     * @return the path for the video description of this task step object.
     */
    public String getVideoPath() {
        return videoPath;
    }

    /**
     * Set the path to the video description of this step
     *
     * @param videoPath the path to the video description
     */
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    /**
     * Get the path to find the audio description of this task step.
     *
     * @return the path for the audio description of this task step object.
     */
    public String getAudioPath() {
        return audioPath;
    }

    /**
     * Set the path to the audio description of this step
     *
     * @param audioPath the path to the audio description
     */
    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    /**
     * Get the id of the task this step belongs to.
     *
     * @return the id of the task this step belongs to.
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Set the id of the task this step belongs to.
     *
     * @param taskId the id of the task this step belongs to
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
        TaskStep taskStep = (TaskStep) o;
        return id.equals(taskStep.id) && index == taskStep.index && Objects.equals(taskId, taskStep.taskId) && type.equals(taskStep.type) && title.equals(taskStep.title) && Objects.equals(imagePath, taskStep.imagePath) && description.equals(taskStep.description) && Objects.equals(videoPath, taskStep.videoPath) && Objects.equals(audioPath, taskStep.audioPath);
    }

    /**
     * Return the hashcode for this task.
     *
     * @return a hash consisting of all fields of the task object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, index, type, title, imagePath, description, videoPath, audioPath, taskId);
    }
}
