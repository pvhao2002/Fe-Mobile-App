package com.app.app.api;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CartAPI {
    @GET("get-by-user")
    Call<ResponseBody> getByUser(@Query("username") String username);

    @POST("upsert")
    Call<ResponseBody> upsert(@Body HashMap<String, Object> params);

    @DELETE("delete")
    Call<ResponseBody> delete(@Query("username") String username);
}