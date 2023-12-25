package com.example.myapplication.finalMobile.Fragments.Admin.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.myapplication.finalMobile.Activity.LoginActivity;
import com.example.myapplication.finalMobile.Adapter.Admin.HistoryAdapter;
import com.example.myapplication.finalMobile.Adapter.CartItemAdapter;
import com.example.myapplication.finalMobile.Enum.OrderStatus;
import com.example.myapplication.finalMobile.Model.CartItem;
import com.example.myapplication.finalMobile.Model.Order;
import com.example.myapplication.finalMobile.Model.User;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.OrderRepo;
import com.example.myapplication.finalMobile.Repo.UserRepo;
import com.example.myapplication.finalMobile.databinding.DialogAdminItemDeliveryBinding;
import com.example.myapplication.finalMobile.databinding.FragmentHistoryBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements HistoryAdapter.OrderAdapterLister {

    private static final String TAG = HistoryFragment.class.getSimpleName();
    FragmentHistoryBinding fragmentHistoryBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private List<Order> dataSource;
    private HistoryAdapter historyAdapter;
    private String uid;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentHistoryBinding = FragmentHistoryBinding.inflate(inflater, container, false);
        return fragmentHistoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }

        uid = user.getUid();

        dataSource = new ArrayList<Order>();
        recyclerView = fragmentHistoryBinding.rcv;
        historyAdapter = new HistoryAdapter(dataSource);
        historyAdapter.setOrderAdapterListener(this);

        if (getContext() != null) {
            recyclerView.setAdapter(historyAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        fetchData();

    }

    private void fetchData() {
        UserRepo.findOrderByIdCustomer(uid, new OrderRepo.OrderCallback() {
            @Override
            public void onOrdersLoaded(List<Order> orders) {
                dataSource.clear();
                if (!orders.isEmpty()) {
                    dataSource.addAll(orders);
                    historyAdapter.setDatasource(dataSource);
                }
            }

            @Override
            public void onLoadFailed(Exception e) {

            }
        });
    }

    @Override
    public void OnShowDetail(Order order) {
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

        processingRadioButton.setEnabled(false);
        completeRadioButton.setEnabled(false);
        canceledRadioButton.setEnabled(false);

        List<CartItem> cartItemList = order.getCartItemList();
        CartItemAdapter adapter = new CartItemAdapter(getContext(), cartItemList);
        listView.setAdapter(adapter);


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setTitle("Order Details");

        bottomSheetDialog.show();


    }

    @Override
    public void OnShowUpdate(Order order) {

    }
}