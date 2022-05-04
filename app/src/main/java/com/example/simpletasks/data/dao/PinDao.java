package com.example.simpletasks.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.simpletasks.data.entity.Pin;


@Dao
public interface PinDao {
    @Insert
    void insertPin(Pin pin);

    @Delete
    void deletePin(Pin pin);

    @Query("SELECT COUNT() FROM Pins WHERE pin = :pinHash")
    int findByValue(int pinHash);
}
