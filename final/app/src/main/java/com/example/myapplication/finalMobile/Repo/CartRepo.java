package com.example.myapplication.finalMobile.Repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Interface.OnCartInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartRepo {
    private static final String TAG = CartRepo.class.getSimpleName();
    private static final String CART_PATH = "carts";
    private static DatabaseReference cartReference;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        cartReference = database.getReference(CART_PATH);
    }
    public static void addToCart(CartItem cartItem, String uid, String idProduct) {
        cartReference
                .child(uid)
                .child(idProduct)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    CartItem existingCartItem = dataSnapshot.getValue(CartItem.class);

                    // Increment the quantity
                    int newQuantity = existingCartItem.getQuantity() + cartItem.getQuantity();
                    existingCartItem.setQuantity(newQuantity);

                    // Update the existing entry in the cart
                    cartReference
                            .child(uid)
                            .child(idProduct).setValue(existingCartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "Quantity updated successfully");
                            } else {
                                Log.i(TAG, "Error updating quantity: " + task.getException().getMessage());
                            }
                        }
                    });
                } else {
                    // Product does not exist in the cart, add a new entry
                    cartReference
                            .child(uid)
                            .child(idProduct).setValue(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "Product added to cart successfully");
                            } else {
                                Log.i(TAG, "Error adding to cart: " + task.getException().getMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "DatabaseError: " + error.getMessage());
            }
        });
    }

    public static void findCartByUid(String uid, final OnCartInterface listener) {
        cartReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CartItem> cartItemList = new ArrayList<CartItem>();
                for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = cartItemSnapshot.getValue(CartItem.class);
                    cartItemList.add(cartItem);
                }
                listener.onCartFound(cartItemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteItemInCart(String uid, String idProduct, final HandleCart listener) {
        Query query = cartReference.child(uid).orderByKey().equalTo(idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue();
                }
                listener.onDeleteProduct();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void clearCart(String uid) {
        cartReference.child(uid)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Cart cleared successfully");
                        } else {
                            Log.e(TAG, "Failed to clear cart");
                        }
                    }
                });
    }

    public interface HandleCart {
        void onDeleteProduct();
    }
}
