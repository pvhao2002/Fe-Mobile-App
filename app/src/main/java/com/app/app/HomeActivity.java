package com.app.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.app.adapter.HomeAdapter;
import com.app.app.api.CategoryAPI;
import com.app.app.api.impl.CategoryApiImpl;
import com.app.app.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    CategoryAPI categoryAPI = CategoryApiImpl.getInstance();
    Button btnCategory, btnProduct, btnOrder, btnStatistic;
    ImageView imageViewLogout;
    RecyclerView recyclerViewCategory;
    ArrayList<Category> categories;
    HomeAdapter categoryAdapter;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnCategory = findViewById(R.id.btnCategory);
        btnProduct = findViewById(R.id.btnProduct);
        btnOrder = findViewById(R.id.btnOrder);
        btnStatistic = findViewById(R.id.btnStatistic);
        imageViewLogout = findViewById(R.id.imageViewLogout);
        recyclerViewCategory = findViewById(R.id.recyclerViewTable);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        categories = new ArrayList<>();
        initData();
        btnCategory.setOnClickListener(v -> goToScreen(CategoryActivity.class));
        btnProduct.setOnClickListener(v -> goToScreen(ProductActivity.class));
        btnOrder.setOnClickListener(v -> goToScreen(OrderActivity.class));
        imageViewLogout.setOnClickListener(v -> goToScreen(ProfileActivity.class));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        categoryAdapter = new HomeAdapter(categories, HomeActivity.this);
        recyclerViewCategory.setAdapter(categoryAdapter);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            categories.clear();
            initData();
            categoryAdapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });
    }
    private synchronized void initData() {
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
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(HomeActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void goToScreen(Class<?> cls) {
        startActivity(new Intent(HomeActivity.this, cls));
    }
}