package com.app.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.app.api.UserAPI;
import com.app.app.api.impl.UserApiImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RegisterActivity extends AppCompatActivity {

    TextView txtLogin;
    Button btnSignup;
    EditText editTextUserName, editTextPassword, editTextFullName, editTextConfirmPassword;

    UserAPI api = UserApiImpl.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtLogin = findViewById(R.id.txtLogin);
        btnSignup = findViewById(R.id.btnSignup);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        txtLogin.setOnClickListener(v -> finish());
        btnSignup.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        String username = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (username.isEmpty()) {
            editTextUserName.setError("Tên đăng nhập không được để trống");
            editTextUserName.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Mật khẩu không được để trống");
            editTextPassword.requestFocus();
            return;
        }

        if (fullName.isEmpty()) {
            editTextFullName.setError("Họ tên không được để trống");
            editTextFullName.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Mật khẩu không khớp");
            editTextConfirmPassword.requestFocus();
            return;
        }

        // Call API to register user
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("fullName", fullName);
        api.register(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String s = response.body().string();
                        JSONObject object = new JSONObject(s);
                        if (object.getBoolean("success")) {
                            Toast.makeText(RegisterActivity.this, object.getString("data"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, object.getString("data"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e("RegisterActivity", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("RegisterActivity", t.getMessage());
            }
        });
    }
}