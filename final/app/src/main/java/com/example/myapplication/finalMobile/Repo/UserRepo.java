package com.example.myapplication.finalMobile.Repo;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.finalMobile.Interface.UpdateInfoCallback;
import com.example.myapplication.finalMobile.Model.Order;
import com.example.myapplication.finalMobile.Model.User;
import com.example.myapplication.finalMobile.Service.ApiClient;
import com.example.myapplication.finalMobile.Service.ApiResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepo {
    private final static String TAG = UserRepo.class.getSimpleName();

    private static final String ORDER_PATH = "orders";
    private static final DatabaseReference ordersReference;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ordersReference = database.getReference(ORDER_PATH);
    }


    public static void updateInfoUser(String name, Uri uri, final UpdateInfoCallback callback) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder().setDisplayName(name);

        if (uri != null) {
            profileUpdatesBuilder.setPhotoUri(uri);
        }

        UserProfileChangeRequest profileUpdates = profileUpdatesBuilder.build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onUpdateSuccess();
                } else {
                    callback.onUpdateFailed(task.getException());
                }
            }
        });
    }

    public static void updatePhone(PhoneAuthCredential credential, final UpdateInfoCallback callback) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        user.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "update successfully");
                    callback.onUpdateSuccess();
                } else {
                    Log.i(TAG, "update fail");
                    callback.onUpdateFailed(task.getException());
                }
            }
        });
    }

    public static void updateUser(String userId, User updatedUser, final UpdateInfoCallback callback) {
        Call<User> call = ApiClient.getApiService().updateUser(userId, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onUpdateSuccess();
                } else {
                    Log.e("ApiError", "Error: " + response.message());
                    callback.onUpdateFailed(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("NetworkError", "Error: " + t.getMessage());
            }
        });
    }

    public static void getAllUser(final OnHandle onHandle) {
        Call<List<User>> call = ApiClient.getApiService().getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    onHandle.OnGetAll(users);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i(TAG, t.toString());
            }
        });
    }

    public static void deleteUser(String uid, final UpdateInfoCallback listener) {
        Call<ApiResponse> call = ApiClient.getApiService().deleteUser(uid);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.i(UserRepo.class.getSimpleName(), "success delete");
                    listener.onUpdateSuccess();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(UserRepo.class.getSimpleName(), t.toString());
                listener.onUpdateFailed(new Exception(t));
            }
        });
    }

    public static void findOrderByIdCustomer(String idCustomer, final OrderRepo.OrderCallback callback) {

        Query query = ordersReference.orderByChild("idCustomer").equalTo(idCustomer);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> orders = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    orders.add(order);
                }

                Collections.sort(orders, new Comparator<Order>() {
                    @Override
                    public int compare(Order o1, Order o2) {
                        return Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime());
                    }
                });

                callback.onOrdersLoaded(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onLoadFailed(error.toException());
            }
        });
    }


    public interface OnHandle {
        void OnGetAll(List<User> users);
    }
}
