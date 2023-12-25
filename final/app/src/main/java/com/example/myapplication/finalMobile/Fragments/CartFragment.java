package com.example.myapplication.finalMobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.finalMobile.Activity.LoginActivity;
import com.example.myapplication.finalMobile.Adapter.CartAdapter;
import com.example.myapplication.finalMobile.Interface.OnClickedCartItem;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.CartRepo;
import com.example.myapplication.finalMobile.Interface.OnCartInterface;
import com.example.myapplication.finalMobile.databinding.FragmentCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
    private static final String TAG = CartFragment.class.getSimpleName();
    FragmentCartBinding fragmentCartBinding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String uid;

    RecyclerView rcv;
    CartAdapter cartAdapter;
    List<CartItem> dataSources;
    ImageView imageView;

    public CartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false);
        return fragmentCartBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }

        uid = user.getUid();

        imageView = fragmentCartBinding.imageCartEmpty;
        dataSources = new ArrayList<CartItem>();
        cartAdapter = new CartAdapter(new OnClickedCartItem() {
            @Override
            public void OnDeleteItem(List<CartItem> cartItemList, int position) {
                Log.i(TAG, "clicked item prepare delete cart item successfully at : " + position);

                CartItem cartItem = cartItemList.get(position);
                CartRepo cartRepo = new CartRepo();

                cartRepo.deleteItemInCart(uid, cartItem.getProductId(), new CartRepo.HandleCart() {
                    @Override
                    public void onDeleteProduct() {
                        Log.i(TAG, "delete cart item successfully");
                        cartItemList.remove(position);
                        cartAdapter.setDatasource(cartItemList);
                        updateTotalPrice(cartItemList);
                    }
                });

            }
        });
        cartAdapter.setDatasource(dataSources);


        rcv = fragmentCartBinding.rcvCart;
        rcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcv.setAdapter(cartAdapter);

        // find cart
        CartRepo.findCartByUid(uid, new OnCartInterface() {
            @Override
            public void onCartFound(List<CartItem> cartItemList) {
                dataSources.clear();
                if (!cartItemList.isEmpty()) {
                    imageView.setVisibility(View.GONE);
                    Log.i(TAG, cartItemList.toString());
                    cartAdapter.setDatasource(cartItemList);
                    updateTotalPrice(cartItemList);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });

        // handle check out
        fragmentCartBinding.btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<CartItem> cartItemList = cartAdapter.getDatasource();

                if (cartItemList.size() == 0) {
                    Toast.makeText(getContext(), "Cart empty ? ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.fragment_container, new CheckoutFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    private void updateTotalPrice(List<CartItem> cartItemList) {
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItemList) {
            totalPrice += cartItem.getPrice() * cartItem.getQuantity();
        }
        fragmentCartBinding.totalPriceordercart.setText(String.valueOf(totalPrice));
    }
}