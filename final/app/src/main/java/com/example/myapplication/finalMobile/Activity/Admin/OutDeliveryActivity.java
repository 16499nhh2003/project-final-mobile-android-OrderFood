//package com.example.myapplication.finalMobile.Activity.Admin;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//
//import com.example.myapplication.finalMobile.Adapter.Admin.DeliveryAdapter;
//import com.example.myapplication.finalMobile.R;
//
//import java.util.ArrayList;
//
//
//public class OutDeliveryActivity extends AppCompatActivity {
//    ImageButton btnBackMainScreen;
//    private RecyclerView deliveryRcv;
//    private ArrayList<String> customerName = new ArrayList<>();
//    private ArrayList<String> moneypay = new ArrayList<>();
//    private DeliveryAdapter adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.viewholder_admin_activity_out_delivery);
//        btnBackMainScreen = findViewById(R.id.btnBackMainScreen);
//        btnBackMainScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(OutDeliveryActivity.this, MainScreenActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        deliveryRcv = findViewById(R.id.deliveryRcv);
//
//        customerName.add("John");
//        customerName.add("Jane");
//        customerName.add("Duc Minh");
//        customerName.add("Huy Hoa");
//
//        moneypay.add("Not Received");
//        moneypay.add("Pending");
//        moneypay.add("Received");
//        moneypay.add("Received");
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        deliveryRcv.setLayoutManager(layoutManager);
//        adapter = new DeliveryAdapter(customerName, moneypay);
//        deliveryRcv.setAdapter(adapter);
//    }
//}