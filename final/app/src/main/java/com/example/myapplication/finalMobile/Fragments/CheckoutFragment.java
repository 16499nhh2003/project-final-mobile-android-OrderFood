package com.example.myapplication.finalMobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.finalMobile.Activity.LoginActivity;
import com.example.myapplication.finalMobile.Interface.OnListerOrder;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Order;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.CartRepo;
import com.example.myapplication.finalMobile.Interface.OnCartInterface;
import com.example.myapplication.finalMobile.Repo.OrderRepo;
import com.example.myapplication.finalMobile.databinding.FragmentCheckoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.List;

public class CheckoutFragment extends Fragment {
    private static final String TAG = CheckoutFragment.class.getSimpleName();
    FragmentCheckoutBinding binding;
    String uid;
    public CheckoutFragment() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
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

        binding.buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextCustomerName.getText().toString().trim();
                String phoneCustomer = binding.editTextCustomerPhone.getText().toString().trim();
                String address = binding.editTextCustomerAddress.getText().toString().trim();

                if (name.isEmpty() || phoneCustomer.isEmpty() || address.isEmpty()) {
                    Toast.makeText(getContext(), "Field error ? ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Order order = new Order();
                order.setIdCustomer(uid);
                order.setCreatedAt(new Date());

                order.setAddressCustomer(address);
                order.setNameCustomer(name);
                order.setPhoneCustomer(phoneCustomer);

                CartRepo.findCartByUid(uid, new OnCartInterface() {
                    @Override
                    public void onCartFound(List<CartItem> cartItemList) {
                        Log.i(TAG, cartItemList.size() + "");
                        order.setCartItemList(cartItemList);

                        double totalPrice = 0.0;
                        for (CartItem cartItem : cartItemList) {
                            totalPrice += cartItem.getPrice() * cartItem.getQuantity();
                        }
                        order.setTotal(totalPrice);

                        OrderRepo.createOrder(order, new OnListerOrder() {
                            @Override
                            public void OnCreateSuccess(String idOrder) {
                                Toast.makeText(getContext(), "Create order " + idOrder + "successfully", Toast.LENGTH_SHORT).show();

                                CartRepo.clearCart(uid);

                                Bundle bundle = new Bundle();
                                bundle.putString("idOrder", idOrder);
                                OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
                                orderSummaryFragment.setArguments(bundle);

                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction
                                        .replace(R.id.fragment_container, orderSummaryFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    }
                });
            }
        });
    }
}