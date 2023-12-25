package com.example.myapplication.finalMobile.Service;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }
}