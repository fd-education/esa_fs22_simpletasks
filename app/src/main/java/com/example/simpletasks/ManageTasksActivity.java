package com.example.simpletasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.domain.login.User;

public class ManageTasksActivity extends AppCompatActivity {
    private static final String TAG = "ManageTaskActivity";
    public static final String CREATE_NEW_TASK = "create new task";
    private User user;

    /**
     * Set and adjust the view and the user instance
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tasks);

        // Initialize the user
        user = User.getUser();

        Log.d(TAG, "ManageTasks created. User Login State: " + user.isLoggedIn());
    }

    /**
     * handles the click event when the user wants to log out.
     *
     * @param view the view whose click event was triggered
     */
    public void onLogoutClicked(View view) {
        onBackPressed();
    }

    /**
     * Log the user out and go back to the main view.
     */
    @Override
    public void onBackPressed() {
        //asks if user really wants to logout
        new DialogBuilder()
                .setDescriptionText(R.string.logout_popup_text)
                .setContext(this)
                .setTwoButtonLayout(R.string.cancel_popup, R.string.logout_popup_button)
                .setAction(() -> {
                    //log user out
                    user.logOut();
                    Log.d(TAG, "Logout clicked. User Login State: " + user.isLoggedIn());
                    super.onBackPressed();
                }).build().show();

    }

    /**
     * Handle click events on the add task button
     *
     * @param view the view that triggered the event
     */
    public void onAddTaskClicked(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_KEY, MODE_PRIVATE);
        sharedPreferences.edit().putString(EditTaskActivity.SHARED_PREF_TASK_ID, CREATE_NEW_TASK).apply();

        Intent intent = new Intent(this, EditTaskActivity.class);
        startActivity(intent);
    }

    /**
     * Handle click events on the settings button
     *
     * @param view the view that triggered the event
     */
    public void onSettingsClicked(View view) {
        Log.d(TAG, "on settings button clicked, launching settings activity");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}