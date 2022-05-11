package com.example.simpletasks.data.daos;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskWithSteps;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks " +
            "ORDER BY next_start_date ASC")
    LiveData<List<Task>> getAll();

    @Transaction
    @Query("SELECT * FROM tasks " +
            "ORDER BY next_start_date ASC")
    LiveData<List<TaskWithSteps>> getAllWithSteps();

    @Query("SELECT * FROM tasks " +
            "WHERE next_start_date >= :dayStart AND next_start_date < :dayEnd " +
            "ORDER BY next_start_date ASC")
    LiveData<List<Task>> getByDate(Date dayStart, Date dayEnd);

    @Transaction
    @Query("SELECT * FROM tasks " +
            "WHERE next_start_date >= :dayStart AND next_start_date < :dayEnd " +
            "ORDER BY next_start_date ASC")
    LiveData<List<TaskWithSteps>> getByDateWithSteps(Date dayStart, Date dayEnd);

    @Query("SELECT * FROM tasks WHERE id = :id")
    Task getById(int id);

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskWithSteps getByIdWithSteps(int id);

    @Update
    void updateTasks(List<Task> tasks);

    @Insert(onConflict = REPLACE)
    void insertTasks(List<Task> tasks);

    @Delete
    void deleteTasks(List<Task> tasks);
}
