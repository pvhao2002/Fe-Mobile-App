package com.app.app.api;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderAPI {
    @POST("checkout")
    Call<ResponseBody> checkout(@Body HashMap<String, Object> params);

    @GET("all")
    Call<ResponseBody> all(@Query("username") String username);

    @GET("all-order")
    Call<ResponseBody> allOrder();

    @PATCH("update-status")
    Call<ResponseBody> updateStatus(@Body HashMap<String, Object> params);
}
