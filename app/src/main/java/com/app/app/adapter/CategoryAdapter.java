package com.app.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.CategoryInfoActivity;
import com.app.app.R;
import com.app.app.model.Category;
import com.app.app.utils.Constant;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<Category> listCategory;
    Context context;

    public CategoryAdapter(ArrayList<Category> listCategory, Context context) {
        this.listCategory = listCategory;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_category, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = listCategory.get(position);
        holder.cateName.setText(category.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.CATE, category);
            intent.putExtras(bundle);
            intent.putExtra(Constant.IS_ADD, false);
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
