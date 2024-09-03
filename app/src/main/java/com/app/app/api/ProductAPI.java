package com.app.app.api;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ProductAPI {
    @GET("all")
    Call<ResponseBody> getAll();

    @GET("by-category")
    Call<ResponseBody> getByCategory(@Query("cid") int category);

    @Multipart
    @POST("upsert")
    Call<ResponseBody> upsertProduct(
            @PartMap Map<String, RequestBody> params,
            @Part List<MultipartBody.Part> images
    );
}
