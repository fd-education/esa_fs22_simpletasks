package com.example.simpletasks;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button popupBTN;
    Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popupBTN = findViewById(R.id.popupBTN);
        mDialog = new Dialog(this);

        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.popup);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void onSkipClicked(View view) {
        //TODO
    }

    public void onLoginClicked(View view) {
        //TODO
    }

    public void onSettingsClicked(View view) {
        //TODO
    }
}