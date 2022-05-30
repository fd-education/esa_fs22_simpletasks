package com.example.simpletasks.domain.settings;

import android.content.Context;

import com.example.simpletasks.data.entities.Pin;
import com.example.simpletasks.data.repositories.PinRepository;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import java.util.HashMap;
import java.util.Map;

public class PinController {

    private final PinRepository pinRepository;

    public PinController(Context context) {
        pinRepository = new PinRepository(context);
    }

    /**
     * Send the pin to the specified phoneNumber using the specified firebaseFunctions
     *
     * @param pin the pin to send
     * @param phoneNumber the phone number of the recipient
     * @param firebaseFunctions the firebase function used to send the pin
     * @return the result of the http transmission
     */
    public Task<String> sendPin(Pin pin, PhoneNumber phoneNumber, FirebaseFunctions firebaseFunctions) {
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

    /**
     * Adds a given pin to the database.
     *
     * @param pin the pin to be inserted
     * @return a future that returns the row of the inserted pin
     */
    public ListenableFuture<Long> addPin(Pin pin) {
        return pinRepository.insertPin(pin);
    }

    /**
     * Looks up if the given pin exists in the database.
     * For this, the value of the pin is used and not the id.
     *
     * @param pin the pin to be looked up
     * @return a future that returns whether the pin exists or not
     */
    public ListenableFuture<Boolean> doesPinExist(Pin pin) {
        return pinRepository.isValidPin(pin);
    }

    /**
     * Deletes a given pin.
     * For this, the value of the pin is used and not the id.
     * If multiple pins with this value exists, all will be deleted.
     *
     * @param pin the pin to be deleted
     * @return a future. true if the pin was deleted, false if the pin didn't exist
     */
    public ListenableFuture<Boolean> deletePin(Pin pin) {
        return pinRepository.deletePin(pin);
    }

    /**
     * Get the count of stored pins.
     *
     * @return a future that returns the pin count
     */
    public ListenableFuture<Integer> getPinCount() {
        return pinRepository.getPinCount();
    }
}
