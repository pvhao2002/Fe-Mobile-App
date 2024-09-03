package com.app.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.app.adapter.CategoryAdapter;
import com.app.app.api.CategoryAPI;
import com.app.app.api.impl.CategoryApiImpl;
import com.app.app.callback.CategoryCallBack;
import com.app.app.model.Category;
import com.app.app.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity implements CategoryCallBack {
    TextView btnBack, btnAdd;
    RecyclerView recyclerViewCategory;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;
    CategoryAPI categoryAPI = CategoryApiImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        recyclerViewCategory = findViewById(R.id.recycleViewCategory);
        btnBack = findViewById(R.id.textViewBackProductInfo);
        btnAdd = findViewById(R.id.textViewAddProduct);
        categories = new ArrayList<>();
        initData();
        btnBack.setOnClickListener(v -> finish());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryActivity.this, 3, GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        categoryAdapter = new CategoryAdapter(categories, this, CategoryActivity.this);
        recyclerViewCategory.setAdapter(categoryAdapter);
        btnAdd.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), CategoryInfoActivity.class);
            intent.putExtra(Constant.IS_ADD, true);
            Category cate = new Category();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.CATE, cate);
            intent.putExtras(bundle);
            directToCategoryInfo(intent);
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            categories.clear();
            initData();
            categoryAdapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    categories.clear();
                    initData();
                    categoryAdapter.notifyDataSetChanged();
                }
            });

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
                        Toast.makeText(CategoryActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    Toast.makeText(CategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void directToCategoryInfo(Intent intent) {
        someActivityResultLauncher.launch(intent);
    }
}