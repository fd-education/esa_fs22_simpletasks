package com.example.simpletasks.data.daos;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT EXISTS(SELECT * FROM Pins WHERE pin = :pinHash)")
    boolean isExists(int pinHash);
}
