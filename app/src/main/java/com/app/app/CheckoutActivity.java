package com.app.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.app.api.OrderAPI;
import com.app.app.api.impl.OrderApiImpl;
import com.app.app.model.User;
import com.app.app.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {
    TextView textViewBackCheckout, btnContinue;
    User user;
    EditText editTextEmailCheckout, editTextPhone, editTextFullnameCheckout;
    OrderAPI orderAPI = OrderApiImpl.getInstance();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        textViewBackCheckout = findViewById(R.id.textViewBackCheckout);
        btnContinue = findViewById(R.id.btnContinue);
        editTextEmailCheckout = findViewById(R.id.editTextEmailCheckout);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextFullnameCheckout = findViewById(R.id.editTextFullnameCheckout);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnContinue.setOnClickListener(v -> {
            if (editTextEmailCheckout.getText().toString().trim().isEmpty()
                    || editTextPhone.getText().toString().trim().isEmpty()
                    || editTextFullnameCheckout.getText().toString().trim().isEmpty()) {
                editTextEmailCheckout.setError("Email is required");
                editTextPhone.setError("Phone is required");
                editTextFullnameCheckout.setError("Fullname is required");
                editTextFullnameCheckout.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            HashMap<String, Object> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("phone", editTextPhone.getText().toString().trim());
            params.put("email", editTextEmailCheckout.getText().toString().trim());
            params.put("fullName", editTextFullnameCheckout.getText().toString().trim());
            orderAPI.checkout(params).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().string();
                            JSONObject obj = new JSONObject(res);
                            if (obj.getBoolean("success")) {
                                Toast.makeText(CheckoutActivity.this, "Đặt hàng thành côgn", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CheckoutActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(CheckoutActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Log.e("error >> CheckoutActivity", e.getMessage());
                        }
                    } else {
                        Toast.makeText(CheckoutActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("error >> CheckoutActivity", response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(CheckoutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("error >> CheckoutActivity", t.getMessage());
                }
            });
        });

        textViewBackCheckout.setOnClickListener(v -> finish());

    }
}