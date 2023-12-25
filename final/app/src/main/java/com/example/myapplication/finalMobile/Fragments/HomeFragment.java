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
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.finalMobile.Activity.LoginActivity;
import com.example.myapplication.finalMobile.Adapter.CategoryAdapter;
import com.example.myapplication.finalMobile.Adapter.ProductAdapter;
import com.example.myapplication.finalMobile.Adapter.ViewPagerAdapter;
import com.example.myapplication.finalMobile.Interface.OnClickedProduct;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Category;
import com.example.myapplication.finalMobile.Model.Product;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.CartRepo;
import com.example.myapplication.finalMobile.Repo.CategoryRepo;
import com.example.myapplication.finalMobile.Repo.ProductRepo;
import com.example.myapplication.finalMobile.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements OnClickedProduct, ProductAdapter.OnAddToCart {
    private static final String TAG = HomeFragment.class.getSimpleName();
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    int[] images = {R.drawable.banner2, R.drawable.banner3, R.drawable.banner1};
    private FragmentHomeBinding binding;
    private List<Category> categories;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<Product> dataSource;

    private ProductRepo productRepo;
    final Handler handler = new Handler(Looper.getMainLooper());
    final int delay = 3000;
    private Timer timer;
    String uid;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();

        // show categories
        CategoryRepo categoryRepo = new CategoryRepo(new CategoryRepo.OnCategoryInterface() {
            @Override
            public void Categories(List<Category> categories) {
                getCategories(categories);
            }
        });
        categoryRepo.getAllCategory();

        // show popular
        productRepo = new ProductRepo(new ProductRepo.OnProductInter() {
            @Override
            public void Products(List<Product> products) {
                getProducts(products);
            }
        });
        productRepo.getTop3Products();

        // button show view more
        binding.viewmore.setOnClickListener(new View.OnClickListener() {
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

        // button search
        binding.keyword.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {

                productRepo.search(keyword, new ProductRepo.OnProductInter() {
                    @Override
                    public void Products(List<Product> products) {
                        productAdapter.setData(products);
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                productRepo.search(keyword, new ProductRepo.OnProductInter() {
                    @Override
                    public void Products(List<Product> products) {
                        productAdapter.setData(products);
                    }
                });
                return true;
            }
        });
    }

    private void initUi() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        uid = user.getUid();

        mViewPager = (ViewPager) binding.viewPagerMain;
        mViewPagerAdapter = new ViewPagerAdapter(getContext(), images);
        mViewPager.setAdapter(mViewPagerAdapter);

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = mViewPager.getCurrentItem();
                        int totalItems = mViewPagerAdapter.getCount();
                        int nextItem = (currentItem + 1) % totalItems;
                        mViewPager.setCurrentItem(nextItem);
                    }
                });
            }
        }, delay, delay);


        RecyclerView rvCategory = binding.recycleViewCategory;
        categories = new ArrayList<Category>();
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        categoryAdapter = new CategoryAdapter(categories);

        rvCategory.setLayoutManager(linearLayout);
        rvCategory.setAdapter(categoryAdapter);


        RecyclerView rvProduct = binding.recycleViewProduct;
        dataSource = new ArrayList<Product>();
        productAdapter = new ProductAdapter(this);
        productAdapter.setData(dataSource);
        productAdapter.setOnAddToCart(this);

        rvProduct.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvProduct.setAdapter(productAdapter);

    }

    private void getCategories(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        categoryAdapter.setData(categories);
    }

    private void getProducts(List<Product> products) {
        this.dataSource.clear();
        this.dataSource.addAll(products);
        productAdapter.setData(products);
    }

    @Override
    public void OnProClicked(List<Product> products, int position) {
        Product product = products.get(position);
        Log.i(TAG, product.toString());
        Bundle bundle = new Bundle();
        bundle.putString("id", product.getId().toString().trim());
        bundle.putString("name", product.getName().toString());
        bundle.putString("url", product.getUrl().toString().trim());
        bundle.putString("des", product.getDes().toString().trim());
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
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void addToCart(Product product) {
        CartItem cartItem = new CartItem(product.getId(), 1, product.getPrice());
        CartRepo.addToCart(cartItem, uid, product.getId());
        Toast.makeText(getContext(), "Product added to cart", Toast.LENGTH_LONG).show();
    }
}