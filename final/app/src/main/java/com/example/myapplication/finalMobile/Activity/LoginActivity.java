package com.example.myapplication.finalMobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.finalMobile.Fragments.HomeFragment;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Utils.Validator.EmailUtils;
import com.example.myapplication.finalMobile.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseConnectionMonitor.startMonitoring(getBaseContext());


        // inflate
        binding = ActivityLoginBinding.inflate(getLayoutInflater(), null, false);

        // render view
        setContentView(binding.getRoot());

        // handle redirect to page sign up
        binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


        // handle login
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }


    // handle login account
    private void handleLogin() {
        try {
            String email = binding.etEmail.getText().toString().trim();
            String pass = binding.etPass.getText().toString().trim();

            if (EmailUtils.isValidEmail(email)) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser != null) {
                                boolean isVerify = currentUser.isEmailVerified();
                                if (isVerify) {
                                    Toast.makeText(getBaseContext(), "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();

                                    currentUser.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                        @Override
                                        public void onSuccess(GetTokenResult getTokenResult) {
                                            if (getTokenResult.getClaims() != null) {
                                                Boolean adminClaim = (Boolean) getTokenResult.getClaims().get("admin");
                                                if (adminClaim != null && adminClaim) {
                                                    Log.i(TAG, adminClaim + "");

                                                    startActivity(new Intent(getBaseContext(), AdminMainActivity.class));

                                                } else {
                                                    Log.e(TAG, "Admin claim is null");
                                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                                }
                                            } else {
                                                Log.e(TAG, "Claims object is null");
                                            }
                                        }
                                    });

                                    binding.etPass.setText("");
                                    binding.etEmail.setText("");

                                    startActivity(new Intent(getBaseContext(), MainActivity.class));

                                } else {
                                    Toast.makeText(getBaseContext(), "Email chưa được xác thực.", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                }
                            }
                        } else {
                            binding.etPass.setText("");
                            Toast.makeText(getBaseContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Exception  : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
