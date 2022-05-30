package com.example.simpletasks.data.daos;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.simpletasks.data.entities.Pin;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * DAO to interact with the pin table in the room database.
 */
@Dao
public interface PinDao {
    /**
     * Insert a pin entity (row) into the pin table.
     * Replace row on conflict.
     *
     * @param pin the pin entity to insert
     */
    @Insert(onConflict = REPLACE)
    ListenableFuture<Long> insertPin(Pin pin);


    /**
     * Check if a hashed pin exists in the database.
     *
     * @param pinHash the hash of the pin to check
     * @return true if the hash exists, false otherwise
     */
    @Query("SELECT EXISTS(SELECT * FROM Pins WHERE hash = :pinHash)")
    ListenableFuture<Boolean> isExists(int pinHash);

    /**
     * Delete a pin entity (row) from the pin table
     *
     * @param pin the pin entity to delete
     */
    @Delete
    void deletePin(Pin pin);


    /**
     * Delete all data from the pins database.
     *
     * Currently only used for the seeding process to avoid huge amounts of data
     * through constant seeding at application start-up.
     */
    @Query("DELETE FROM pins")
    void deleteAll();

    /**
     * Returns all pins that match the given hash
     *
     * @param pinHash the hash to be looked up
     * @return a future that returns all found pins
     */
    @Query("SELECT * FROM Pins WHERE hash = :pinHash")
    ListenableFuture<Pin[]> findByHash(int pinHash);

    /**
     * Returns all stored pins
     *
     * @return all pins stored in the database
     */
    @Query("SELECT * FROM Pins")
    ListenableFuture<Pin[]> getPins();
}
