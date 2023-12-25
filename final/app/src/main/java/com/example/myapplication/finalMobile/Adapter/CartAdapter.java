package com.example.myapplication.finalMobile.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.finalMobile.Interface.OnClickedCartItem;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Product;
import com.example.myapplication.finalMobile.Repo.ProductRepo;
import com.example.myapplication.finalMobile.databinding.FragmentCartBinding;
import com.example.myapplication.finalMobile.databinding.ViewholderCartBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public static final String TAG = CartAdapter.class.getSimpleName();
    OnClickedCartItem onClickedCartItem;
    List<CartItem> datasource;

    public CartAdapter(OnClickedCartItem onClickedCartItem) {
        this.onClickedCartItem = onClickedCartItem;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDatasource(List<CartItem> datasource) {
        this.datasource = datasource;
        this.notifyDataSetChanged();
    }

    public List<CartItem> getDatasource() {
        return this.datasource;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartItem cartItem = datasource.get(position);

        ProductRepo.findProductById(cartItem.getProductId(), new ProductRepo.OnProductFoundListener() {
            @Override
            public void onProductFound(Product product) {
                Log.i(TAG, product.toString());

                holder.binding.cartproducttitle.setText(product.getName().trim());
                holder.binding.cartdesc.setText("Quantity : x" + cartItem.getQuantity());
                holder.binding.cartPrice.setText("Price Unit : " + String.valueOf(cartItem.getPrice()));

                Glide.with(holder.itemView).load(product.getUrl()).into(holder.binding.cartmainimage);
            }

            @Override
            public void onProductNotFound() {
            }

            @Override
            public void onProductError(String errorMessage) {
            }
        });
    }

    @Override
    public int getItemCount() {
        if (datasource.isEmpty()) {
            return 0;
        }
        return datasource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ViewholderCartBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ViewholderCartBinding.bind(itemView);
            binding.btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickedCartItem.OnDeleteItem(datasource, getAdapterPosition());
        }
    }
}