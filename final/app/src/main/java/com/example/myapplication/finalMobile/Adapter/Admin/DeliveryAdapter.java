//package com.example.myapplication.finalMobile.Adapter.Admin;
//
//import android.content.res.ColorStateList;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.myapplication.finalMobile.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {
//    private ArrayList<String> customerName;
//    private ArrayList<String> moneyPay;
//
//    public DeliveryAdapter(ArrayList<String> customerName, ArrayList<String> moneyPay) {
//        this.customerName = customerName;
//        this.moneyPay = moneyPay;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        TextView customer, payment, received;
//        CardView statusColor;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            customer = itemView.findViewById(R.id.customer);
//            payment = itemView.findViewById(R.id.payment);
//            received = itemView.findViewById(R.id.received);
//            statusColor = itemView.findViewById(R.id.statusColor);
//
//        }
//
//        public void bind(int position){
//            customer.setText(customerName.get(position));
//            received.setText(moneyPay.get(position));
//
//            Map<String, Integer> colorMap = new HashMap<>();
//            colorMap.put("Received", Color.GREEN);
//            colorMap.put("Not Received", Color.RED);
//            colorMap.put("Pending", Color.GRAY);
//            received.setTextColor(colorMap.getOrDefault(moneyPay.get(position), Color.BLACK));
//            statusColor.setBackgroundTintList(ColorStateList.valueOf(colorMap.getOrDefault(moneyPay.get(position), Color.BLACK)));
//        }
//    }
//
//    @NonNull
//    @Override
//    public DeliveryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_delivery, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DeliveryAdapter.ViewHolder holder, int position) {
//        holder.bind(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return customerName.size();
//    }
//}
//
