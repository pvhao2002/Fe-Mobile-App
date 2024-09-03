package com.app.app.api;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategoryAPI {
    @GET("all")
    Call<ResponseBody> getAll();

    @POST("upsert")
    Call<ResponseBody> upsert(@Body HashMap<String, Object> category);
}
