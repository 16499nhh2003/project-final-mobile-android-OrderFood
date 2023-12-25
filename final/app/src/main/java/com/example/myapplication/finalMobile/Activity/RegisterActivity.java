package com.example.myapplication.finalMobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.finalMobile.Model.User;
import com.example.myapplication.finalMobile.Utils.Validator.EmailUtils;
import com.example.myapplication.finalMobile.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = RegisterActivity.class.getSimpleName();
    private ActivitySignupBinding binding;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseConnectionMonitor.startMonitoring(getBaseContext());

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // handle sign up
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        // handle redirect page login
        binding.tvRedirectPageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /// Check if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            Log.i(TAG, currentUser.getUid());
        }
    }

    // handle sign up
    private void handleSignUp() {

        String etEmail = binding.etEmail.getText().toString().trim();
        String password = binding.etPass.getText().toString().trim();

        // check fields empty
        if (TextUtils.isEmpty(etEmail) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Please enter email , phone and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign up Firebase Authenticate
        try {
            String fullName = binding.etName.getText().toString().trim();

            User user = new User();
            user.setFullName(fullName);

            if (EmailUtils.isValidEmail(etEmail)) {
                mAuth.createUserWithEmailAndPassword(etEmail, password)
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Registration success
                                Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getBaseContext(), "Sent email Verify", Toast.LENGTH_SHORT).show();
                                                String uid = mAuth.getCurrentUser().getUid();
                                                user.setEmail(etEmail);
                                                updateProfileInDatabase(uid, user);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getBaseContext(), "Error : Registration " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed." + Objects.requireNonNull(task.getException()).getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } catch (
                Exception e) {
            Log.i(TAG, Objects.requireNonNull(e.getMessage()));
            Toast.makeText(RegisterActivity.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void updateProfileInDatabase(String uid, User user) {
        try {
            reference
                    .child("users")
                    .child(uid)
                    .setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                handler.postDelayed(() -> {
                                    clearField();
                                    Toast.makeText(getBaseContext(), "Tạo tài khoản thành công. Vui lòng xác nhận tài khoản qua gmail hoặc qua điện thoại", Toast.LENGTH_SHORT).show();
                                }, 2000);
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearField() {
        binding.etPass.setText("");
        binding.etName.setText("");
        binding.etPass.setText("");
        binding.etPassConfirm.setText("");
    }

}

