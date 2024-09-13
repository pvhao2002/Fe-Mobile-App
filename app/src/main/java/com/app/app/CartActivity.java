package com.app.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.app.adapter.CartAdapter;
import com.app.app.api.CartAPI;
import com.app.app.api.impl.CartApiImpl;
import com.app.app.callback.CartCallback;
import com.app.app.model.Cart;
import com.app.app.model.User;
import com.app.app.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartCallback {
    TextView textViewBackCart, textViewEmpty;
    TextView textViewCartPrice, textViewCartDiscount, textViewCartTotal, btnCartCheckout, btnCartClear;
    RecyclerView recyclerViewCart;
    CartAPI api = CartApiImpl.getInstance();
    User user = SharedPrefManager.getInstance(this).getUser();
    Cart cart;
    CartAdapter cartAdapter;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cart = new Cart();

        textViewBackCart = findViewById(R.id.textViewBackCart);
        recyclerViewCart = findViewById(R.id.recyclerViewTable);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        textViewCartPrice = findViewById(R.id.textViewCartPrice);
        textViewCartDiscount = findViewById(R.id.textViewCartDiscount);
        textViewCartTotal = findViewById(R.id.textViewCartTotal);
        btnCartCheckout = findViewById(R.id.btnCartCheckout);
        btnCartClear = findViewById(R.id.btnCartClear);

        if (user.getUserId() == 0) {
            startActivity(new Intent(CartActivity.this, MainActivity.class));
        }
        init();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CartActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerViewCart.setLayoutManager(gridLayoutManager);
        cartAdapter = new CartAdapter(cart, this);
        recyclerViewCart.setAdapter(cartAdapter);

        textViewBackCart.setOnClickListener(v -> finish());

        btnCartClear.setOnClickListener(v -> {
            api.delete(user.getUsername()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        if (obj.getBoolean("success")) {
                            init();
                            Toast.makeText(CartActivity.this, obj.getString("data"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CartActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnCartCheckout.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, CheckoutActivity.class)));

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            init();
            pullToRefresh.setRefreshing(false);
        });
    }

    private void init() {
        api.getByUser(user.getUsername()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        JSONObject data = obj.getJSONObject("data");
                        if (data == null || data.length() == 0) {
                            textViewEmpty.setText("Giỏ hàng trống");
                            textViewEmpty.setVisibility(TextView.VISIBLE);
                            btnCartCheckout.setEnabled(false);
                            btnCartClear.setEnabled(false);
                        } else {
                            textViewEmpty.setText("");
                            textViewEmpty.setVisibility(TextView.GONE);
                            cart = new Cart(data);
                            cartAdapter.setCart(cart);
                            textViewCartPrice.setText(CURRENCY_FORMAT.format(cart.getTotalPrice()));
                            textViewCartDiscount.setText(CURRENCY_FORMAT.format(cart.getDiscount()));
                            textViewCartTotal.setText(CURRENCY_FORMAT.format(cart.getTotal()));
                        }
                    } else {
                        textViewEmpty.setText("Giỏ hàng trống");
                        textViewEmpty.setVisibility(TextView.VISIBLE);
                        btnCartCheckout.setEnabled(false);
                        btnCartClear.setEnabled(false);
                        Toast.makeText(CartActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCartUpdated(Integer productId, Integer quantity) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("productId", productId);
        params.put("quantity", quantity);
        api.upsert(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        init();
                    } else {
                        Toast.makeText(CartActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("errere", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("errere", t.getMessage());
            }
        });
    }

}