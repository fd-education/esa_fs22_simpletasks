package com.example.simpletasks.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpletasks.data.entity.TaskStep;

import java.util.List;

@Dao
public interface TaskStepDao {

    @Query("SELECT * FROM TaskSteps " +
            "WHERE fk_task_id = :taskId " +
            "ORDER BY `index` ASC")
    LiveData<List<TaskStep>> getByTaskId(int taskId);

    @Update
    void updateTaskSteps(TaskStep... taskSteps);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTaskSteps(TaskStep... taskSteps);

    @Delete
    void deleteTaskSteps(TaskStep... taskSteps);
}
