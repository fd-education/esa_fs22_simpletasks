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

public class PinRepository {
    private final PinDao pinDao;

    public PinRepository(Application application) {
        // TODO Uncomment for final submission and delete seed version
        // AppDatabase db = AppDatabase.getAppDb(application);
        AppDatabase db = AppDatabase.getSeededAppDb(application, true, true);
        pinDao = db.pinDao();
    }

    public void insertPin(final Pin pin) {
        AppDatabase.databaseWriteExecutor.execute(() -> pinDao.insertPin(pin));
    }

    public void deletePin(final Pin pin) {
        AppDatabase.databaseWriteExecutor.execute(() -> pinDao.deletePin(pin));
    }

    public void deleteAll(){
        pinDao.deleteAll();
    }

    public Boolean isValidPin(int pin) {
        try{
            return pinDao.isExists(pin).get();
        } catch(ExecutionException | InterruptedException e){
            return false;
        }
    }
}
