package com.example.myapplication.finalMobile.Fragments.Admin.Order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.myapplication.finalMobile.Adapter.Admin.OrderAdapter;
import com.example.myapplication.finalMobile.Adapter.CartItemAdapter;
import com.example.myapplication.finalMobile.Enum.OrderStatus;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Order;
import com.example.myapplication.finalMobile.Repo.OrderRepo;
import com.example.myapplication.finalMobile.databinding.DialogAdminItemDeliveryBinding;
import com.example.myapplication.finalMobile.databinding.DialogOrderItemLayoutBinding;
import com.example.myapplication.finalMobile.databinding.FragmentOrderMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;


public class OrderMainFragment extends Fragment implements OrderAdapter.OrderAdapterLister {
    private static final String TAG = OrderMainFragment.class.getSimpleName();
    FragmentOrderMainBinding fragmentOrderMainBinding;
    private RecyclerView rcv;
    private OrderAdapter adapter;
    private List<Order> dataSource;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OrderMainFragment() {
    }

    public static OrderMainFragment newInstance(String param1, String param2) {
        OrderMainFragment fragment = new OrderMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentOrderMainBinding = FragmentOrderMainBinding.inflate(inflater, container, false);
        return fragmentOrderMainBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataSource = new ArrayList<Order>();
        rcv = fragmentOrderMainBinding.deliveryRcv;
        adapter = new OrderAdapter(dataSource);
        adapter.setOrderAdapterListener(this);

        if (getContext() != null) {
            rcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            rcv.setAdapter(adapter);
            rcv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }


        OrderRepo.getAllOrder(new OrderRepo.OrderCallback() {
            @Override
            public void onOrdersLoaded(List<Order> orders) {
                dataSource.clear();
                dataSource.addAll(orders);
                adapter.setDatasource(dataSource);
            }

            @Override
            public void onLoadFailed(Exception e) {
                Log.i(TAG, e.toString());
            }
        });


    }

    @Override
    public void OnShowDetail(Order clickedOrder) {

        DialogOrderItemLayoutBinding dialogOrderItemLayoutBinding = DialogOrderItemLayoutBinding.inflate(LayoutInflater.from(getContext()));

        String orderSummary = "Order Details:\n" + "Customer Name: " + clickedOrder.getNameCustomer() + "\n" + "Phone: " + clickedOrder.getPhoneCustomer() + "\n" + "Address: " + clickedOrder.getAddressCustomer() + "\n" + "Payment: Cash" + "\n" + "Total: $" + clickedOrder.getTotal() + "\n" + "Delivery Status: " + clickedOrder.getOrderStatus().toString();
        dialogOrderItemLayoutBinding.orderSummaryTextView.setText(orderSummary);


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(dialogOrderItemLayoutBinding.getRoot());

        bottomSheetDialog.show();
    }

    @Override
    public void OnShowUpdate(Order order) {

        Log.i(TAG, order.toString());

        DialogAdminItemDeliveryBinding dialogAdminItemDeliveryBinding = DialogAdminItemDeliveryBinding.inflate(LayoutInflater.from(getContext()));
        View dialogView = dialogAdminItemDeliveryBinding.getRoot();

        TextView orderDetailsTextView = dialogAdminItemDeliveryBinding.orderDetailsTextView;
        TextView customerNameTextView = dialogAdminItemDeliveryBinding.customerNameTextView;
        TextView phoneTextView = dialogAdminItemDeliveryBinding.phoneTextView;
        TextView addressTextView = dialogAdminItemDeliveryBinding.addressTextView;
        RadioButton processingRadioButton = dialogAdminItemDeliveryBinding.processingRadioButton;
        RadioButton completeRadioButton = dialogAdminItemDeliveryBinding.completeRadioButton;
        RadioButton canceledRadioButton = dialogAdminItemDeliveryBinding.canceledRadioButton;
        ListView listView = dialogAdminItemDeliveryBinding.listView;


        orderDetailsTextView.setText("Order Details: " + order.getIdOrder());
        customerNameTextView.setText("Customer Name: " + order.getNameCustomer());
        phoneTextView.setText("Phone: " + order.getPhoneCustomer());
        addressTextView.setText("Address: " + order.getAddressCustomer());

        String val = order.getOrderStatus().toString();
        if (processingRadioButton.getText().toString().trim().equalsIgnoreCase(val)) {
            processingRadioButton.setChecked(true);
        } else if (completeRadioButton.getText().toString().trim().equalsIgnoreCase(val)) {
            completeRadioButton.setChecked(true);
        } else if (completeRadioButton.getText().toString().trim().equalsIgnoreCase(val)) {
            canceledRadioButton.setChecked(true);
        }

        List<CartItem> cartItemList = order.getCartItemList();
        CartItemAdapter adapter = new CartItemAdapter(getContext(), cartItemList);
        listView.setAdapter(adapter);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setTitle("Update Order Details");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String id = order.getIdOrder();
                OrderStatus orderStatus = OrderStatus.PROCESSING;
                int selectedRadioButtonId = dialogAdminItemDeliveryBinding.statusRadioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioButtonId);
                    if (selectedRadioButton == processingRadioButton) {
                    } else if (selectedRadioButton == completeRadioButton) {
                        orderStatus = OrderStatus.COMPLETE;
                    } else if (selectedRadioButton == canceledRadioButton) {
                        orderStatus = OrderStatus.CANCELED;
                    }
                }

                OrderRepo.updateStatusOrder(id, orderStatus, new OrderRepo.OrderUpdateCallback() {
                    @Override
                    public void onOrderUpdated(Order updatedOrder) {
                        Log.i(TAG, "update success");
                    }

                    @Override
                    public void onOrderUpdateFailed(Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }
}