package com.example.myapplication.finalMobile.Fragments.Admin.Product;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.finalMobile.Activity.AdminMainActivity;
import com.example.myapplication.finalMobile.Adapter.Admin.ProductAdapter;
import com.example.myapplication.finalMobile.Model.Product;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.ProductRepo;
import com.example.myapplication.finalMobile.databinding.AdminActivityMainBinding;
import com.example.myapplication.finalMobile.databinding.FragmentListProductBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListProductFragment extends Fragment {
    private static final String TAG = ListProductFragment.class.getSimpleName();
    ImageButton btnBackMainScreen;
    RecyclerView menuRecyclerView;
    ArrayList<Product> dataSources;
    ProductAdapter adapter;
    FragmentListProductBinding binding;
    FloatingActionButton floatingActionButton;

    AddProductFragment addProductFragment;

    public ListProductFragment() {
    }

    public void setAddProductFragment(AddProductFragment addProductFragment) {
        this.addProductFragment = addProductFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi();

        ProductRepo.getAllProduct1(new ProductRepo.OnProductInter() {
            @Override
            public void Products(List<Product> products) {
                dataSources.clear();
                if (!products.isEmpty()) {
                    dataSources.addAll(products);
                    adapter.setDatasource(dataSources);
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, addProductFragment).addToBackStack(ListProductFragment.class.getSimpleName()).commit();
            }
        });

    }

    private void initUi() {
        btnBackMainScreen = binding.btnBackMainScreen;
        menuRecyclerView = binding.menuRecyclerView;
        floatingActionButton = binding.addProduct;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        menuRecyclerView.setLayoutManager(layoutManager);

        dataSources = new ArrayList<Product>();
        adapter = new ProductAdapter(dataSources);
        menuRecyclerView.setAdapter(adapter);

    }

}