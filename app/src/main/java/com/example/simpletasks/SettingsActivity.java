package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for the settings screen.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    /**
     * Set and adjust the view.
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Log.d(TAG, "finished initialisation");
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
        // TODO: implement when user info is ready
    }
}