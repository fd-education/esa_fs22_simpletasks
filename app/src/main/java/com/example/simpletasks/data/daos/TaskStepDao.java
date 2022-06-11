package com.example.simpletasks.data.daos;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpletasks.data.entities.TaskStep;

import java.util.List;

/**
 * DAO to interact with the taskSteps table in the room database.
 */
@Dao
public interface TaskStepDao {

    /**
     * Fetch all task step entities of a tasks specified by its id ordered by their index
     *
     * @param taskId the taskId to lookup
     * @return LiveData<List<TaskStep>> observable with all task step entities of a task
     */
    @Query("SELECT * FROM TaskSteps " +
            "WHERE fk_task_id = :taskId " +
            "ORDER BY `index` ASC")
    LiveData<List<TaskStep>> getByTaskId(String taskId);

    /**
     * Insert a taskStep into the taskSteps table.
     *
     * @param taskStep the taskSteps to insert
     */
    @Insert(onConflict = REPLACE)
    void insertTaskStep(TaskStep taskStep);

    /**
     * Insert a list of taskSteps into the taskSteps table.
     *
     * @param taskSteps the taskSteps to insert
     */
    @Insert(onConflict = REPLACE)
    void insertTaskSteps(List<TaskStep> taskSteps);

    /**
     * Update a list of task steps in the taskSteps table.
     *
     * @param taskSteps the tasks to update
     */
    @Update
    void updateTaskSteps(List<TaskStep> taskSteps);

    /**
     * Delete one task step from the taskSteps table.
     * @param taskStep the task step to delete
     */
    @Delete
    void deleteTaskStep(TaskStep taskStep);

    /**
     * Delete a list of task steps from the taskSteps table.
     *
     * @param taskSteps the tasks to delete
     */
    @Delete
    void deleteTaskSteps(List<TaskStep> taskSteps);

    /**
     * Delete all data from the taskSteps database.
     */
    @Query("DELETE FROM taskSteps")
    void deleteAll();
}
