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
//import com.example.myapplication.finalMobile.Adapter.Admin.PendingOrderAdapter;
//import com.example.myapplication.finalMobile.R;
//
//import java.util.ArrayList;
//
//public class PendingOrderActivity extends AppCompatActivity {
//    ImageButton btnBackMainScreen;
//    private RecyclerView pendingOrderRcv;
//    private ArrayList<String> customerName = new ArrayList<>();
//    private ArrayList<String> quantity = new ArrayList<>();
//    private PendingOrderAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.admin_activity_pending_order);
//        btnBackMainScreen = findViewById(R.id.btnBackMainScreen);
//        btnBackMainScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PendingOrderActivity.this, MainScreenActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        pendingOrderRcv = findViewById(R.id.pendingOrderRcv);
//
//        customerName.add("John");
//        customerName.add("Jane");
//        customerName.add("Duc Minh");
//        customerName.add("Huy Hoa");
//
//        quantity.add("2");
//        quantity.add("3");
//        quantity.add("4");
//        quantity.add("5");
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        pendingOrderRcv.setLayoutManager(layoutManager);
//        adapter = new PendingOrderAdapter(customerName, quantity);
//        pendingOrderRcv.setAdapter(adapter);
//    }
//}