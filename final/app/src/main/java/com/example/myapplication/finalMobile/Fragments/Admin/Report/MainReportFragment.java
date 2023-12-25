package com.example.myapplication.finalMobile.Fragments.Admin.Report;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.finalMobile.Model.Order;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.Repo.OrderRepo;
import com.example.myapplication.finalMobile.databinding.FragmentMainReportBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainReportFragment extends Fragment {

    private static final String TAG = MainReportFragment.class.getSimpleName();
    private FragmentMainReportBinding fragmentMainReportBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainReportFragment newInstance(String param1, String param2) {
        MainReportFragment fragment = new MainReportFragment();
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
        fragmentMainReportBinding = FragmentMainReportBinding.inflate(inflater, container, false);
        return fragmentMainReportBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LineChart lineChart = fragmentMainReportBinding.chart;
        PieChart pieChart = fragmentMainReportBinding.pieChart;


        OrderRepo.getAllOrder(new OrderRepo.OrderCallback() {
            @Override
            public void onOrdersLoaded(List<Order> orderList) {
                List<Entry> entries = new ArrayList<>();
                for (int i = 0; i < orderList.size(); i++) {
                    Order order = orderList.get(i);
                    entries.add(new Entry(i, (float) order.getTotal()));
                }


                LineDataSet dataSet = new LineDataSet(entries, "Total Amount");
                dataSet.setColor(Color.BLUE);
                dataSet.setCircleColor(Color.RED);
                dataSet.setDrawValues(true);

                LineData lineData = new LineData(dataSet);

                lineChart.setData(lineData);
                lineChart.invalidate();

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        if (value >= 0 && value < orderList.size()) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                            return dateFormat.format(orderList.get((int) value).getCreatedAt());
                        }
                        return "";
                    }
                });

                xAxis.setLabelRotationAngle(45);
                xAxis.setGranularity(1f);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


                List<PieEntry> pieEntries = new ArrayList<>();
                for (Order order : orderList) {
                    pieEntries.add(new PieEntry((float) order.getTotal(), order.getTotal()));
                }

                PieDataSet pieDataSet = new PieDataSet(pieEntries, "Total Amount");
                pieDataSet.setColors(Color.BLUE, Color.GREEN, Color.RED);
                pieDataSet.setSliceSpace(3f);
                pieDataSet.setSelectionShift(5f);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();

            }

            @Override
            public void onLoadFailed(Exception e) {

            }
        });


    }
}