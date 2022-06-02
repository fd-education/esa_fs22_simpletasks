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
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.List;

/**
 * DAO to interact with the tasks table in the room database.
 */
@Dao
public interface TaskDao {

    /**
     * Fetch all task entities from the tasks table
     *
     * @return LiveData<List<Task>> observable with all task entities
     */
    @Query("SELECT * FROM tasks " +
            "ORDER BY next_start_date ASC")
    LiveData<List<Task>> getAll();

    /**
     * Fetch all task entities with their steps from the tasks table
     *
     * @return LiveData<List<TaskWithSteps>> observable with all task entities and their steps
     */
    @Transaction
    @Query("SELECT * FROM tasks " +
            "ORDER BY next_start_date ASC")
    LiveData<List<TaskWithSteps>> getAllWithSteps();

    /**
     * Fetch all task entities in a given date span.
     *
     * @param dayStart starting date of the date span (incl.)
     * @param dayEnd ending date of the date span (excl.)
     * @return LiveData<List<Task>> observable with all task entities in that date span
     */
    @Query("SELECT * FROM tasks " +
            "WHERE next_start_date >= :dayStart AND next_start_date < :dayEnd " +
            "ORDER BY next_start_date ASC")
    LiveData<List<Task>> getByDate(Date dayStart, Date dayEnd);

    /**
     * Fetch all task entities with their task steps in a given date span
     *
     * @param dayStart starting date of the date span (incl.)
     * @param dayEnd ending date of the date span (excl.)
     * @return @return LiveData<List<TaskWithSteps>> observable with all task entities and their steps
     */
    @Transaction
    @Query("SELECT * FROM tasks " +
            "WHERE next_start_date >= :dayStart AND next_start_date < :dayEnd " +
            "ORDER BY next_start_date ASC")
    LiveData<List<TaskWithSteps>> getByDateWithSteps(Date dayStart, Date dayEnd);

    /**
     * Get a task by its id from the tasks table.
     *
     * @param id the id to lookup
     * @return a task entity
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    Task getById(int id);

    /**
     * Get a task with its steps by its id from the tasks table.
     *
     * @param id the id to lookup
     * @return a task entity
     */
    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskWithSteps getByIdWithSteps(int id);

    /**
     * Update a list of tasks in the tasks table.
     *
     * @param tasks the tasks to update
     */
    @Update
    void updateTasks(List<Task> tasks);

    /**
     * Insert a list of tasks into the tasks table.
     *
     * @param tasks the tasks to insert
     */
    @Insert(onConflict = REPLACE)
    void insertTasks(List<Task> tasks);

    /**
     * Delete a list of tasks from the tasks table.
     *
     * @param tasks the tasks to delete
     */
    @Delete()
    void deleteTasks(List<Task> tasks);

    /**
     * Delete all data from the tasks database.
     */
    @Query("DELETE FROM tasks")
    ListenableFuture<Integer> deleteAll();
}
