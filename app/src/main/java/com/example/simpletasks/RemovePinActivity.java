package com.example.simpletasks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class RemovePinActivity extends AppCompatActivity {

    private static final String TAG = "RemovePinActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_pin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Log.d(TAG, "finished initialisation");
    }

    public void onBackClicked(View view) {
        super.onBackPressed();
    }

    public void onRemovePinClicked(View view) {
        Log.d(TAG, "removing pin...");
    }
}