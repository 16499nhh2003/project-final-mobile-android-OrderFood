package com.example.myapplication.finalMobile.Adapter.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> datasource;
    private OrderAdapterLister orderAdapterListener;

    public OrderAdapter(List<Order> datasource) {
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
        TextView customerName, payment, total, delivery;
        CardView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ViewholderAdminItemDeliveryBinding binding = ViewholderAdminItemDeliveryBinding.bind(itemView);
            customerName = binding.customerName;
            payment = binding.payment;
            total = binding.total;
            delivery = binding.delivery;
            status = binding.statusColor;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Order clickedOrder = datasource.get(getAdapterPosition());
                    if (orderAdapterListener != null) {
                        orderAdapterListener.OnShowDetail(clickedOrder);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Order clickedOrder = datasource.get(getAdapterPosition());
                    if (orderAdapterListener != null) {
                        orderAdapterListener.OnShowUpdate(clickedOrder);
                        return true;
                    }
                    return false;
                }
            });

        }


        public void bind(int position) {
            Order order = datasource.get(position);
            customerName.setText(order.getNameCustomer());
            payment.setText("Cash");
            total.setText("Total : " + order.getTotal());
            delivery.setText(order.getOrderStatus().toString());

            String val = order.getOrderStatus().toString();
            if (val.equalsIgnoreCase("PROCESSING")) {
                status.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.PROCESSING_color));
            } else if (val.equalsIgnoreCase("COMPLETE")) {
                status.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.COMPLETE_color));
            } else {
                status.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.CANCELED_color));
            }
        }
    }


    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_item_delivery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
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
