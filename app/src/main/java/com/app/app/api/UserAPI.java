package com.app.app.api;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {
    @POST("login")
    Call<ResponseBody> login (@Body HashMap<String, String> loginDTO);
}
