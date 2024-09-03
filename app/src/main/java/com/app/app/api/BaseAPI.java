package com.app.app.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseAPI {
    protected static final String BASE_HREF = "http://192.168.1.7:8080/api/";
    private static HttpLoggingInterceptor sLogging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder sHttpClient = new OkHttpClient.Builder();

    protected static <S> S createService(Class<S> serviceClass, String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        if (!sHttpClient.interceptors().contains(sLogging)) {
            sHttpClient.addInterceptor(sLogging);
            builder.client(sHttpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
}
