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

public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivity";

    User user;
    Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = User.getUser();
        login = new LoginController(this.getApplication());
    }

    public void onLoginClicked(View v){
        String pin = ((TextView) findViewById(R.id.pinPasswordInput)).getText().toString();

        boolean validLogin = login.isValidPin(pin);

        if(validLogin){
            Log.d(TAG, "Pin is valid.");
            User.getUser().logIn();
            Intent intent = new Intent(this, ManageTaskActivity.class);
            startActivity(intent);
        } else {
            Log.d(TAG, "Pin is not valid.");
            ((TextView) findViewById(R.id.loginError)).setText(getString(R.string.login_error, 2));
        }
    }
}