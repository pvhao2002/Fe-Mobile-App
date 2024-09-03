package com.app.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.app.utils.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {
    TextView btnLogout, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.textViewBackProductInfo);
        btnLogout.setOnClickListener(v -> logout());
        btnBack.setOnClickListener(v -> finish());
    }

    private void logout() {
        SharedPrefManager.getInstance(ProfileActivity.this).logout();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }
}