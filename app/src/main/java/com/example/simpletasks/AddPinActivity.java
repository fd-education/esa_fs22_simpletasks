package com.example.simpletasks;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.Pin;
import com.example.simpletasks.data.viewmodels.PinViewModel;
import com.example.simpletasks.domain.settings.AddPinController;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class AddPinActivity extends AppCompatActivity {
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static final String TAG = "AddPinActivity";
    private static final String regionCode = "CH";
    private static TextWatcher watcher = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        EditText phoneInputView = findViewById(R.id.phoneNumberInput);
        phoneInputView.setOnFocusChangeListener((v, isFocused) -> {
            if (!isFocused) {
                phoneInputView.addTextChangedListener(getInstantValidationTextWatcher());
                String phoneNumber = ((EditText) v).getText().toString();
                boolean isValid = isValidPhoneNumber(phoneNumber);
                showPhoneNumberError(!isValid);
            }
        });

        FirebaseApp.initializeApp(this);
    }

    private boolean shouldSendPin() {
        RadioButton sendPinRadio = findViewById(R.id.sendPinRadio);
        return sendPinRadio.isChecked();
    }

    public void onGeneratePinClicked(View view) {
        Pin pin = Pin.random();
        Log.d(TAG, "pin generated");
        if (shouldSendPin()) {
            sendPin(pin);
        } else {
            showPin(pin);
        }

    }

    private void showPin(Pin pin) {
        // TODO: When popup are ready
        savePin(pin);
        Log.d(TAG, "showed pin locally");
    }

    private void savePin(Pin pin) {
        PinViewModel pinViewModel = new ViewModelProvider(this).get(PinViewModel.class);
        pinViewModel.insertPin(pin);
    }

    private void sendPin(Pin pin) {
        Log.d(TAG, "validating pin");
        showPhoneNumberError(false);
        EditText phoneInputView = findViewById(R.id.phoneNumberInput);
        String phoneNumberInput = phoneInputView.getText().toString();
        PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneNumberUtil.parseAndKeepRawInput(phoneNumberInput, regionCode);
        } catch (NumberParseException e) {
            showPhoneNumberError(true);
            phoneInputView.addTextChangedListener(getInstantValidationTextWatcher());
            Log.d(TAG, "phone number was invalid");
            return;
        }

        View sendErrorView = findViewById(R.id.sendPinError);
        sendErrorView.setVisibility(View.GONE);
        FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
        Task<String> responseTask = AddPinController.sendPin(pin, phoneNumber, firebaseFunctions);
        Log.d(TAG, "sent pin, waiting for response");
        View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        responseTask.addOnCompleteListener((s) -> {
            progressBar.setVisibility(View.INVISIBLE);
            Log.d(TAG, "response completed");
        });
        responseTask.addOnSuccessListener((s) -> {
            // TODO: Show when popup is ready
            savePin(pin);
            Log.d(TAG, "response successful");
            finish();
        });
        responseTask.addOnFailureListener(e -> {
            sendErrorView.setVisibility(View.VISIBLE);
            if (e instanceof FirebaseFunctionsException) {
                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                FirebaseFunctionsException.Code code = ffe.getCode();
                Log.e(TAG, "sending pin failed with FirebaseFunctionsException:");
                Log.e(TAG, code.toString());
                Log.e(TAG, ffe.getMessage());
            } else {
                Log.e(TAG, "sending pin failed with generic exception:");
                Log.e(TAG, e.getMessage());
            }
        });
    }

    @NonNull
    private TextWatcher getInstantValidationTextWatcher() {
        if (watcher == null) {
            watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    boolean isValid = isValidPhoneNumber(editable.toString());
                    showPhoneNumberError(!isValid);
                }
            };
        }
        return watcher;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        try {
            phoneNumberUtil.parse(phoneNumber, regionCode);
        } catch (NumberParseException e) {
            return false;
        }
        return true;
    }

    private void showPhoneNumberError(boolean showError) {
        View phoneNumberErrorView = findViewById(R.id.phoneNumberError);
        int visibility;
        if (showError) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.GONE;
        }
        phoneNumberErrorView.setVisibility(visibility);
    }

    public void onBackClicked(View view) {
        super.onBackPressed();
    }

    public void onSendChoiceChanged(View view) {
        View phoneNumberGroup = findViewById(R.id.phoneNumberGroup);
        if (shouldSendPin()) {
            phoneNumberGroup.setVisibility(View.VISIBLE);
        } else {
            phoneNumberGroup.setVisibility(View.GONE);
        }
    }
}