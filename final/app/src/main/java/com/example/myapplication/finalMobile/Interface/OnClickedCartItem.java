package com.example.myapplication.finalMobile.Interface;

import com.example.myapplication.finalMobile.Model.CartItem;

import java.util.List;

public interface OnClickedCartItem {
    void OnDeleteItem(List<CartItem> cartItemList   , int position);
}
