package com.example.simpletasks.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.entities.Pin;

import java.security.InvalidParameterException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    public void deleteAll(){
        pinDao.deleteAll();
    }

    public Boolean isValidPin(int pin) {
        try {
            Future<Boolean> future = AppDatabase.databaseWriteExecutor.submit(new ValidateTask(pin));

            return future.get();
        } catch(ExecutionException e){
            return false;
        } catch(InterruptedException e){
            return false;
        }
    }

    class ValidateTask implements Callable<Boolean>{
        boolean isValid;
        int pin;

        ValidateTask(int pin){
            this.pin = pin;
        }

        public Boolean call() throws InvalidParameterException{
           return pinDao.isExists(pin);
        }
    }
}
