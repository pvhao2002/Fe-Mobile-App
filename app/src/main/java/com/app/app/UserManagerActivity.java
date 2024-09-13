package com.app.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.app.adapter.UserAdapter;
import com.app.app.api.UserAPI;
import com.app.app.api.impl.UserApiImpl;
import com.app.app.callback.UserCallback;
import com.app.app.model.User;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagerActivity extends AppCompatActivity implements UserCallback {

    ArrayList<User> userArrayList;
    UserAPI userAPI = UserApiImpl.getInstance();
    TextView textViewBackUserMana;
    RecyclerView recycleViewUserMana;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        userArrayList = new ArrayList<>();
        initData();
        textViewBackUserMana = findViewById(R.id.textViewBackUserMana);
        recycleViewUserMana = findViewById(R.id.recycleViewUserMana);

        textViewBackUserMana.setOnClickListener(v -> finish());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserManagerActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recycleViewUserMana.setLayoutManager(gridLayoutManager);
        userAdapter = new UserAdapter(userArrayList, this);
        recycleViewUserMana.setAdapter(userAdapter);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            userArrayList.clear();
            initData();
            pullToRefresh.setRefreshing(false);
        });
    }

    private void initData() {
        userAPI.getAllUsers().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObj = dataArray.getJSONObject(i);
                            User user = new User(jsonObj);
                            userArrayList.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(UserManagerActivity.this, StringUtils.defaultIfBlank(obj.getString("data"), "Lỗi"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    Toast.makeText(UserManagerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(UserManagerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBlockClick(int id) {
        // Block user
        showConfirmationDialog(id);
    }

    private void showConfirmationDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserManagerActivity.this);
        builder.setTitle("Khóa tài khoản")
                .setMessage("Bạn có muốn khóa tài khoản này không?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    userAPI.blockUser(id).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject obj = new JSONObject(response.body().string());
                                if (obj.getBoolean("success")) {
                                    Toast.makeText(UserManagerActivity.this, obj.getString("data"), Toast.LENGTH_SHORT).show();
                                    userArrayList.clear();
                                    initData();
                                } else {
                                    Toast.makeText(UserManagerActivity.this, StringUtils.defaultIfBlank(obj.getString("data"), "Lỗi"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | IOException e) {
                                Toast.makeText(UserManagerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(UserManagerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }
}