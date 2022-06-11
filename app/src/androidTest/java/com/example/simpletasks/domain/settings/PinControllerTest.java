package com.example.simpletasks.domain.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.entities.Pin;
import com.example.simpletasks.domain.settings.PinController;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class PinControllerTest {

    private PinController pinController;
    private Context context;
    private PinDao pinDao;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        pinController = new PinController(context);
        pinDao = AppDatabase.getAppDb(context).pinDao();
        pinDao.deleteAll();
    }

    @Test
    public void testSendPin() throws NumberParseException {
        FirebaseApp.initializeApp(context);
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
//        functions.useEmulator("10.0.2.2", 5001); //For local Firebase Function, probably faster
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String numberToParse = "+41790000000"; //This phone number is ignored and does not send a sms
        PhoneNumber phoneNumber = phoneNumberUtil.parseAndKeepRawInput(numberToParse, "CH");

        Task<String> task = pinController.sendPin(Pin.fromInt(123123), phoneNumber, functions);
        //noinspection StatementWithEmptyBody
        while (!task.isComplete()) ;

        assertEquals(task.getResult(), "Success");
        assertTrue(task.isSuccessful());
    }

    @Test
    public void testAddPin() throws ExecutionException, InterruptedException {
        final Pin pin = Pin.fromInt(123);

        final ListenableFuture<Long> future = pinController.addPin(pin);
        future.get();

        assertTrue(future.isDone());
        pinDao.isExists(pin.getHash());
    }

    @Test
    public void testDoesPinExist() throws ExecutionException, InterruptedException {
        final Pin existingPin = Pin.fromInt(123);
        final Pin notExistingPin = Pin.fromInt(1234);
        pinDao.insertPin(existingPin).get();

        final ListenableFuture<Boolean> existingPinFuture = pinController.doesPinExist(existingPin);
        final ListenableFuture<Boolean> notExistingPinFuture = pinController.doesPinExist(notExistingPin);

        assertTrue(existingPinFuture.get());
        assertFalse(notExistingPinFuture.get());
    }

    @Test
    public void testDeletePin() throws ExecutionException, InterruptedException {
        final Pin existingPin = Pin.fromInt(123);
        final Pin notExistingPin = Pin.fromInt(1234);
        pinDao.insertPin(existingPin).get();

        final ListenableFuture<Boolean> deletedPinFuture = pinController.deletePin(existingPin);
        final ListenableFuture<Boolean> notDeletedPinFuture = pinController.deletePin(notExistingPin);

        assertTrue(deletedPinFuture.get());
        assertFalse(notDeletedPinFuture.get());
        assertFalse(pinDao.isExists(existingPin.getHash()).get());
    }

    @Test
    public void testGetPinCount() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 20; i++) {
            pinDao.insertPin(Pin.fromInt(i)).get();
        }

        final ListenableFuture<Integer> pinCount = pinController.getPinCount();

        assertEquals(20L, (long) pinCount.get());
    }


}