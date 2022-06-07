package com.example.simpletasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for the main screen
 */
public class MainActivity extends AppCompatActivity {
    // Keys for the intent extras
    public static final String TASK_INTENT_EXTRA = "task_intent_extra";
    public static final String TASK_ID_INTENT_EXTRA = "task_id_intent_extra";
    public static final String SHARED_PREF_KEY = "SIMPLE_TASK_SHARED_PREF";

    private static final String TAG = "MainActivity";

    /**
     * Set and adjust the view and the life cycle owner
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "finished initialisation");
    }

    /**
     * Handle click events of the login button
     *
     * @param view the view whose click event was triggered
     */
    public void onLoginClicked(View view) {
//        Intent intent = new Intent(this, LoginActivity.class);
        Intent intent = new Intent(this, ManageTasksActivity.class);
        startActivity(intent);
    }

    /**
     * Handle click events of the settings button
     *
     * @param view the view whose click event was triggered
     */
    public void onSettingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}