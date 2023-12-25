package com.example.myapplication.finalMobile.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.finalMobile.Interface.OnClickedProduct;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Product;
import com.example.myapplication.finalMobile.Repo.CartRepo;
import com.example.myapplication.finalMobile.databinding.ViewholderProductBinding;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Product> dataSource;
    OnClickedProduct onClickedProduct;
    OnAddToCart onAddToCart;

    public ProductAdapter(OnClickedProduct onClickedProduct) {
        this.onClickedProduct = onClickedProduct;
    }

    public void setOnAddToCart(OnAddToCart onAddToCart) {
        this.onAddToCart = onAddToCart;
    }


    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderProductBinding binding = ViewholderProductBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = dataSource.get(position);

        holder.binding.nameProduct.setText(product.getName());
        holder.binding.priceProduct.setText(product.getPrice().toString());

        Glide.with(holder.itemView.getContext()).load(product.getUrl()).centerCrop().into(holder.binding.imageproduct);


    }

    @Override
    public int getItemCount() {
        if (dataSource.isEmpty()) {
            return 0;
        }
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ViewholderProductBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ViewholderProductBinding.bind(itemView);

            binding.imageproduct.setOnClickListener(this);
            binding.nameProduct.setOnClickListener(this);
            binding.priceProduct.setOnClickListener(this);

            binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = dataSource.get(getAdapterPosition());
                    onAddToCart.addToCart(product);
                }
            });
        }

        @Override
        public void onClick(View v) {
            onClickedProduct.OnProClicked(dataSource, getAdapterPosition());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> products) {
        this.dataSource = products;
        notifyDataSetChanged();
    }

    public interface OnAddToCart {
        void addToCart(Product product);
    }
}
