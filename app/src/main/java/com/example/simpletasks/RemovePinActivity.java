package com.example.simpletasks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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