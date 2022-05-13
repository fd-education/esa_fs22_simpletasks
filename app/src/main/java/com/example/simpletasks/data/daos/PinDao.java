package com.example.simpletasks.data.daos;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.simpletasks.data.entities.Pin;

@Dao
public interface PinDao {
    @Insert(onConflict = REPLACE)
    void insertPin(Pin pin);

    @Delete
    void deletePin(Pin pin);

    @Query("SELECT EXISTS(SELECT * FROM Pins WHERE hash = :pinHash)")
    boolean isExists(int pinHash);

    @Query("DELETE FROM pins")
    void deleteAll();
}
