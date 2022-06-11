package com.example.simpletasks.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * Entity that models a pin used to authenticate a user.
 */
@Entity(tableName = "pins")
public class Pin {
    @PrimaryKey
    @NonNull
    private String id;

    // Store the hashes of the pins
    private int hash;

    // For the pojo the pin is used as a String
    @Ignore
    private String pin;

    @Ignore
    public Pin(final String pin) {
        validatePin(pin);
        this.id = UUID.randomUUID().toString();
        this.hash = pin.hashCode();
        this.pin = pin;
    }

    public Pin(int hash) {
        this.id = UUID.randomUUID().toString();
        this.hash = hash;
    }

    /**
     * Get the id of the Pin object.
     *
     * @return the id of the Pin object.
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * Set the id of the Pin object.
     *
     * @param id the id to set
     */
    public void setId(@NonNull String id) {
        this.id = id;
    }

    /**
     * Get the pin value from the pojo.
     *
     * @return the pin value as a String
     */
    public String getPin() {
        return pin;
    }

    /**
     * Set the Pin and the hash.
     *
     * @param pin String of the pin to be set.
     */
    public void setPin(String pin) {
        validatePin(pin);
        this.pin = pin;
        this.hash = pin.hashCode();
    }

    /**
     * Get the hash of the Pin
     *
     * @return the hash value of the pin
     */
    public int getHash() {
        return hash;
    }

    /**
     * Takes an integer value and creates a Pin object from it.
     *
     * @param pin the int value to create a Pin object from
     * @return a Pin object
     */
    public static Pin fromInt(int pin) {
        String pinStr = String.format(Locale.getDefault(), "%05d", pin);
        return new Pin(pinStr);
    }

    /**
     * Creates a random Pin with a value ranging from 0 through 9999
     *
     * @return a Pin object
     */
    static public Pin random() {
        Random random = new Random();
        int randomInt = random.nextInt(10_000);
        return Pin.fromInt(randomInt);
    }

    // Validate pin values against a regex that checks for five or more digits
    private void validatePin(final String pin) {
        Objects.requireNonNull(pin, "PIN must not be null");
        if (!pin.matches("^\\d{5,}$")) {
            throw new IllegalArgumentException(pin + " is not a valid PIN; must consist of at least 5 digits");
        }
    }
}