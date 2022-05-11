package com.example.simpletasks.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "pins")
public class Pin {
    @PrimaryKey
    @NonNull
    private String id;

    private int pin;

    public Pin(int pin) {
        this.id = UUID.randomUUID().toString();
        this.pin = pin;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id){
        this.id = id;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}