package com.example.simpletasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;

import com.example.simpletasks.data.entities.Pin;
import com.example.simpletasks.domain.settings.PinController;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import org.junit.Test;

public class AddPinActivityTest {
    @Test
    public void testSendPin() throws NumberParseException {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
//        functions.useEmulator("10.0.2.2", 5001); //For local Firebase Function, probably faster
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String numberToParse = "+41790000000"; //This phone number is ignored and does not send a sms
        PhoneNumber phoneNumber = phoneNumberUtil.parseAndKeepRawInput(numberToParse, "CH");
        PinController pinController = new PinController(ApplicationProvider.getApplicationContext());
        Task<String> task = pinController.sendPin(Pin.fromInt(123123), phoneNumber, functions);
        //noinspection StatementWithEmptyBody
        while (!task.isComplete()) ;
        assertEquals(task.getResult(), "Success");
        assertTrue(task.isSuccessful());
    }
}