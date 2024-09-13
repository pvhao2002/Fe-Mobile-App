package com.app.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.app.api.CategoryAPI;
import com.app.app.api.impl.CategoryApiImpl;
import com.app.app.model.Category;
import com.app.app.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryInfoActivity extends AppCompatActivity {
    TextView textViewBackCateInfo, btnUpsert, btnDel;
    Category category;
    EditText editTextCateName;
    CategoryAPI categoryAPI = CategoryApiImpl.getInstance();
    boolean isAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_info);

        textViewBackCateInfo = findViewById(R.id.textViewBackProductInfo);
        btnUpsert = findViewById(R.id.btnUpsert);
        editTextCateName = findViewById(R.id.editTextCateName);
        btnDel = findViewById(R.id.btnDel);

        textViewBackCateInfo.setOnClickListener(v -> finish());
        category = (Category) getIntent().getSerializableExtra(Constant.CATE);
        isAdd = getIntent().getBooleanExtra(Constant.IS_ADD, false);

        btnUpsert.setText(isAdd ? "Thêm" : "Cập nhật");
        if (!isAdd) {
            editTextCateName.setText(category.getName());
        } else {
            btnDel.setVisibility(TextView.GONE);
        }

        btnUpsert.setOnClickListener(e -> {
            String name = editTextCateName.getText().toString();
            if (name.isEmpty()) {
                editTextCateName.setError("Tên không được để trống");
                editTextCateName.requestFocus();
                return;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            if (!isAdd) {
                map.put("id", category.getCategoryId());
            }
            categoryAPI.upsert(map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    handleSuccess(response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnDel.setOnClickListener(e -> showConfirmationDialog());
    }

    private void handleSuccess(Response<ResponseBody> response) {
        try {
            String res = response.body().string();
            JSONObject json = new JSONObject(res);
            if (json.getBoolean("success")) {
                Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Thất bại", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Xóa")
                .setMessage("Bạn có muốn xóa danh mục này không?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    categoryAPI.delete(category.getCategoryId()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            handleSuccess(response);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }
}