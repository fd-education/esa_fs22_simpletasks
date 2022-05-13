package com.example.simpletasks.data.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.simpletasks.data.entities.Pin;


@Dao
public interface PinDao {
    @Insert
    void insertPin(Pin pin);

    @Delete
    void deletePin(Pin pin);

    @Query("SELECT COUNT() FROM Pins WHERE hash = :pinHash")
    int findByValue(int pinHash);
}
