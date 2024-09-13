package com.app.app;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.app.adapter.MyOrderAdapter;
import com.app.app.api.OrderAPI;
import com.app.app.api.impl.OrderApiImpl;
import com.app.app.callback.OrderCallback;
import com.app.app.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements OrderCallback {
    ArrayList<Order> orders;
    OrderAPI orderAPI = OrderApiImpl.getInstance();
    MyOrderAdapter myOrderAdapter;
    RecyclerView recyclerView;
    TextView textViewBack;
    ProgressBar progressBarOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orders = new ArrayList<>();

        myOrderAdapter = new MyOrderAdapter(orders, true, this);
        recyclerView = findViewById(R.id.recycleViewOrder);
        textViewBack = findViewById(R.id.textViewBackOrder);
        progressBarOrder = findViewById(R.id.progressBarOrder);


        textViewBack.setOnClickListener(v -> finish());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(OrderActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(myOrderAdapter);

        init();

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            orders.clear();
            init();
            pullToRefresh.setRefreshing(false);
        });
    }

    private void init() {
        orderAPI.allOrder().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        JSONArray data = obj.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject orderObj = data.getJSONObject(i);
                            Order order = new Order(orderObj);
                            orders.add(order);
                        }
                        myOrderAdapter.setOrderArrayList(orders);
                    } else {
                        Toast.makeText(OrderActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    Toast.makeText(OrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpdateStatusOrder(Integer orderId, String status) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("status", status);
        progressBarOrder.setVisibility(ProgressBar.VISIBLE);
        orderAPI.updateStatus(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressBarOrder.setVisibility(ProgressBar.GONE);
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        orders.clear();
                        init();
                    }
                    Toast.makeText(OrderActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException | IOException e) {
                    Toast.makeText(OrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}