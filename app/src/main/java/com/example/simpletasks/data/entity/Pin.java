package com.example.simpletasks.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity(tableName = "pins")
public class Pin {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int pin;

    public Pin(int pin){
        this.pin = pin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%06d", pin);
    }
}