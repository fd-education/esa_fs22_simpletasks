package com.example.simpletasks.domain.login;

import androidx.lifecycle.LiveData;

public interface Login {
    boolean isValidPin(String pin);
}
