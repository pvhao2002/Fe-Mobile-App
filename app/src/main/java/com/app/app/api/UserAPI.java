package com.app.app.api;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAPI {
    @POST("login")
    Call<ResponseBody> login (@Body HashMap<String, String> loginDTO);

    @POST("register")
    Call<ResponseBody> register (@Body HashMap<String, String> registerDTO);

    @POST("update-password")
    Call<ResponseBody> updatePassword (@Body HashMap<String, String> updatePasswordDTO);

    @GET("get-all-users")
    Call<ResponseBody> getAllUsers();

    @DELETE("block-user")
    Call<ResponseBody> blockUser(@Query("id") int id);
}
