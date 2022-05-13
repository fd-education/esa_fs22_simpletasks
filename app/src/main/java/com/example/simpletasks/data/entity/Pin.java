package com.example.simpletasks.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;
import java.util.Random;

@Entity(tableName = "pins")
public class Pin {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int pin;

    public Pin(int pin){
        this.pin = pin;
    }

    static public Pin random() {
        Random random = new Random();
        int randomInt = random.nextInt(10_000);
        return new Pin(randomInt);
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