package com.app.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.app.adapter.ProductAdapter;
import com.app.app.api.ProductAPI;
import com.app.app.api.impl.ProductApiImpl;
import com.app.app.callback.ProductCallback;
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

public class ProductActivity extends AppCompatActivity implements ProductCallback {
    TextView textViewBackProductInfo, textViewAddProduct;
    ArrayList<Product> products = new ArrayList<>();

    ProductAPI productAPI = ProductApiImpl.getInstance();
    ProductAdapter productAdapter;
    RecyclerView recyclerViewProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        textViewBackProductInfo = findViewById(R.id.textViewBackProductInfo);
        textViewAddProduct = findViewById(R.id.textViewAddProduct);
        recyclerViewProduct = findViewById(R.id.recycleViewProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerViewProduct.setLayoutManager(gridLayoutManager);
        productAdapter = new ProductAdapter(products);
        recyclerViewProduct.setAdapter(productAdapter);
        getListProduct();
        textViewBackProductInfo.setOnClickListener(v -> finish());
        textViewAddProduct.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), ProductInfoActivity.class);
            intent.putExtra(Constant.IS_ADD, true);
            openProductInfo(intent);
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            products.clear();
            getListProduct();
            productAdapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });
    }

    private void getListProduct() {
        productAPI.getAll().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObj = dataArray.getJSONObject(i);
                            Product product = new Product(jsonObj);
                            products.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ProductActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.e("errere", obj.getString("message"));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.e("errere", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("errere", t.getMessage());
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    products.clear();
                    getListProduct();
                    productAdapter.notifyDataSetChanged();
                }
            });

    @Override
    public void openProductInfo(Intent intent) {
        someActivityResultLauncher.launch(intent);
    }
}