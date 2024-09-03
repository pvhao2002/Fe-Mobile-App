package com.app.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.R;
import com.app.app.ShopActivity;
import com.app.app.model.Category;
import com.app.app.utils.Constant;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    ArrayList<Category> listCategory;
    Context context;

    public HomeAdapter(ArrayList<Category> listCategory, Context context) {
        this.listCategory = listCategory;
        this.context = context;
    }


    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_category, parent, false);
        return new HomeAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        Category category = listCategory.get(position);
        holder.cateName.setText(category.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShopActivity.class);
            intent.putExtra(Constant.CATE, category.getCategoryId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.listCategory == null ? 0 : this.listCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cateName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cateName = itemView.findViewById(R.id.textViewCateRowName);
        }
    }
}
