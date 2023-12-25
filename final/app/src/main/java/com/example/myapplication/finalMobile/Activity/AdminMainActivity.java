package com.example.myapplication.finalMobile.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ReportFragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.finalMobile.Fragments.Admin.HomeFragment;
import com.example.myapplication.finalMobile.Fragments.Admin.Product.AddProductFragment;
import com.example.myapplication.finalMobile.Fragments.Admin.Product.EditProductFragment;
import com.example.myapplication.finalMobile.Fragments.Admin.Product.ListProductFragment;
import com.example.myapplication.finalMobile.Fragments.Admin.Order.OrderMainFragment;
import com.example.myapplication.finalMobile.Fragments.Admin.Report.MainReportFragment;
import com.example.myapplication.finalMobile.Fragments.Admin.User.HistoryFragment;
import com.example.myapplication.finalMobile.Fragments.Admin.User.MainUserFragment;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Utils.Contrainst.Contraints;
import com.example.myapplication.finalMobile.databinding.AdminActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    AdminActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    TextView tvName, tvEmail;
    ImageView imageViewUrl;
    private static final String TAG = AdminMainActivity.class.getSimpleName();
    final private AddProductFragment addProductFragment = new AddProductFragment();
    ListProductFragment listProductFragment = new ListProductFragment();

    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    handleActivityResult(result, addProductFragment);
                }
            });

    private void handleActivityResult(ActivityResult result, Fragment fragment) {
        if (result.getResultCode() == RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                Uri uri = intent.getData();
                if (uri != null) {
                    if (fragment instanceof AddProductFragment) {
                        ((AddProductFragment) fragment).setUri(uri);
                    } else if (fragment instanceof EditProductFragment) {
                        ((EditProductFragment) fragment).setUri(uri);
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseConnectionMonitor.startMonitoring(getBaseContext());

        binding = AdminActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        drawerLayout = binding.drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, binding.toolbarAdmin, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        initUi();

        listProductFragment.setAddProductFragment(addProductFragment);

        if (savedInstanceState == null) {
            replaceFragment(listProductFragment);
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        binding.navView.setNavigationItemSelectedListener(this);

        showUserInformation();

    }

    private void initUi() {
        tvName = binding.navView.getHeaderView(0).findViewById(R.id.header_name);
        tvEmail = binding.navView.getHeaderView(0).findViewById(R.id.header_email);
        imageViewUrl = binding.navView.getHeaderView(0).findViewById(R.id.header_image);
    }

    public void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String email = user.getEmail();
        String fullName = user.getDisplayName();
        Uri url = user.getPhotoUrl();

        tvEmail.setText(email);

        if (fullName == null) {
            tvName.setVisibility(View.GONE);
        } else {
            tvName.setText(fullName);
        }

        Glide.with(getBaseContext()).load(url).into(imageViewUrl);

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_products) {
            replaceFragment(listProductFragment);
        }

        if (id == R.id.menu_users) {
            replaceFragment(new MainUserFragment());
        }

        if (id == R.id.menu_orders) {
            replaceFragment(new OrderMainFragment());
        }

        if (id == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.menu_home) {
            replaceFragment(new MainReportFragment());
        }


        Menu menu = binding.navView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setChecked(false);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Contraints.MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}
