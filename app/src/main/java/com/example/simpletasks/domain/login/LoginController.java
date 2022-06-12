package com.example.simpletasks.domain.login;

import android.app.Application;

import com.example.simpletasks.data.repositories.PinRepository;

/**
 * Controller with business logic required for the login process.
 */
public class LoginController implements Login {
    private final PinRepository pinRepo;

    /**
     * Constructor takes the context to get the pin repository.
     *
     * @param application application context for the repository
     */
    public LoginController(Application application){
        pinRepo = new PinRepository(application);
    }

    /**
     * Check if the entered pin is valid.
     *
     * @param pin pin that was entered by the user
     * @return true if the pin is valid, false otherwise
     */
    @Override
    public boolean isValidPin(String pin){
        return pinRepo.isValidPin(pin.hashCode());
    }
}
