package com.example.simpletasks.domain.settings;

import com.example.simpletasks.data.entities.Pin;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import java.util.HashMap;
import java.util.Map;

public class AddPinController {
    public static Task<String> sendPin(Pin pin, PhoneNumber phoneNumber, FirebaseFunctions firebaseFunctions) {
        Map<String, Object> data = new HashMap<>();
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String formattedPhoneNumber = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        data.put("phoneNumber", formattedPhoneNumber);
        data.put("pin", pin.getPin());

        return firebaseFunctions
                .getHttpsCallable("sendPin")
                .call(data)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
                    return (String) task.getResult().getData();
                });
    }
}
