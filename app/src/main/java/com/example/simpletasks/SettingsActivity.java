package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.domain.login.User;
import com.example.simpletasks.domain.settings.PinController;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity for the settings screen.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private final User user = User.getUser();
    private ExecutorService executorService;

    /**
     * Set and adjust the view.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        executorService = Executors.newSingleThreadExecutor();

        Log.d(TAG, "finished initialisation");
    }

    /**
     * Disables the add and remove pin buttons if a pin is set, but the user is not logged in and
     * disables the remove pin button if no pin is set.
     */
    @Override
    protected void onResume() {
        super.onResume();
        PinController pinController = new PinController(this.getApplication());
        Futures.addCallback(pinController.getPinCount(), new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer pinCount) {
                runOnUiThread(() -> checkShowButtons(pinCount));
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Log.e(TAG, "pin count could not be loaded, disabling remove pin button. Error was:");
                Log.e(TAG, t.getMessage());
                runOnUiThread(() -> checkShowButtons(0));
            }
        }, executorService);

        if (!user.isLoggedIn()) {
            final View logoutButton = findViewById(R.id.logoutButton);
            logoutButton.setVisibility(View.GONE);
        }
    }

    // Enables or disables the button by setting the alpha value / opacity
    private void setButtonEnabled(View button, boolean enable) {
        if (enable) {
            button.setAlpha(1f);
            button.setClickable(true);
        } else {
            button.setAlpha(0.5f);
            button.setClickable(false);
        }
    }

    // Enables or disables the add pin and remove pin buttons
    private void checkShowButtons(Integer pinCount) {
        final View removePinButtonView = findViewById(R.id.removePinButton);
        final View addPinButtonView = findViewById(R.id.addPinButton);
        final View pinRemoveDeactivatedTextView = findViewById(R.id.pinRemoveButtonDeactivatedText);
        final View buttonsDeactivatedTextView = findViewById(R.id.buttonsDeactivatedText);

        // reset all buttons
        pinRemoveDeactivatedTextView.setVisibility(View.GONE);
        buttonsDeactivatedTextView.setVisibility(View.GONE);
        setButtonEnabled(removePinButtonView, true);
        setButtonEnabled(addPinButtonView, true);

        Log.d(TAG, "checking pin count");
        if (pinCount == 0) {
            Log.d(TAG, "no pins are currently set");
            pinRemoveDeactivatedTextView.setVisibility(View.VISIBLE);
            setButtonEnabled(removePinButtonView, false);
        } else {
            Log.d(TAG, "checking if user is logged in");
            if (!user.isLoggedIn()) {
                Log.d(TAG, "user is not logged in");
                buttonsDeactivatedTextView.setVisibility(View.VISIBLE);
                setButtonEnabled(removePinButtonView, false);
                setButtonEnabled(addPinButtonView, false);
            }
        }
    }

    /**
     * Handle click events on the add pin button.
     * Start the add pin activity.
     *
     * @param view the view that triggered the event
     */
    public void onAddPinClicked(View view) {
        Intent intent = new Intent(this, AddPinActivity.class);
        startActivity(intent);
    }

    /**
     * Handle click events on the remove pin button.
     * Start the remove pin activity.
     *
     * @param view the view that triggered the event
     */
    public void onRemovePinClicked(View view) {
        Intent intent = new Intent(this, RemovePinActivity.class);
        startActivity(intent);
    }

    /**
     * Handle click events on the save button.
     * Forward to the phone settings.
     *
     * @param view the view that triggered the event
     */
    public void onOpenDisplaySettingsClicked(View view) {
        Intent displaySettingsIntent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        startActivity(displaySettingsIntent);
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
     * Handle click events on the logout button
     *
     * @param view the view that triggered the event
     */
    public void onLogoutClicked(View view) {
        user.logOut();
        finish();
    }

    /**
     * Handle click event on the open language settings button.
     * Opens the system settings for localisation / language.
     *
     * @param view the view that triggered the event
     */
    public void onOpenLanguageSettingsButton(View view) {
        Intent languageSettingsIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(languageSettingsIntent);
    }

    /**
     * Shuts down the executor service after the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}