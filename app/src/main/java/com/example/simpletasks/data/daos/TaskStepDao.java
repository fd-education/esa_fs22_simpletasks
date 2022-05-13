package com.example.simpletasks.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpletasks.data.entities.TaskStep;

import java.util.List;

@Dao
public interface TaskStepDao {

    @Query("SELECT * FROM TaskSteps " +
            "WHERE fk_task_id = :taskId " +
            "ORDER BY `index` ASC")
    LiveData<List<TaskStep>> getByTaskId(String taskId);

    @Update
    void updateTaskSteps(List<TaskStep> taskSteps);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTaskSteps(List<TaskStep> taskSteps);

    @Delete
    void deleteTaskSteps(List<TaskStep> taskSteps);
}
