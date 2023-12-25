package com.example.myapplication.finalMobile.Interface;

import com.example.myapplication.finalMobile.Model.Order;

public interface OnFindOrderListener {
    void onFindOrderSuccess(Order order);

    void onFindOrderFailure(String errorMessage);
}