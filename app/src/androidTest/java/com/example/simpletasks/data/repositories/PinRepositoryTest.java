package com.example.simpletasks.data.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.entities.Pin;
import com.google.common.util.concurrent.ListenableFuture;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class PinRepositoryTest {

    private PinRepository pinRepository;
    private PinDao pinDao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        pinRepository = new PinRepository(context);
        pinDao = AppDatabase.getAppDb(context).pinDao();
        pinDao.deleteAll();
    }

    @Test
    public void testAddPin() throws ExecutionException, InterruptedException {
        final Pin pin = Pin.fromInt(123);

        final ListenableFuture<Long> future = pinRepository.insertPin(pin);
        future.get();

        assertTrue(future.isDone());
        pinDao.isExists(pin.getHash());
    }

    @Test
    public void testDoesPinExist() throws ExecutionException, InterruptedException {
        final Pin existingPin = Pin.fromInt(123);
        final Pin notExistingPin = Pin.fromInt(1234);
        pinDao.insertPin(existingPin).get();

        final ListenableFuture<Boolean> existingPinFuture = pinRepository.isValidPin(existingPin);
        final ListenableFuture<Boolean> notExistingPinFuture = pinRepository.isValidPin(notExistingPin);

        assertTrue(existingPinFuture.get());
        assertFalse(notExistingPinFuture.get());
    }

    @Test
    public void testDeletePin() throws ExecutionException, InterruptedException {
        final Pin existingPin = Pin.fromInt(123);
        final Pin notExistingPin = Pin.fromInt(1234);
        pinDao.insertPin(existingPin).get();

        final ListenableFuture<Boolean> deletedPinFuture = pinRepository.deletePin(existingPin);
        final ListenableFuture<Boolean> notDeletedPinFuture = pinRepository.deletePin(notExistingPin);

        assertTrue(deletedPinFuture.get());
        assertFalse(notDeletedPinFuture.get());
        assertFalse(pinDao.isExists(existingPin.getHash()).get());
    }

    @Test
    public void testGetPinCount() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 20; i++) {
            pinDao.insertPin(Pin.fromInt(i)).get();
        }

        final ListenableFuture<Integer> pinCount = pinRepository.getPinCount();

        assertEquals(20L, (long) pinCount.get());
    }


}