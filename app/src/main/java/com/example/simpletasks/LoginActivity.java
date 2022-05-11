package com.example.simpletasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.simpletasks.domain.login.Login;
import com.example.simpletasks.domain.login.LoginController;
import com.example.simpletasks.domain.user.User;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivity";

    User user;
    Login login;
    int attemptsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        user = User.getUser();
        login = new LoginController(this.getApplication());
    }

    public void onLoginClicked(View v){
        String pin = ((TextView) findViewById(R.id.pinPasswordInput)).getText().toString();
        TextView errorText = ((TextView) findViewById(R.id.loginError));

        boolean validLogin = login.isValidPin(pin);

        if(validLogin){
            Log.d(TAG, "Pin is valid.");
            user.logIn();
            errorText.setText("");
            Intent intent = new Intent(this, ManageTaskActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "Pin is not valid.");
            errorText.setText(getString(R.string.login_error));
        }
    }
}