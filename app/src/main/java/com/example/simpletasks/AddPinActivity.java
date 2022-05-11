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

import com.example.simpletasks.data.entity.Pin;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class AddPinActivity extends AppCompatActivity {
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static final String TAG = "AddPinActivity";
    private static final String regionCode = "CH";
    private static TextWatcher watcher = null;

    static public Pin createRandomPIN() {
        Random random = new Random();
        int randomInt = random.nextInt(10_000);
        return new Pin(randomInt);
    }

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
            if (isFocused) {
                showPhoneNumberError(false);
            } else {
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
        Pin pin = createRandomPIN();
        Log.d(TAG, "pin generated");
        if (shouldSendPin()) {
            sendPin(pin);
        } else {
            showPin();
        }

    }

    private void showPin() {
        // TODO: When popup are ready
        Log.d(TAG, "showed pin locally");
    }

    private void sendPin(Pin pin) {
        Log.d(TAG, "validating pin");
        showPhoneNumberError(false);
        EditText phoneInputView = findViewById(R.id.phoneNumberInput);
        String phoneNumber = phoneInputView.getText().toString();
        if (!isValidPhoneNumber(phoneNumber)) {
            showPhoneNumberError(true);
            phoneInputView.addTextChangedListener(getInstantValidationTextWatcher());
            Log.d(TAG, "phone number was invalid");
            return;
        }

        View sendErrorView = findViewById(R.id.sendPinError);
        sendErrorView.setVisibility(View.GONE);

        Task<String> responseTask = firebaseSendPin(pin, phoneNumber);
        Log.d(TAG, "sent pin, waiting for response");
        View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        responseTask.addOnCompleteListener((s) -> {
            progressBar.setVisibility(View.INVISIBLE);
            Log.d(TAG, "response completed");
        });
        responseTask.addOnSuccessListener((s) -> {
            // TODO: Show when popup is ready
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

    private Task<String> firebaseSendPin(Pin pin, String phoneNumber) {
        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance(FirebaseApp.getInstance());
        Map<String, Object> data = new HashMap<>();
        data.put("phoneNumber", phoneNumber);
        data.put("pin", String.format(Locale.getDefault(), "%06d", pin.getPin()));

        return mFunctions
                .getHttpsCallable("sendPin")
                .call(data)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
                    return (String) task.getResult().getData();
                });
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