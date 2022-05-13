package com.example.simpletasks.data.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.entities.*;
import com.example.simpletasks.data.repositories.PinRepository;


public class PinViewModel extends AndroidViewModel {

    private final PinRepository pinRepo;

    public PinViewModel(Application application) {
        super(application);
        pinRepo = new PinRepository(application);
    }

    public void deletePin(Pin pin) {
        pinRepo.deletePin(pin);
    }

    public void insertPin(Pin pin) {
        pinRepo.insertPin(pin);
    }

    public boolean isValidPin(final String pin){
        return pinRepo.isValidPin(pin.hashCode());
    }
}
