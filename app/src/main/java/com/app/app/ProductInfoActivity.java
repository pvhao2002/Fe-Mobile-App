package com.app.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.adapter.ProductImageAdapter;
import com.app.app.api.CategoryAPI;
import com.app.app.api.ProductAPI;
import com.app.app.api.impl.CategoryApiImpl;
import com.app.app.api.impl.ProductApiImpl;
import com.app.app.model.Category;
import com.app.app.model.Product;
import com.app.app.model.ProductImage;
import com.app.app.utils.Constant;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView btnChooseImage, btnAdd, btnDel, btnBack;
    EditText editTextName, editTextPrice, editTextDiscount;
    private List<Uri> imageUris;
    ArrayList<String> stringArrayList;
    Product product;
    boolean isAdd;
    boolean isUri;
    RecyclerView recyclerView;
    ProductImageAdapter productImageAdapter;
    ProductAPI productAPI = ProductApiImpl.getInstance();
    CategoryAPI categoryAPI = CategoryApiImpl.getInstance();
    ArrayList<Category> listCategory;
    Spinner spinnerCategory;
    Category selectedCategory;
    ProgressBar progressBarProduct;
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                if (imageUris != null) {
                    imageUris.clear();
                }
                if (stringArrayList != null) {
                    stringArrayList.clear();
                }
                if (data.getClipData() != null) {
                    // Đọc nhiều hình ảnh
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                } else if (data.getData() != null) {
                    // Đọc một hình ảnh duy nhất
                    Uri selectedImageUri = data.getData();
                    imageUris.add(selectedImageUri);
                }
                productImageAdapter.setUriArrayList(true, (ArrayList<Uri>) imageUris);
                productImageAdapter.notifyDataSetChanged();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        isUri = true;
        imageUris = new ArrayList<>();
        listCategory = new ArrayList<>();

        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnAdd = findViewById(R.id.btnUpsertProduct);
        btnDel = findViewById(R.id.btnDelProduct);
        btnBack = findViewById(R.id.textViewBackProductInfo);
        editTextName = findViewById(R.id.editTextProductName);
        editTextPrice = findViewById(R.id.editTextPriceProduct);
        editTextDiscount = findViewById(R.id.editTextDiscountProduct);
        recyclerView = findViewById(R.id.recycleProductImage);
        spinnerCategory = findViewById(R.id.spinnerCategoryProduct);
        product = (Product) getIntent().getSerializableExtra(Constant.PRODUCT);
        progressBarProduct = findViewById(R.id.progressBarProduct);
        isAdd = getIntent().getBooleanExtra(Constant.IS_ADD, true);

        if (!isAdd) {
            editTextName.setText(product.getName());
            editTextPrice.setText(product.getPrice().toString());
            editTextDiscount.setText(String.valueOf(product.getDiscount()));
            stringArrayList = (ArrayList<String>) product.getImages().stream().map(ProductImage::getUrl).collect(Collectors.toList());
            isUri = false;
            btnAdd.setText("Cập nhật");
        } else {
            btnDel.setVisibility(View.GONE);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductInfoActivity.this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        productImageAdapter = new ProductImageAdapter((ArrayList<Uri>) imageUris, stringArrayList, isUri);
        recyclerView.setAdapter(productImageAdapter);
        initDataCate();

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple image selection
            imagePickerLauncher.launch(intent);
        });

        btnAdd.setOnClickListener(v -> {
            // Add product
            if (StringUtils.isBlank(editTextName.getText().toString())) {
                editTextName.setError("Tên không được để trống");
                editTextName.requestFocus();
                return;
            }
            if (StringUtils.isBlank(editTextPrice.getText().toString())) {
                editTextPrice.setError("Giá không được để trống");
                editTextPrice.requestFocus();
                return;
            }
            if (StringUtils.isBlank(editTextDiscount.getText().toString())) {
                editTextDiscount.setError("Giảm giá không được để trống");
                editTextDiscount.requestFocus();
                return;
            }
            if (imageUris.size() == 0) {
                editTextName.setError("Chưa chọn hình ảnh");
                editTextName.requestFocus();
                return;
            }
            upsertProductFunction();
        });
        btnDel.setOnClickListener(v -> showConfirmationDialog());

        btnBack.setOnClickListener(v -> {
            Log.e("Test", "Back");
            editTextPrice.setText(imageUris == null ? 0 : imageUris.size());
        });
    }

    private void initDataCate() {
        progressBarProduct.setVisibility(ProgressBar.VISIBLE);
        categoryAPI.getAll().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressBarProduct.setVisibility(ProgressBar.GONE);
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObj = dataArray.getJSONObject(i);
                            Category category = new Category(jsonObj);
                            listCategory.add(category);
                        }
                        if (listCategory.size() > 0) {
                            selectedCategory = listCategory.get(0);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ProductInfoActivity.this, android.R.layout.simple_spinner_item);
                        for (Category category : listCategory) {
                            adapter.add(category.getName());
                        }
                        spinnerCategory.setAdapter(adapter);
                        spinnerCategory.setOnItemSelectedListener(ProductInfoActivity.this);
                    } else {
                        Toast.makeText(ProductInfoActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    Toast.makeText(ProductInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("errere", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductInfoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("errere", t.getMessage());
            }
        });
    }

    private void upsertProductFunction() {
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri uri : imageUris) {
            String filePath = getRealPathFromURI(uri);
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
            imageParts.add(body);
        }
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), editTextName.getEditableText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), new BigDecimal(editTextPrice.getEditableText().toString()).toString());
        RequestBody discount = RequestBody.create(MediaType.parse("text/plain"), new BigDecimal(editTextDiscount.getEditableText().toString()).toString());
        RequestBody cateId = RequestBody.create(MediaType.parse("text/plain"), selectedCategory != null ? String.valueOf(selectedCategory.getCategoryId()) : "");
        Map<String, RequestBody> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);
        params.put("discount", discount);
        params.put("cateId", cateId);

        if (!isAdd) {
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(product.getProductId()));
            params.put("id", id);
        }
        progressBarProduct.setVisibility(ProgressBar.VISIBLE);

        productAPI.upsertProduct(params, imageParts).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressBarProduct.setVisibility(ProgressBar.GONE);
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getBoolean("success")) {
                        Toast.makeText(ProductInfoActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ProductInfoActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } catch (JSONException | IOException e) {
                    Toast.makeText(ProductInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("errere", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductInfoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("errere", t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCategory = listCategory.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return StringUtils.EMPTY;
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Xóa")
                .setMessage("Bạn có muốn xóa sản phẩm này không?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (product != null) {
                        productAPI.deleteProduct(product.getProductId()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    JSONObject obj = new JSONObject(response.body().string());
                                    if (obj.getBoolean("success")) {
                                        Toast.makeText(ProductInfoActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ProductInfoActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException | IOException e) {
                                    Toast.makeText(ProductInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("errere", e.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(ProductInfoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("errere", t.getMessage());
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}