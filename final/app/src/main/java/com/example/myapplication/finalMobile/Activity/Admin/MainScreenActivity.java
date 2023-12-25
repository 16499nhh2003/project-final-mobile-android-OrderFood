package com.example.myapplication.finalMobile.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.finalMobile.R;


public class MainScreenActivity extends AppCompatActivity {
    private ConstraintLayout addMenu, allItemMenu, delivery, profileAdmin;
    TextView pendingOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_main_screen);

        pendingOrder = findViewById(R.id.pendingOrder);
        addMenu = findViewById(R.id.addMenu);
        allItemMenu = findViewById(R.id.allItemMenu);
        delivery = findViewById(R.id.delivery);
        profileAdmin = findViewById(R.id.profileAdmin);
        pendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, null);
                startActivity(intent);
            }
        });
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, null);
                startActivity(intent);
            }
        });

        allItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, null);
                startActivity(intent);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, null);
                startActivity(intent);
            }
        });
        profileAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, AdminProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}