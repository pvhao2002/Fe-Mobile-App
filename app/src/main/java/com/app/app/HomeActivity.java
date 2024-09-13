package com.app.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.app.adapter.ButtonFunctionAdapter;
import com.app.app.adapter.HomeAdapter;
import com.app.app.api.CategoryAPI;
import com.app.app.api.impl.CategoryApiImpl;
import com.app.app.model.ButtonFunction;
import com.app.app.model.Category;
import com.app.app.model.User;
import com.app.app.utils.Constant;
import com.app.app.utils.SharedPrefManager;

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

public class HomeActivity extends AppCompatActivity {
    CategoryAPI categoryAPI = CategoryApiImpl.getInstance();
    ImageView imageViewLogout;
    RecyclerView recyclerViewCategory, recyclerViewBtn;
    ArrayList<Category> categories;
    HomeAdapter categoryAdapter;
    Toast toast;
    TextView textView2;
    User currentUser;
    ArrayList<ButtonFunction> buttonFunctions = new ArrayList<>();
    ButtonFunctionAdapter buttonFunctionAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        currentUser = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        initButtonFunction();

        imageViewLogout = findViewById(R.id.imageViewLogout);
        recyclerViewCategory = findViewById(R.id.recyclerViewTable);
        textView2 = findViewById(R.id.textView2);
        recyclerViewBtn = findViewById(R.id.recyleBtn);
        textView2.setText(currentUser.getFullName());
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        categories = new ArrayList<>();
        initData();
        imageViewLogout.setOnClickListener(v -> goToScreen(ProfileActivity.class));


        GridLayoutManager gridLayoutManagerBtn = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerViewBtn.setLayoutManager(gridLayoutManagerBtn);
        buttonFunctionAdapter = new ButtonFunctionAdapter(buttonFunctions, HomeActivity.this);
        recyclerViewBtn.setAdapter(buttonFunctionAdapter);

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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
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
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initButtonFunction() {
        if(Constant.ROLE_ADMIN.equalsIgnoreCase(currentUser.getRole())) {
            buttonFunctions.add(new ButtonFunction("Quản lý người dùng", UserManagerActivity.class));
            buttonFunctions.add(new ButtonFunction("Quản lý danh mục", CategoryActivity.class));
            buttonFunctions.add(new ButtonFunction("Quản lý sản phẩm", ProductActivity.class));
            buttonFunctions.add(new ButtonFunction("Quản lý đơn hàng", OrderActivity.class));
        } else {
            buttonFunctions.add(new ButtonFunction("Giỏ hàng", CartActivity.class));
            buttonFunctions.add(new ButtonFunction("Đơn hàng đã mua", MyOrderActivity.class));
        }
    }

    private void goToScreen(Class<?> cls) {
        startActivity(new Intent(HomeActivity.this, cls));
    }
}