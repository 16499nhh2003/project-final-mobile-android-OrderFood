package com.example.myapplication.finalMobile.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
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
import com.example.myapplication.finalMobile.Adapter.ProductAdapter;
import com.example.myapplication.finalMobile.Interface.OnClickedProduct;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Product;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.CartRepo;
import com.example.myapplication.finalMobile.Repo.ProductRepo;
import com.example.myapplication.finalMobile.databinding.FragmentProductBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements ProductRepo.OnProductInter, OnClickedProduct, ProductAdapter.OnAddToCart {

    private static final String TAG = ProductFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> products;
    private FragmentProductBinding fragmentProductBinding;
    String uid;

    public ProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentProductBinding = FragmentProductBinding.inflate(inflater, container, false);
        return fragmentProductBinding.getRoot();
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


        products = new ArrayList<Product>();
        productAdapter = new ProductAdapter(this);
        productAdapter.setData(products);

        productAdapter.setOnAddToCart(this);

        recyclerView = fragmentProductBinding.fragmentProduct;
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        ProductRepo productRepo = new ProductRepo(this);
        ProductRepo.getAllProduct();
    }

    @Override
    public void Products(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        productAdapter.setData(products);
    }

    @Override
    public void OnProClicked(List<Product> products, int position) {

        Product product = products.get(position);
        Log.i(TAG, product.toString());

        Bundle bundle = new Bundle();
        bundle.putString("id", product.getId().trim());
        bundle.putString("name", product.getName());
        bundle.putString("url", product.getUrl().trim());
        bundle.putString("des", product.getDes().trim());
        bundle.putDouble("price", product.getPrice());


        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        productDetailFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_container, productDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addToCart(Product product) {
        CartItem cartItem = new CartItem(product.getId(), 1, product.getPrice());
        CartRepo.addToCart(cartItem, uid, product.getId());
        Toast.makeText(getContext(), "Product added to cart", Toast.LENGTH_LONG).show();
    }
}