package com.example.myapplication.finalMobile.Adapter.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.finalMobile.Fragments.Admin.Product.EditProductFragment;
import com.example.myapplication.finalMobile.Model.Product;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.ProductRepo;
import com.example.myapplication.finalMobile.databinding.ViewholderAdminItemAllBinding;

import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> datasource;

    public ProductAdapter(List<Product> datasource) {
        this.datasource = datasource;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDatasource(List<Product> datasource) {
        this.datasource = datasource;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewholderAdminItemAllBinding binding;
        TextView foodName, foodPrice;
        ImageView foodImageView;
        ImageButton btnBin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ViewholderAdminItemAllBinding.bind(itemView);

            foodName = binding.foodName;
            foodPrice = binding.foodPrice;
            foodImageView = binding.foodImageView;

            btnBin = binding.btnBin;

            btnBin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditDialog(getAdapterPosition());
                }
            });
        }

        private void showEditDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Edit Item")
                    .setMessage("Edit product?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openEditFragment(position);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        private void openEditFragment(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("idProduct", datasource.get(position).getId());
            EditProductFragment editFragment = new EditProductFragment();
            editFragment.setArguments(bundle);

            FragmentManager fragmentManager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null)
                    .commit();

        }

        private void showDeleteConfirmationDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeFood(position);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        public void removeFood(int position) {
            String productId = datasource.get(position).getId();

            ProductRepo.deleteProduct(productId, new ProductRepo.OnHandleProduct() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void OnSuccessful() {
                    notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();
                }

                @Override
                public void OnFailure(Exception e) {

                }
            });
        }

        public void bind(int position) {
            Product product = datasource.get(position);
            foodName.setText(product.getName());
            foodPrice.setText(String.valueOf(product.getPrice()));
            Glide.with(itemView.getContext()).load(product.getUrl()).error(R.drawable.menu2).centerCrop().into(foodImageView);
        }
    }


    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_item_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (datasource.isEmpty()) {
            return 0;
        }
        return datasource.size();
    }
}
