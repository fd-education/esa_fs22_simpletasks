package com.example.simpletasks.domain.login;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.repositories.PinRepository;

public class LoginController implements Login {
    private PinRepository pinRepo;

    public LoginController(Application application){
        pinRepo = new PinRepository(application);
    }

    @Override
    public boolean isValidPin(String pin){
        return pinRepo.isValidPin(pin.hashCode());
    }
}
