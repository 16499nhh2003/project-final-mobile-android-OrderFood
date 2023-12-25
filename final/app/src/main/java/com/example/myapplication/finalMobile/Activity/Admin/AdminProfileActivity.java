package com.example.myapplication.finalMobile.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.finalMobile.R;


public class AdminProfileActivity extends AppCompatActivity {
    ImageButton btnBackMainScreen;
    TextView editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_admin_profile);
        btnBackMainScreen = findViewById(R.id.btnBackMainScreen);
        btnBackMainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfileActivity.this, MainScreenActivity.class);
                startActivity(intent);
            }
        });

        editProfile = findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfileActivity.this, EditAdminActivity.class);
                startActivity(intent);
            }
        });

    }
}