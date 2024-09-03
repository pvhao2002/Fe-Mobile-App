package com.app.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.app.app.adapter.ShopAdapter;
import com.app.app.api.ProductAPI;
import com.app.app.api.impl.ProductApiImpl;
import com.app.app.model.Product;
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

public class ShopActivity extends AppCompatActivity {
    RecyclerView recycleViewProductByCate;
    TextView btnBack;
    ArrayList<Product> productArrayList;
    ShopAdapter shopAdapter;

    ProductAPI productAPI = ProductApiImpl.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        productArrayList = new ArrayList<>();
        shopAdapter = new ShopAdapter(productArrayList);
        getListProductByCategory(getIntent().getIntExtra(Constant.CATE, 0));
        recycleViewProductByCate = findViewById(R.id.recycleViewProductByCate);
        btnBack = findViewById(R.id.textViewBackShop);

        btnBack.setOnClickListener(v -> finish());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ShopActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycleViewProductByCate.setLayoutManager(gridLayoutManager);
        recycleViewProductByCate.setAdapter(shopAdapter);


        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            productArrayList.clear();
            getListProductByCategory(getIntent().getIntExtra(Constant.CATE, 0));
            shopAdapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });
    }

    private void getListProductByCategory(Integer id) {
        productAPI.getByCategory(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObj = dataArray.getJSONObject(i);
                            Product product = new Product(jsonObj);
                            productArrayList.add(product);
                        }
                        shopAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ShopActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.e("errere", obj.getString("message"));
                    }
                } catch (IOException | JSONException e) {
                    Toast.makeText(ShopActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("errere", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ShopActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("errere", t.getMessage());
            }
        });
    }
}