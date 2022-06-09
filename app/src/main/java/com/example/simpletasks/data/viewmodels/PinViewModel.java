package com.example.simpletasks.data.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.simpletasks.data.entities.*;
import com.example.simpletasks.data.repositories.PinRepository;

/**
 * Store and manage Pin data.
 */
public class PinViewModel extends AndroidViewModel {

    private final PinRepository pinRepo;

    /**
     * Initialize the view model and the pin repository using the app context.
     * @param application the app context
     */
    public PinViewModel(Application application) {
        super(application);
        pinRepo = new PinRepository(application);
    }

    /**
     * Insert a pin entity (row) into the pin table.
     * Replace row on conflict.
     *
     * @param pin the pin entity to insert
     */
    public void insertPin(Pin pin) {
        pinRepo.insertPin(pin);
    }

    /**
     * Check if a pin exists in the database.
     *
     * @param pin the pin to check
     * @return true if the pin exists, false otherwise
     */
    public boolean isValidPin(final String pin){
        return pinRepo.isValidPin(pin.hashCode());
    }

    /**
     * Delete a pin entity (row) from the pin table
     *
     * @param pin the pin entity to delete
     */
    public void deletePin(Pin pin) {
        pinRepo.deletePin(pin);
    }
}
