package com.example.myapplication.finalMobile.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.myapplication.finalMobile.Adapter.CartItemAdapter;
import com.example.myapplication.finalMobile.Interface.OnFindOrderListener;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Order;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.OrderRepo;
import com.example.myapplication.finalMobile.databinding.FragmentOrderSummaryBinding;

import java.lang.reflect.Array;
import java.util.List;

public class OrderSummaryFragment extends Fragment {

    FragmentOrderSummaryBinding fragmentOrderSummaryBinding;
    public OrderSummaryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentOrderSummaryBinding = FragmentOrderSummaryBinding.inflate(inflater , container , false);
        return fragmentOrderSummaryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String idOrder = this.getArguments().getString("idOrder");

        if(TextUtils.isEmpty(idOrder)) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .replace(R.id.fragment_container, new ProductFragment())
                    .addToBackStack(null)
                    .commit();
        }

        OrderRepo.findOrderById(idOrder, new OnFindOrderListener() {
            @Override
            public void onFindOrderSuccess(Order order) {
                List<CartItem> cartItemList = order.getCartItemList();
                CartItemAdapter adapter = new CartItemAdapter(getContext() , cartItemList);
                fragmentOrderSummaryBinding.lvOrder.setAdapter(adapter);
            }
            @Override
            public void onFindOrderFailure(String errorMessage) {
            }
        });

        fragmentOrderSummaryBinding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .replace(R.id.fragment_container, new ProductFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}