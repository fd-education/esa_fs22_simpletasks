package com.example.simpletasks.domain.login;

/**
 * Interface for the business logic required for the login process.
 */
public interface Login {
    /**
     * Validate whether or not a pin is valid or not.
     *
     * @param pin pin that was entered by the user
     * @return true if the pin is valid, false otherwise
     */
    boolean isValidPin(String pin);
}
