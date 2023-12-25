package com.example.myapplication.finalMobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Product;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.ProductRepo;

import java.util.List;

public class CartItemAdapter extends BaseAdapter {
    private Context context;
    private List<CartItem> cartItemList;

    public CartItemAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @Override
    public int getCount() {
       return cartItemList != null ? cartItemList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return cartItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_cart_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView price = convertView.findViewById(R.id.price);
        TextView quantity = convertView.findViewById(R.id.quantity);
        TextView totalPrice = convertView.findViewById(R.id.totalPrice);

        CartItem cartItem = cartItemList.get(position);

        if (cartItem != null) {

            String idProduct = cartItem.getProductId();

            ProductRepo productRepo = new ProductRepo();
            productRepo.findProductById(idProduct, new ProductRepo.OnProductFoundListener() {
                @Override
                public void onProductFound(Product product) {
                    name.setText("Name: " + product.getName());
                }

                @Override
                public void onProductNotFound() {
                }

                @Override
                public void onProductError(String errorMessage) {

                }
            });
            price.setText(String.valueOf(cartItem.getPrice()));
            quantity.setText(String.valueOf(cartItem.getQuantity()));
            totalPrice.setText("Total Price : " + String.valueOf(cartItem.getPrice() * cartItem.getQuantity()));
        }
        return convertView;
    }

}
