package com.example.simpletasks.data.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

@Entity(tableName = "pins")
public class Pin {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int hash;

    @Ignore
    private String pin;

    @Ignore
    public Pin(final String pin) {
        validatePin(pin);
        this.pin = pin;
        this.hash = pin.hashCode();
    }

    private void validatePin(final String pin) {
        Objects.requireNonNull(pin, "PIN must not be null");
        if (!pin.matches("^\\d{5,}$")){
            throw new IllegalArgumentException(pin+" is not a valid PIN; must consist of at least 5 digits");
        }
    }

    public Pin(int hash) {
        this.hash = hash;
    }

    public static Pin fromInt(int pin) {
        String pinStr = String.format(Locale.getDefault(), "%05d", pin);
        return new Pin(pinStr);
    }

    static public Pin random() {
        Random random = new Random();
        int randomInt = random.nextInt(10_000);
        return Pin.fromInt(randomInt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        validatePin(pin);
        this.pin = pin;
        this.hash = pin.hashCode();
    }

    public int getHash() {
        return hash;
    }
}