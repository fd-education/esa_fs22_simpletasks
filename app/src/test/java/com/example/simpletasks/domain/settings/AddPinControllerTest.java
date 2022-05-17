package com.example.simpletasks.domain.settings;

import android.content.Context;

import com.example.simpletasks.data.entities.Pin;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddPinControllerTest extends TestCase {

    @Mock
    Context context;

    @Test
    public void testSendPin() {
        FirebaseApp.initializeApp(context);
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        functions.useEmulator("10.0.2.2", 5001);
        AddPinController.sendPin(Pin.fromInt(123123), new PhoneNumber(), functions);
    }
}