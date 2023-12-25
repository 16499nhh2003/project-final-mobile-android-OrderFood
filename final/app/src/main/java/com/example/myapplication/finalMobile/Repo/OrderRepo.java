package com.example.myapplication.finalMobile.Repo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.finalMobile.Enum.OrderStatus;
import com.example.myapplication.finalMobile.Interface.OnFindOrderListener;
import com.example.myapplication.finalMobile.Interface.OnListerOrder;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.PUT;

public class OrderRepo {
    private static final String TAG = OrderRepo.class.getSimpleName();
    private static final String ORDER_PATH = "orders";
    private static final DatabaseReference ordersReference;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ordersReference = database.getReference(ORDER_PATH);
    }
    public static void createOrder(Order order, final OnListerOrder listerOrder) {
        String orderId = ordersReference.push().getKey();
        if (orderId != null) {
            order.setIdOrder(orderId);
            ordersReference.child(orderId).setValue(order)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "create order : " + orderId + "Successfully");
                                listerOrder.OnCreateSuccess(orderId);
                            } else {
                                Log.i(TAG, "create order fail.");
                            }
                        }
                    });
        }
    }
    public static void findOrderById(String id, final OnFindOrderListener listener) {
        ordersReference.child(id).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Order order = task.getResult().getValue(Order.class);
                            if (order != null) {
                                Log.i(TAG, "Found order by id: " + order.toString());
                                listener.onFindOrderSuccess(order);
                            } else {
                                Log.i(TAG, "Order not found for id: " + id);
                                listener.onFindOrderFailure("Order not found");
                            }
                        }
                    }
                });
    }
    public static void getAllOrder(final OrderCallback orderCallback) {
        Query query = ordersReference.orderByChild("createdAt");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> orders = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    orders.add(order);
                }
                orderCallback.onOrdersLoaded(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                orderCallback.onLoadFailed(databaseError.toException());
            }
        });
    }
    public static void updateStatusOrder(String id, OrderStatus orderStatus, final OrderUpdateCallback callback) {
        Log.i(TAG, "updateStatusOrder" + "id : " + id);
        Query query = ordersReference.orderByChild("idOrder").equalTo(id);
        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(TAG, "onChildAdded");
                Order order = snapshot.getValue(Order.class);
                if (order != null) {
                    order.setOrderStatus(orderStatus);
                    ordersReference.child(snapshot.getKey()).setValue(order)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if (callback != null) {
                                        callback.onOrderUpdated(order);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, e.toString());
                                    callback.onOrderUpdateFailed(e);
                                }
                            });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(TAG, "onChildChanged");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());

            }
        });
    }

    public interface OrderCallback {
        void onOrdersLoaded(List<Order> orders);

        void onLoadFailed(Exception e);
    }

    public interface OrderUpdateCallback {
        void onOrderUpdated(Order updatedOrder);

        void onOrderUpdateFailed(Exception e);

    }
}
