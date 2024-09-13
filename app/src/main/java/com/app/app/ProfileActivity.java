package com.app.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ProfileActivity extends AppCompatActivity {
    TextView btnLogout, btnBack, btnUpdatePassword;
    UserAPI api = UserApiImpl.getInstance();
    EditText editTextFullName, editTextPassword, editTextNewPassword, editTextConfirmPassword;
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentUser = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.textViewBackProductInfo);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        editTextFullName = findViewById(R.id.editTextFullname);
        editTextPassword = findViewById(R.id.editTextTextCurrentPassword);
        editTextNewPassword = findViewById(R.id.editTextTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextTextConfirmPassword);

        editTextFullName.setText(currentUser.getFullName());
        btnUpdatePassword.setOnClickListener(v -> updatePassword());


        btnLogout.setOnClickListener(v -> logout());
        btnBack.setOnClickListener(v -> finish());
    }

    private void updatePassword() {
        String fullName = editTextFullName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Tên không được để trống");
            editTextFullName.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Mật khẩu không được để trống");
            editTextPassword.requestFocus();
            return;
        }

        if(!password.equals(currentUser.getPassword())){
            editTextPassword.setError("Mật khẩu không đúng");
            editTextPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            editTextNewPassword.setError("Mật khẩu mới không được để trống");
            editTextNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if(password.equals(newPassword)){
            editTextNewPassword.setError("Mật khẩu mới không được trùng với mật khẩu cũ");
            editTextNewPassword.requestFocus();
            return;
        }

        HashMap<String, String> updatePasswordDTO = new HashMap<>();
        updatePasswordDTO.put("username", currentUser.getUsername());
        updatePasswordDTO.put("fullName", fullName);
        updatePasswordDTO.put("newPassword", newPassword);

        api.updatePassword(updatePasswordDTO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getBoolean("success")) {
                            Toast.makeText(ProfileActivity.this, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            logout();
                        } else {
                            Toast.makeText(ProfileActivity.this, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Log.e("ProfileActivity", e.getMessage());
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        SharedPrefManager.getInstance(ProfileActivity.this).logout();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }
}