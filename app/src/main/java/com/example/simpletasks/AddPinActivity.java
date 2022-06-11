package com.example.simpletasks;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entities.Pin;
import com.example.simpletasks.domain.settings.PinController;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

/**
 * Activity to add a new pin to the database and handle its sending process.
 */
public class AddPinActivity extends AppCompatActivity {
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static final String TAG = "AddPinActivity";
    private static final String regionCode = "CH";
    private static TextWatcher watcher = null;
    private PinController pinController;

    /**
     * Set and adjust the view and initialize firebase.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin);

        // Set listener to handle when the focus changes
        EditText phoneInputView = findViewById(R.id.phoneNumberInput);
        phoneInputView.setOnFocusChangeListener((v, isFocused) -> {
            if (!isFocused) {
                phoneInputView.addTextChangedListener(getInstantValidationTextWatcher());
                String phoneNumber = ((EditText) v).getText().toString();
                boolean isValid = isValidPhoneNumber(phoneNumber);
                showPhoneNumberError(!isValid);
            }
        });

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        pinController = new PinController(this.getApplication());
        Log.d(TAG, "finished initialisation");
    }

    /**
     * Handle click events for the generate pin button.
     * Sends or only shows the pin depending on the user's selection
     *
     * @param view the view whose click event was triggered
     */
    public void onGeneratePinClicked(View view) {
        Pin pin = Pin.random();
        Log.d(TAG, "pin generated");
        if (shouldSendPin()) {
            sendPin(pin);
        } else {
            showPin(pin);
        }
    }

    /**
     * Handle click events on the back button.
     *
     * @param view the view whose click event was triggered
     */
    public void onBackClicked(View view) {
        super.onBackPressed();
    }

    /**
     * Handle change of the radio button choice for sending pins.
     * Shows the phone number input if sending the pin is selected
     *
     * @param view the view whose click event was triggered
     */
    public void onSendChoiceChanged(View view) {
        View phoneNumberGroup = findViewById(R.id.phoneNumberGroup);
        if (shouldSendPin()) {
            phoneNumberGroup.setVisibility(View.VISIBLE);
        } else {
            phoneNumberGroup.setVisibility(View.GONE);
        }
    }

    // Check if the send pin radio button is checked
    private boolean shouldSendPin() {
        RadioButton sendPinRadio = findViewById(R.id.sendPinRadio);
        return sendPinRadio.isChecked();
    }

    // Show the pin on the screen
    private void showPin(Pin pin) {
        new DialogBuilder()
                .setDescriptionText(R.string.show_pin_popup, pin.getPin())
                .setContext(this)
                .setTwoButtonLayout(R.string.cancel_popup, R.string.accept_info_popup)
                .setAction(() -> {
                    pinController.addPin(pin);
                    Log.d(TAG, "showed pin locally");
                    finish();
                })
                .build().show();
    }

    // Use firebase to send the pin to the user
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
        Task<String> responseTask = pinController.sendPin(pin, phoneNumber, firebaseFunctions);
        Log.d(TAG, "sent pin, waiting for response");
        View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Handle completion of the sending process
        responseTask.addOnCompleteListener((s) -> {
            progressBar.setVisibility(View.INVISIBLE);
            Log.d(TAG, "response completed");
        });

        // Handle success when sending pins in firebase
        responseTask.addOnSuccessListener((s) -> {
            Log.d(TAG, "response successful");
            pinController.addPin(pin);
            Log.d(TAG, "pin saved");
            Log.d(TAG, "showing confirmation popup");
            final String formattedPhoneNumber = phoneNumberUtil.formatInOriginalFormat(phoneNumber, regionCode);
            showPinSentPopup(formattedPhoneNumber);
        });

        // Handle errors when sending pins in firebase
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

    // shows a popup confirming that the pin has been sent to the given phone number
    private void showPinSentPopup(String formattedPhoneNumber) {
        new DialogBuilder()
                .setDescriptionText(R.string.pin_sent_popup, formattedPhoneNumber)
                .setContext(this)
                .setCenterButtonLayout(R.string.accept_info_popup)
                .setAction(() -> {
                    Log.d(TAG, "user closed popup");
                    finish();
                })
                .build().show();
    }

    // Create TextWatcher to validate the users entered phone number
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

    // Validate a phone number string
    private boolean isValidPhoneNumber(String phoneNumber) {
        try {
            phoneNumberUtil.parse(phoneNumber, regionCode);
        } catch (NumberParseException e) {
            return false;
        }
        return true;
    }

    // Show the error if the phone number is invalid
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
}