package com.example.myapplication.finalMobile.Fragments.Admin.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.finalMobile.Adapter.Admin.UserAdapter;
import com.example.myapplication.finalMobile.Interface.UpdateInfoCallback;
import com.example.myapplication.finalMobile.Model.User;
import com.example.myapplication.finalMobile.Repo.UserRepo;
import com.example.myapplication.finalMobile.databinding.DialogEditUserBinding;
import com.example.myapplication.finalMobile.databinding.FragmentListUserBinding;

import java.util.ArrayList;
import java.util.List;


public class MainUserFragment extends Fragment implements UserAdapter.UserAdapterListener {
    private static final String TAG = MainUserFragment.class.getSimpleName();
    private RecyclerView rcv;
    private UserAdapter adapter;
    private List<User> dataSource;

    FragmentListUserBinding fragmentListUserBinding;
    private ProgressDialog progressDialog;

    public MainUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentListUserBinding = FragmentListUserBinding.inflate(inflater, container, false);
        return fragmentListUserBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());

        rcv = fragmentListUserBinding.recyclerView;
        dataSource = new ArrayList<User>();
        adapter = new UserAdapter(dataSource);
        adapter.setListener(this);

        if (getContext() != null) {
            rcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            rcv.setAdapter(adapter);
            rcv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
        fetchUser();
    }

    private void fetchUser() {
        Log.i(TAG, "fetchUser");

        progressDialog.setTitle("Fetching ...");
        progressDialog.show();

        this.dataSource.clear();
        UserRepo.getAllUser(new UserRepo.OnHandle() {
            @Override
            public void OnGetAll(List<User> users) {
                if (!users.isEmpty()) {
                    progressDialog.hide();
                    adapter.setDataSource(users);
                }
            }
        });

    }

    @Override
    public void onEditUserClicked(User user) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit User Details");

        DialogEditUserBinding binding = DialogEditUserBinding.inflate(getLayoutInflater());
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();

        EditText editTextFullName = binding.editTextFullName;
        EditText editTextEmail = binding.editTextEmail;
        EditText editTextPhone = binding.editTextPhone;
        Button buttonSave = binding.buttonSave;
        Button buttonCancel = binding.buttonCancel;

        editTextFullName.setText(user.getDisplayName());
        editTextEmail.setText(user.getEmail());
        editTextPhone.setText(user.getPhoneNumber());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = user.getUid();
                Log.i(TAG, uid);
                String fullName = editTextFullName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.setDisplayName(fullName);
                user.setEmail(email);
                user.setPhoneNumber(phone);

                UserRepo.updateUser(uid, user, new UpdateInfoCallback() {
                    @Override
                    public void onUpdateSuccess() {
                        progressDialog.hide();
                        fetchUser();
                        dialog.dismiss();
                        Log.i(TAG, "UPDATE SUCCESS");
                        Toast.makeText(getContext(), "Update Success", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onUpdateFailed(Exception e) {
                        progressDialog.hide();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void OnDeleteUser(User user) {
        Log.i(TAG, "Button delete clicked" + user.getUid());
        UserRepo.deleteUser(user.getUid(), new UpdateInfoCallback() {
            @Override
            public void onUpdateSuccess() {
                Log.e(TAG, "User deleted successfully");
                fetchUser();
                Toast.makeText(getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateFailed(Exception e) {
                Log.e(TAG, e.toString());
            }
        });

    }
}