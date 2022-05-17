package com.example.simpletasks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class RemovePinActivity extends AppCompatActivity {

    private static final String TAG = "RemovePinActivity";

    /**
     * Set and adjust the view and the controllers
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_pin);

        // Remove the action bar at the top of the screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

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
        Toast.makeText(this, "remove pin clicked", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "removing pin...");
    }
}