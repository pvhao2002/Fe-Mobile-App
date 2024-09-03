package com.app.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.app.api.UserAPI;
import com.app.app.api.impl.UserApiImpl;
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

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etUsername, etPassword;
    TextView textViewResetPassword;

    UserAPI api = UserApiImpl.getInstance();
    private final String LOGIN_FAIL = "Đăng nhập thất bại";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Intent getPermission = new Intent();
                getPermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getPermission);
            }
        }

        if (SharedPrefManager.getInstance(MainActivity.this).isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
            return;
        }

        btnLogin = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        textViewResetPassword = findViewById(R.id.txtForgotPassword);

        textViewResetPassword.setOnClickListener(v -> Toast.makeText(this, "Chức năng đang được phát triển", Toast.LENGTH_SHORT).show());

        btnLogin.setOnClickListener(e -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if (username.isEmpty()) {
                etUsername.setError("Vui lòng nhập tên đăng nhập");
                etUsername.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Mật khẩu không được để trống");
                etPassword.requestFocus();
                return;
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("password", password);

            api.login(map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                JSONObject objData = new JSONObject(jsonObject.getString("data"));
                                User user = new User(objData);
                                SharedPrefManager.getInstance(MainActivity.this).userLogin(user);
                                Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException | JSONException ex) {
                            Toast.makeText(MainActivity.this, LOGIN_FAIL, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, LOGIN_FAIL, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, LOGIN_FAIL, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}