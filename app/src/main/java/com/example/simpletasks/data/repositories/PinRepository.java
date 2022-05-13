package com.example.simpletasks.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.entities.Pin;
import com.google.common.util.concurrent.ListenableFuture;

import java.security.InvalidParameterException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Handles all the interactions with the pin table through its DAO.
 */
public class PinRepository {
    private final PinDao pinDao;

    /**
     * Initialize the repository using the application context.
     *
     * @param application the context
     */
    public PinRepository(Application application) {
        // TODO Uncomment for final submission and delete seed version
        // AppDatabase db = AppDatabase.getAppDb(application);
        AppDatabase db = AppDatabase.getSeededAppDb(application, true, true);
        pinDao = db.pinDao();
    }

    /**
     * Insert a pin entity (row) into the pin table.
     *
     * @param pin the pin entity to insert
     */
    public void insertPin(final Pin pin) {
        AppDatabase.databaseWriteExecutor.execute(() -> pinDao.insertPin(pin));
    }

    /**
     * Delete a pin entity (row) from the pin table
     *
     * @param pin the pin entity to delete
     */
    public void deletePin(final Pin pin) {
        AppDatabase.databaseWriteExecutor.execute(() -> pinDao.deletePin(pin));
    }

    /**
     * Check if a hashed pin exists in the database.
     *
     * @param pinHash the hash of the pin to check
     * @return true if the hash exists, false otherwise
     */
    public Boolean isValidPin(int pinHash) {
        try{
            return pinDao.isExists(pinHash).get();
        } catch(ExecutionException | InterruptedException e){
            return false;
        }
    }
}
