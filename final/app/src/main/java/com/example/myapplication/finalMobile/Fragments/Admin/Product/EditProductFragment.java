package com.example.myapplication.finalMobile.Fragments.Admin.Product;

import static com.example.myapplication.finalMobile.Adapter.CartAdapter.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

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
import com.example.myapplication.finalMobile.databinding.FragmentEditProductBinding;

import java.util.ArrayList;
import java.util.List;


public class EditProductFragment extends Fragment {
    private static final String TAG = EditProductFragment.class.getSimpleName();
    FragmentEditProductBinding binding;
    String idProduct;
    TextView tvSelectImage, name, price, des;
    ImageView selectImage;
    ImageButton btnBackMainScreen;
    AppCompatButton btnAddItem;
    Spinner spinner;
    Category category;
    Uri uri;
    AdminMainActivity mainActivity;
    Context context;
    List<Category> categoryList = new ArrayList<Category>();
    ProgressDialog progressDialog;
    final private ActivityResultLauncher<Intent> mActivityResultLauncher1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleActivityResult(result, new EditProductFragment());
        }
    });

    private void handleActivityResult(ActivityResult result, Fragment fragment) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                Uri uri = intent.getData();
                if (uri != null) {
                    this.setUri(uri);
                }
            }
        }
    }

    public EditProductFragment() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            idProduct = arguments.getString("idProduct");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated");

        selectImage = binding.selectImage;

        initialize();

        ProductRepo.findProductById(idProduct, new ProductRepo.OnProductFoundListener() {
            @Override
            public void onProductFound(Product product) {
                name.setText(product.getName());
                price.setText(String.valueOf(product.getPrice()));
                des.setText(product.getDes());
                Glide.with(mainActivity).load(product.getUrl()).error(R.drawable.user).centerCrop().into(selectImage);
                handleSpinner(product.getCategory());
            }

            @Override
            public void onProductNotFound() {
            }

            @Override
            public void onProductError(String errorMessage) {
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    openGallery1();
                    return;
                }
                if (mainActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery1();
                } else {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    getActivity().requestPermissions(permissions, Contraints.MY_REQUEST_CODE_UPDATE);
                }
            }
        });
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namep = binding.enterFoodName.getText().toString();
                String pricep = binding.enterFoodPrice.getText().toString();
                String des = binding.description.getText().toString();

                if (namep.isEmpty() || Double.parseDouble(pricep) < 0 || des.isEmpty()) {
                    Toast.makeText(getContext(), "Validator Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ProductRepo.findProductById(idProduct, new ProductRepo.OnProductFoundListener() {
                    @Override
                    public void onProductFound(Product product) {

                        product.setName(namep);
                        product.setPrice(Double.parseDouble(pricep));
                        product.setDes(des);
                        product.setCategory(category);

                        progressDialog.setMessage("Update product");
                        progressDialog.show();
                        ProductRepo.updateProduct(product, uri, new ProductRepo.OnHandleProduct() {
                            @Override
                            public void OnSuccessful() {
                                progressDialog.hide();
                                Toast.makeText(getContext(), "Update product successful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void OnFailure(Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProductNotFound() {
                    }

                    @Override
                    public void onProductError(String errorMessage) {

                    }
                });
            }
        });

    }

    public void openGallery1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher1.launch(Intent.createChooser(intent, "Select Picture"));
    }

    public void setUri(Uri uri) {
        this.uri = uri;
        Glide.with(getContext()).load(uri).into(selectImage);
    }

    private void initialize() {

        tvSelectImage = binding.tvSelectImage;
        btnBackMainScreen = binding.btnBackMainScreen;
        spinner = binding.ingredient;
        btnAddItem = binding.btnAddItem;
        name = binding.enterFoodName;
        des = binding.description;
        price = binding.enterFoodPrice;

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
                    categoryList.add(category);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_spinner_item, categoryNames);
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

    private void handleSpinner(Category selectedCategory) {
        int selectedIndex = -1;
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId().equals(selectedCategory.getId())) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex != -1) {
            spinner.setSelection(selectedIndex);
        }
    }

}