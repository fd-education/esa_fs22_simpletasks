package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void onAddPinClicked(View view) {
        Intent intent = new Intent(this, AddPinActivity.class);
        startActivity(intent);
    }


    public void onOpenDisplaySettingsClicked(View ignored) {
        Intent displaySettingsIntent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        startActivity(displaySettingsIntent);
    }

    public void onRemovePinClicked(View view) {
        Intent intent = new Intent(this, RemovePinActivity.class);
        startActivity(intent);
    }

    public void onBackClicked(View view) {
        super.onBackPressed();
    }


    public void onLogoutClicked(View view) {
        // TODO: implement when user info is ready
    }
}