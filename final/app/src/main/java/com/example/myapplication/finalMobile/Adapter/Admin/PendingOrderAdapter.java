//package com.example.myapplication.finalMobile.Adapter.Admin;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.myapplication.finalMobile.R;
//
//import java.util.ArrayList;
//
//public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ViewHolder>{
//    private ArrayList<String> customerName;
//    private ArrayList<String> quantity;
//
//    public PendingOrderAdapter(ArrayList<String> customerName, ArrayList<String> quantity) {
//        this.customerName = customerName;
//        this.quantity = quantity;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        TextView nameCustomer, number;
//        Button btnAccept;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameCustomer = itemView.findViewById(R.id.nameCustomer);
//            number = itemView.findViewById(R.id.number);
////            btnAccept = itemView.findViewById(R.id.btnAccept);
//        }
//
//        public void bind(int position){
//            nameCustomer.setText(customerName.get(position));
//            number.setText(quantity.get(position));
//        }
//    }
//    @NonNull
//    @Override
//    public PendingOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_pendiing_order, parent, false);
//        PendingOrderAdapter.ViewHolder viewHolder = new PendingOrderAdapter.ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PendingOrderAdapter.ViewHolder holder, int position) {
//        holder.bind(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return customerName.size();
//    }
//}
