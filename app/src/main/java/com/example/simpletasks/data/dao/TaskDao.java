package com.example.simpletasks.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpletasks.data.entity.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Tasks " +
            "ORDER BY next_start_date ASC")
    LiveData<List<Task>> getAll();

    @Query("SELECT * FROM Tasks " +
            "WHERE next_start_date >= :dayStart AND next_start_date < :dayEnd " +
            "ORDER BY next_start_date ASC")
    LiveData<List<Task>> getByDate(Date dayStart, Date dayEnd);

    @Query("SELECT * FROM Tasks WHERE id = :id")
    LiveData<Task> getById(int id);

    @Update
    void updateTasks(Task... tasks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTasks(Task... tasks);

    @Delete
    void deleteTasks(Task... tasks);
}
