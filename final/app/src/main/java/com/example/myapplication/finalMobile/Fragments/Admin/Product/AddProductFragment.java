package com.example.myapplication.finalMobile.Fragments.Admin.Product;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.finalMobile.Activity.AdminMainActivity;
import com.example.myapplication.finalMobile.Model.Category;
import com.example.myapplication.finalMobile.Model.Product;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.CategoryRepo;
import com.example.myapplication.finalMobile.Repo.ProductRepo;
import com.example.myapplication.finalMobile.Utils.Contrainst.Contraints;
import com.example.myapplication.finalMobile.databinding.FragmentAddProductBinding;

import java.util.ArrayList;
import java.util.List;


public class AddProductFragment extends Fragment {
    FragmentAddProductBinding binding;
    private static final String TAG = AddProductFragment.class.getSimpleName();
    TextView tvSelectImage;
    ImageView selectImage;
    ImageButton btnBackMainScreen;
    AppCompatButton btnAddItem;
    Spinner spinner;
    Category category;
    Uri uri;
    AdminMainActivity mainActivity;
    TextView enterFoodName;
    ProgressDialog progressDialog;

    public AddProductFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();

//        btnBackMainScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity == null) {
                    return;
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    mainActivity.openGallery();
                    return;
                }
                if (mainActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    mainActivity.openGallery();
                } else {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    mainActivity.requestPermissions(permissions, Contraints.MY_REQUEST_CODE);
                }
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namep = binding.enterFoodName.getText().toString();
                String pricep = binding.enterFoodPrice.getText().toString();
                String des = binding.description.getText().toString();

                if (namep.isEmpty() || !TextUtils.isDigitsOnly(pricep) || des.isEmpty() || category == null || uri == null) {
                    Toast.makeText(getContext(), "Validator Error", Toast.LENGTH_LONG).show();
                    return;
                }

                Product product = new Product();
                product.setName(namep);
                product.setPrice(Double.parseDouble(pricep));
                product.setDes(des);
                product.setCategory(category);

                progressDialog.setMessage("Adding product");
                progressDialog.show();
                ProductRepo.addProduct(product, uri, new ProductRepo.OnHandleProduct() {
                    @Override
                    public void OnSuccessful() {
                        progressDialog.hide();
                        Toast.makeText(getContext(), "Add product successful", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    }

                    @Override
                    public void OnFailure(Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    private void initialize() {
        tvSelectImage = binding.tvSelectImage;
        selectImage = binding.selectImage;
        btnBackMainScreen = binding.btnBackMainScreen;
        spinner = binding.ingredient;
        btnAddItem = binding.btnAddItem;

        mainActivity = (AdminMainActivity) getActivity();
        progressDialog = new ProgressDialog(mainActivity);

        displayCate();
    }

    private void displayCate() {
        CategoryRepo.getAll(new CategoryRepo.OnCategoryInterface() {

            @Override
            public void Categories(List<Category> categories) {
                List<String> categoryNames = new ArrayList<String>();
                for (Category category : categories) {
                    categoryNames.add(category.getTitle());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        category = categories.get(position);
                        Log.i(TAG, category.toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    public void setUri(Uri uri) {
        Glide.with(getActivity()).load(uri).centerCrop().into(selectImage);
        this.uri = uri;
    }

    private void clear() {
        binding.enterFoodName.setText("");
        binding.enterFoodPrice.setText("");
        binding.selectImage.setImageResource(R.drawable.user);
        binding.description.setText("");
    }
}