package com.example.myapplication.finalMobile.Adapter.Admin;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.finalMobile.Model.Order;
import com.example.myapplication.finalMobile.Model.User;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.databinding.DialogOrderItemLayoutBinding;
import com.example.myapplication.finalMobile.databinding.ViewholderAdminItemDeliveryBinding;
import com.example.myapplication.finalMobile.databinding.ViewholderItemHistoryBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigInteger;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<Order> datasource;
    private OrderAdapterLister orderAdapterListener;

    public HistoryAdapter(List<Order> datasource) {
        this.datasource = datasource;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDatasource(List<Order> datasource) {
        this.datasource = datasource;
        notifyDataSetChanged();
    }

    public void setOrderAdapterListener(OrderAdapterLister orderAdapterListener) {
        this.orderAdapterListener = orderAdapterListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, payment, total, delivery, orderStatusTextView, orderDetailsTextView, date, phoneTextView;
        Button viewDetailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ViewholderItemHistoryBinding binding = ViewholderItemHistoryBinding.bind(itemView);

            orderDetailsTextView = binding.orderDetailsTextView;
            customerName = binding.customerNameTextView;
            payment = binding.paymentMethodTextView;
            total = binding.totalTextView;
            delivery = binding.addressTextView;
            orderStatusTextView = binding.orderStatusTextView;
            viewDetailsButton = binding.viewDetailsButton;
            phoneTextView = binding.phoneTextView;
            date = binding.date;



            viewDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Order clickedOrder = datasource.get(getAdapterPosition());
                    if (orderAdapterListener != null) {
                        orderAdapterListener.OnShowDetail(clickedOrder);
                    }
                }
            });
        }


        public void bind(int position) {
            Order order = datasource.get(position);

            customerName.setText("Customer Name :  " + order.getNameCustomer());
            phoneTextView.setText("Phone : " + order.getPhoneCustomer());
            payment.setText("Method : Cash");
            total.setText("Total : " + order.getTotal());
            delivery.setText("Address : " + order.getAddressCustomer());
            orderStatusTextView.setText("Status: " + order.getOrderStatus().toString());
            date.setText("Ngày đặt : " + order.getFormattedCreatedAt());

        }
    }


    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (datasource.isEmpty()) {
            return 0;
        }
        return datasource.size();
    }

    public interface OrderAdapterLister {
        void OnShowDetail(Order order);

        void OnShowUpdate(Order order);
    }
}
