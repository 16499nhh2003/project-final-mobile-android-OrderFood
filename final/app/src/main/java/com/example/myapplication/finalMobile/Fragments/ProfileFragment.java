package com.example.myapplication.finalMobile.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.finalMobile.Activity.LoginActivity;
import com.example.myapplication.finalMobile.Activity.MainActivity;
import com.example.myapplication.finalMobile.Interface.UpdateInfoCallback;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.UserRepo;
import com.example.myapplication.finalMobile.Utils.Contrainst.Contraints;
import com.example.myapplication.finalMobile.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    ImageButton notificationButton;
    FragmentProfileBinding binding;
    FirebaseUser user;
    MainActivity mainActivity;

    TextView name, email, phone;
    ImageView imageView;

    Uri uri;
    private ProgressDialog progressDialog;
    String mVerificationId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUi();
        progressDialog = new ProgressDialog(getActivity());
        showInfo();

        mainActivity = (MainActivity) getActivity();

        binding.avartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity == null) {
                    return;
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    mainActivity.openGallery();
                    return;
                }
                if (mainActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    mainActivity.openGallery();
                } else {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    mainActivity.requestPermissions(permissions, Contraints.MY_REQUEST_CODE);
                }
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating ...");
                progressDialog.show();
                String nameU = name.getText().toString().trim();
                String phoneU = phone.getText().toString().trim();

                UserRepo.updateInfoUser(nameU, uri, new UpdateInfoCallback() {
                    @Override
                    public void onUpdateSuccess() {
                        progressDialog.hide();

                        // update phone
                        if (!phoneU.equalsIgnoreCase(user.getPhoneNumber())) {
                            showNotificationDialog();
                        }

                        mainActivity.showUserInformation();
                    }

                    @Override
                    public void onUpdateFailed(Exception e) {

                    }
                });
            }
        });
    }

    private void initializeUi() {
        name = binding.name;
        email = binding.email;
        phone = binding.phone;
        imageView = binding.avartar;
    }

    private void showInfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Glide.with(getContext()).load(user.getPhotoUrl()).error(R.drawable.user).into(binding.avartar);

            if (user.getDisplayName() == null || user.getDisplayName().isEmpty()) {
                binding.name.setHint("No have information");
            } else {
                binding.name.setText(user.getDisplayName());
            }
            binding.email.setText(user.getEmail());

            if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
                binding.name.setHint("No have information");
            } else {
                binding.phone.setText(user.getPhoneNumber());
            }

        } else {
            Log.e(TAG, "User is null in showInfo()");
        }
    }

    private void showNotificationDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mainActivity);
        bottomSheetDialog.setContentView(R.layout.activity_otp);

        String phoneNumber = phone.getText().toString().trim();
        phoneNumber = phoneNumber.replaceFirst("^0+", "");
        String formattedPhoneNumber = "+84" + phoneNumber;

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder()
                        .setPhoneNumber(formattedPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Log.i(TAG, "get success");
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.i(TAG, e.getMessage());

                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                super.onCodeSent(verificationId, token);
                                Log.d(TAG, "onCodeSent:" + verificationId);
                                mVerificationId = verificationId;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        bottomSheetDialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String otp = "";
                    EditText n1 = bottomSheetDialog.findViewById(R.id.otp_edit_text1);
                    EditText n2 = bottomSheetDialog.findViewById(R.id.otp_edit_text2);
                    EditText n3 = bottomSheetDialog.findViewById(R.id.otp_edit_text3);
                    EditText n4 = bottomSheetDialog.findViewById(R.id.otp_edit_text4);
                    EditText n5 = bottomSheetDialog.findViewById(R.id.otp_edit_text5);
                    EditText n6 = bottomSheetDialog.findViewById(R.id.otp_edit_text6);

                    if (n1 != null && n2 != null && n3 != null && n4 != null && n5 != null && n6 != null) {
                        otp = n1.getText().toString() +
                                n2.getText().toString() +
                                n3.getText().toString() +
                                n4.getText().toString() +
                                n5.getText().toString() +
                                n6.getText().toString();
                    }

                    Log.i(TAG, "mVerificationId: " + mVerificationId + " , otp:" + otp);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    UserRepo.updatePhone(credential, new UpdateInfoCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            bottomSheetDialog.hide();
                        }

                        @Override
                        public void onUpdateFailed(Exception e) {
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(getContext(), "OTP invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bottomSheetDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String email = user.getEmail();
            Log.i(TAG, "User ID: " + uid + ", Email: " + email);
        } else {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }

    public void setUri(Uri uri) {
        Glide.with(mainActivity).load(uri).error(R.drawable.user).into(binding.avartar);
        this.uri = uri;
    }

}