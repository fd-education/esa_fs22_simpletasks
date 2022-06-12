package com.example.simpletasks.data.repositories;

import android.content.Context;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.entities.Pin;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

/**
 * Handles all the interactions with the pin table through its DAO.
 */
public class PinRepository {
    private final PinDao pinDao;

    /**
     * Initialize the repository using the context context.
     *
     * @param context the context
     */
    public PinRepository(Context context) {
        AppDatabase db = AppDatabase.getAppDb(context);
        // AppDatabase db = AppDatabase.getSeededAppDb(context, true, true);
        pinDao = db.pinDao();
    }

    /**
     * Insert a pin entity (row) into the pin table.
     *
     * @param pin the pin entity to insert
     * @return a future that returns the row id of the inserted pin
     */
    public ListenableFuture<Long> insertPin(final Pin pin) {
        return Futures.submit(() -> pinDao.insertPin(pin).get(), AppDatabase.databaseWriteExecutor);
    }

    /**
     * Delete a pin entity (row) from the pin table
     *
     * @param pin the pin entity to delete
     * @return a future that returns true if the pin existed and was deleted, and false if the pin did not exist
     */
    public ListenableFuture<Boolean> deletePin(final Pin pin) {
        return Futures.submit(() -> {
            final Pin[] foundPins = pinDao.findByHash(pin.getHash()).get();
            if (foundPins.length == 0) {
                return false;
            }
            for (Pin foundPin : foundPins) {
                pinDao.deletePin(foundPin);
            }
            return true;
        }, AppDatabase.databaseWriteExecutor);
    }

    /**
     * Check if a hashed pin exists in the database.
     *
     * @param pinHash the hash of the pin to check
     * @return true if the hash exists, false otherwise
     */
    public Boolean isValidPin(int pinHash) {
        try {
            return pinDao.isExists(pinHash).get();
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Gets the count of stored pins
     *
     * @return a future that returns the count of pins
     */
    public ListenableFuture<Integer> getPinCount() {
        return Futures.submit(() -> pinDao.getPins().get().length, AppDatabase.databaseWriteExecutor);
    }

    /**
     * Checks if the pin exists in the database.
     * This only uses the value of the pin and not it's id
     *
     * @param pin the pin to be checked
     * @return a future that returns whether the pin exists in the database
     */
    public ListenableFuture<Boolean> isValidPin(final Pin pin) {
        return Futures.submit(() -> pinDao.isExists(pin.getHash()).get(), AppDatabase.databaseWriteExecutor);
    }
}
