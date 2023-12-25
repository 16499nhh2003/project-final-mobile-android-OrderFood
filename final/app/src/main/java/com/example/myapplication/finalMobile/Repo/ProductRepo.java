package com.example.myapplication.finalMobile.Repo;

import android.annotation.SuppressLint;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.finalMobile.Model.Product;
import com.example.myapplication.finalMobile.Utils.Contrainst.Contraints;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.index.qual.PolyUpperBound;

import java.util.ArrayList;
import java.util.List;

public class ProductRepo {
    List<Product> productList = new ArrayList<Product>();

    private static final String PRODUCT_PATH = "products";
    private static final DatabaseReference productReference;
    private static final StorageReference storageRef;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productReference = database.getReference(PRODUCT_PATH);
        storageRef = FirebaseStorage.getInstance().getReference("product_images");
    }

    static OnProductInter interfaceProducts;


    public ProductRepo(OnProductInter interfaceProducts) {
        ProductRepo.interfaceProducts = interfaceProducts;
    }

    public ProductRepo() {
    }

    public static void getAllProduct() {
        productReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> products = new ArrayList<Product>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    products.add(product);
                }
                interfaceProducts.Products(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void getAllProduct1(final OnProductInter interfaceProducts) {
        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Product> productList = new ArrayList<Product>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                interfaceProducts.Products(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void findProductById(String idProduct, final OnProductFoundListener listener) {
        Query query = productReference.orderByChild("id").equalTo(idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        listener.onProductFound(product);
                        return;
                    }
                } else {
                    listener.onProductNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onProductError(databaseError.getMessage());
            }
        });
    }

    public void getTop3Products() {
        productReference.orderByKey().limitToFirst(Contraints.LIMIT_PAGNIGATION).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    List<Product> top3Products = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        top3Products.add(product);
                    }
                    // Now you have the top 3 products in the 'top3Products' list
                    interfaceProducts.Products(top3Products);
                } catch (Exception e) {
                    interfaceProducts.Products(new ArrayList<Product>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    public void search(String keyword, final OnProductInter listener) {
        productReference.addChildEventListener(new ChildEventListener() {
            List<Product> products = new ArrayList<Product>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                        products.add(product);
                    }
                }
                listener.Products(products);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void addProduct(Product product, Uri uri, final OnHandleProduct listener) {
        String documentId = productReference.push().getKey();
        product.setId(documentId);

        StorageReference imageRef = storageRef.child(product.getId() + ".jpg");

        imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef
                        .getDownloadUrl()
                        .addOnSuccessListener(uri1 -> {
                            product.setUrl(uri1.toString());
                            productReference.child(product.getId()).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    listener.OnSuccessful();
                                }
                            });
                        });
            }
        });
    }

    public static void updateProduct(Product product, Uri updatedUri, final OnHandleProduct listener) {

        if (product.getId() == null) {
            listener.OnFailure(new Exception("Product ID is null"));
            return;
        }

        if (updatedUri != null) {

            StorageReference imageRef = storageRef.child(product.getId() + ".jpg");
            imageRef.putFile(updatedUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    product.setUrl(uri.toString());
                    updateProductData(product, listener);
                });
            }).addOnFailureListener(e -> listener.OnFailure(e));
        } else {
            updateProductData(product, listener);
        }
    }

    private static void updateProductData(Product product, final OnHandleProduct listener) {
        productReference.child(product.getId()).setValue(product)
                .addOnSuccessListener(unused -> listener.OnSuccessful())
                .addOnFailureListener(listener::OnFailure);
    }

    public static void deleteProduct(String id, final OnHandleProduct listener) {
        productReference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                StorageReference imageRef = storageRef.child(id + ".jpg");
                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.OnSuccessful();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.OnFailure(e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.OnFailure(e);
            }
        });

    }

    public interface OnProductInter {
        void Products(List<Product> products);
    }

    public interface OnProductFoundListener {
        void onProductFound(Product product);

        void onProductNotFound();

        void onProductError(String errorMessage);
    }

    public interface OnHandleProduct {
        void OnSuccessful();

        void OnFailure(Exception e);
    }

}
