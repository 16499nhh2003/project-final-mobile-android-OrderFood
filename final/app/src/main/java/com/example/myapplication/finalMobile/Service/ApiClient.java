package com.example.myapplication.finalMobile.Service;

import com.example.myapplication.finalMobile.Utils.Contrainst.Contraints;
import com.example.myapplication.finalMobile.Utils.Contrainst.UrlList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class    ApiClient {
    private static final String BASE_URL = UrlList.Base;
    private static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

}
