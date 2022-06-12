package com.example.simpletasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.domain.popups.DialogBuilder;
import com.example.simpletasks.domain.settings.PinController;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity for the main screen
 */
public class MainActivity extends AppCompatActivity {
    // Keys for the intent extras
    public static final String TASK_INTENT_EXTRA = "task_intent_extra";
    public static final String TASK_ID_INTENT_EXTRA = "task_id_intent_extra";
    public static final String SHARED_PREF_KEY = "SIMPLE_TASK_SHARED_PREF";
    public static final String SHOW_ADD_PIN_TIP = "SHOW_ADD_PIN_TIP";

    private static final String TAG = "MainActivity";
    private ExecutorService executorService;

    /**
     * Set and adjust the view
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        executorService = Executors.newFixedThreadPool(1);

        Log.d(TAG, "finished initialisation");
    }

    @Override
    protected void onDestroy() {
        executorService.shutdown();
        super.onDestroy();
    }

    /**
     * Handle click events of the login button
     *
     * @param view the view whose click event was triggered
     */
    public void onLoginClicked(@SuppressWarnings("unused") View view) {
        Futures.addCallback(new PinController(this).getPinCount(), new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer pinCount) {
                if (pinCount == 0) {
                    if (shouldShowAddPinTip()) {
                        runOnUiThread(() -> showAddPinTip());
                    } else {
                        startManageTaskActivity();
                    }
                } else {
                    startLoginActivity();
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Log.e(TAG, "checking pin count failed with error:");
                Log.e(TAG, t.getMessage());
                startLoginActivity();
            }
        }, executorService);
    }

    private void showAddPinTip() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(SHOW_ADD_PIN_TIP, false).apply();
        new DialogBuilder().setDescriptionText(R.string.no_pin_set_tip)
                .setTwoButtonLayout(R.string.cancel_popup, R.string.accept_info_popup)
                .setContext(this)
                .setAction(this::addPinTipClicked)
                .build().show();
    }

    private void addPinTipClicked() {
        Intent intent = new Intent(this, AddPinActivity.class);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startManageTaskActivity() {
        Intent intent = new Intent(this, ManageTasksActivity.class);
        startActivity(intent);
    }

    private boolean shouldShowAddPinTip() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHOW_ADD_PIN_TIP, true);
    }

    /**
     * Handle click events of the settings button
     *
     * @param view the view whose click event was triggered
     */
    public void onSettingsClicked(@SuppressWarnings("unused") View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}