package com.example.myapplication.finalMobile.Service;

import com.example.myapplication.finalMobile.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/users")
    Call<List<User>> getUsers();

    @PUT("/user/{id}")
    Call<User> updateUser(@Path("id") String userId, @Body User updatedUser);

    @DELETE("/users/{id}")
    Call<ApiResponse> deleteUser(@Path("id") String userId);
}