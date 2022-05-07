package com.example.simpletasks.data.repositories;

import android.app.Application;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.entities.Pin;

public class PinRepository {
    private final PinDao pinDao;

    public PinRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDb(application);
        pinDao = db.pinDao();
    }

    public void insertPin(final Pin pin) {
        AppDatabase.databaseWriteExecutor.execute(() -> pinDao.insertPin(pin));
    }

    public void deletePin(final Pin pin) {
        AppDatabase.databaseWriteExecutor.execute(() -> pinDao.deletePin(pin));
    }

    public boolean isValidPin(int pin) {
        int pinHash = ((Integer) pin).hashCode();

        return (pinDao.findByValue(pinHash) == 1);
    }
}
