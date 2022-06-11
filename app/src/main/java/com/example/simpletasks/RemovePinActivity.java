package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.entities.Pin;
import com.example.simpletasks.domain.settings.PinController;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;

public class RemovePinActivity extends AppCompatActivity {

    private static final String TAG = "RemovePinActivity";
    private final PinController pinController;
    private final ExecutorService executorService;

    public RemovePinActivity() {
        pinController = new PinController(this.getApplication());
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Set and adjust the view and the controllers
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_pin);

        final TextView introWarningView = findViewById(R.id.delete_pin_intro_warning);
        Futures.addCallback(pinController.getPinCount(), new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer count) {
                if (count == 1) {
                    introWarningView.setText(R.string.remove_pin_intro_warning_last_pin);
                } else {
                    introWarningView.setText(R.string.remove_pin_intro_warning_more_pins);
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Log.e(TAG, "pin count could not be loaded, not showing warning. Error was:");
                Log.e(TAG, t.getMessage());
                introWarningView.setVisibility(View.GONE);
            }
        }, executorService);

        Log.d(TAG, "finished initialisation");
    }

    /**
     * Handle click events on the back button
     *
     * @param view the view that triggered the event
     */
    public void onBackClicked(View view) {
        super.onBackPressed();
    }

    /**
     * Handle click events on the remove pin button
     *
     * @param view the view that triggered the event
     */
    public void onRemovePinClicked(View view) {
        Log.d(TAG, "removePin clicked");
        clearPinError();
        final EditText pinInputView = findViewById(R.id.pin_input);

        final String text = pinInputView.getText().toString();

        Log.d(TAG, "validating pin");
        Pin pin;
        try {
            pin = new Pin(text);
        } catch (IllegalArgumentException ignored) {
            showPinError(getString(R.string.invalid_pin));
            Log.d(TAG, "pin was not valid");
            return;
        }
        Log.d(TAG, "pin was valid");

        Log.d(TAG, "checking if pin exists");
        final FutureCallback<Boolean> deletePinCallback = new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean pinWasDeleted) {
                if (Boolean.TRUE.equals(pinWasDeleted)) {
                    Log.d(TAG, "pin existed and was deleted");
                    Log.d(TAG, "pin deleted");
                    finish();
                    // TODO: Add popup
                } else {
                    Log.d(TAG, "pin does not exist");
                    runOnUiThread(() -> showPinError(getString(R.string.pin_not_found)));
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Log.e(TAG, "deleting pin failed with error:");
                Log.e(TAG, t.getMessage());
                runOnUiThread(() -> showPinError(getString(R.string.unknown_error_msg)));
            }
        };

        final ListenableFuture<Boolean> doesPinExistFuture = pinController.deletePin(pin);
        Futures.addCallback(doesPinExistFuture, deletePinCallback, executorService);
    }

    // hide the error message for the pin input
    private void clearPinError() {
        final View errorView = findViewById(R.id.pin_error);

        errorView.setVisibility(View.GONE);
    }

    // sets the error text for the pin input and shows it
    private void showPinError(String string) {
        final TextView errorView = findViewById(R.id.pin_error);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(string);
    }

    /**
     * Shuts down the executor service when the user exits the activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}