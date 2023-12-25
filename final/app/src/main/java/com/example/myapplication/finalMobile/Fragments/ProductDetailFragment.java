package com.example.myapplication.finalMobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.finalMobile.Activity.LoginActivity;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.CartRepo;
import com.example.myapplication.finalMobile.databinding.FragmentProductDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProductDetailFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = ProductFragment.class.getSimpleName();
    FragmentProductDetailBinding binding;
    int quantity;
    double priceFinal, price;
    String productId, uid;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
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

        initializeUi();

        binding.addQuantity.setOnClickListener(this);
        binding.subQuantity.setOnClickListener(this);
        binding.buttonaddtocart.setOnClickListener(this);

    }

    private void initializeUi() {
        if (getArguments() != null) {
            String name = this.getArguments().getString("name");
            String imageUrl = this.getArguments().getString("url");
            String desc = this.getArguments().getString("des");
            Double price = this.getArguments().getDouble("price");
            productId = this.getArguments().getString("id");

            // binding data
            binding.productDetailDesc.setText(desc);
            binding.productDetailName.setText(name);
            binding.productDetailPrice.setText(String.valueOf(price));

            if (getContext() != null) {
                Glide.with(getContext()).load(imageUrl).centerCrop().into(binding.productDetailImage);
            }

        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.addQuantity) {
            increase();
        } else if (id == R.id.subQuantity) {
            decrease();
        } else if (id == R.id.buttonaddtocart) {
            addToCart();
        }
    }

    private void addToCart() {
        if (quantity == 0) {
            Toast.makeText(getActivity(), "Please quantity > 0", Toast.LENGTH_LONG).show();
        } else {
            CartItem cartItem = new CartItem(productId, quantity, price);
            CartRepo.addToCart(cartItem, uid, productId);
            Toast.makeText(getActivity(), "Product added to cart", Toast.LENGTH_LONG).show();
            getParentFragmentManager().popBackStack();
        }
    }

    private void decrease() {
        quantity = Integer.parseInt(binding.quantitycounterproductdetail.getText().toString());
        price = Double.parseDouble(binding.productDetailPrice.getText().toString());
        if (quantity <= 0) {
            quantity = 0;
            priceFinal = 0;
        } else {
            quantity--;
            priceFinal = quantity * price;
        }
        binding.quantitycounterproductdetail.setText(String.valueOf(quantity));
        binding.totalPriceproductdetail.setText("Total is " + String.valueOf(quantity) + " x " + price + "= \n" + String.valueOf(priceFinal));
    }

    private void increase() {
        quantity = Integer.parseInt(binding.quantitycounterproductdetail.getText().toString());
        price = Double.parseDouble(binding.productDetailPrice.getText().toString());
        quantity++;
        priceFinal = quantity * price;
        binding.quantitycounterproductdetail.setText(String.valueOf(quantity));
        binding.totalPriceproductdetail.setText("Total is " + String.valueOf(quantity) + " x " + price + "= \n" + String.valueOf(priceFinal));
    }

}