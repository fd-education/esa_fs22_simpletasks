package com.example.simpletasks.domain.settings;

import com.example.simpletasks.data.entity.Pin;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPinController {
    public static Task<String> sendPin(Pin pin, Phonenumber.PhoneNumber phoneNumber, FirebaseFunctions firebaseFunctions) {
        Map<String, Object> data = new HashMap<>();
        data.put("phoneNumber", phoneNumber.toString());
        data.put("pin", String.format(Locale.getDefault(), "%06d", pin.getPin()));

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
