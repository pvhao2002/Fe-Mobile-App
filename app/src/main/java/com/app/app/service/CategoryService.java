package com.app.app.service;

import android.content.Context;
import android.widget.Toast;

import com.app.app.CategoryActivity;
import com.app.app.api.CategoryAPI;
import com.app.app.api.impl.CategoryApiImpl;
import com.app.app.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryService {
    CategoryAPI categoryAPI = CategoryApiImpl.getInstance();

    private static CategoryService instance;

    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
        }
        return instance;
    }

    public synchronized ArrayList<Category> getAll(Toast toast, Context context) {
        ArrayList<Category> categories = new ArrayList<>();
        categoryAPI.getAll().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObj = dataArray.getJSONObject(i);
                            Category category = new Category(jsonObj);
                            categories.add(category);
                        }
                    } else {
                        toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return categories;
    }
}
