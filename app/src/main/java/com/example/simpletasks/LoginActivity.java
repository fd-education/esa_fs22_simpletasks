package com.example.simpletasks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.simpletasks.domain.login.*;

/**
 * LoginActivity handles lifecycle and business calls for the caretaker login screen.
 */
public class LoginActivity extends AppCompatActivity {
    final String TAG = "LoginActivity";

    Login login;

    /**
     * Set and adjust the view and the controllers
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Remove the action bar at the top of the screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialize the login controller
        login = new LoginController(this.getApplication());
        Log.d(TAG, "finished initialisation");
    }

    /**
     * Validate the pin that was entered to authenticate the user.
     *
     * @param v the view from which the event was triggered
     */
    public void onLoginClicked(View v){
        // Access the required views
        String pin = ((TextView) findViewById(R.id.pinPasswordInput)).getText().toString();
        TextView errorText = ((TextView) findViewById(R.id.loginError));

        // Check if the entered pin is valid
        boolean validLogin = login.isValidPin(pin);

        // Handle the validity of the login pin
        if(validLogin){
            errorText.setText("");
            User.getUser().logIn();
            startActivity(new Intent(this, ManageTasksActivity.class));

            // Finish to remove the LoginActivity from the back stack
            finish();
            Log.d(TAG, "Pin is valid.");
        } else {
            errorText.setText(getString(R.string.login_error));
            Log.d(TAG, "Pin is not valid.");
        }
    }

    /**
     * Handle click events on the back button
     *
     * @param view the view that triggered the event
     */
    public void onBackClicked(View view) {
        super.onBackPressed();
    }
}