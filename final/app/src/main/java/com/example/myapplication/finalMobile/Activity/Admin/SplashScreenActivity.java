package com.example.myapplication.finalMobile.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.finalMobile.R;


public class SplashScreenActivity extends AppCompatActivity {
    private ConstraintLayout splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_splash_screen);

        splashScreen = findViewById(R.id.spashScreen);

        splashScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreenActivity.this, MainScreenActivity.class);
                startActivity(intent);

            }
        });
    }
}