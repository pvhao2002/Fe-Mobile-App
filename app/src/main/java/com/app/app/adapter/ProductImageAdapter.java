package com.app.app.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ProductImageViewHolder> {
    private ArrayList<Uri> uriArrayList;
    private ArrayList<String> stringArrayList;
    private boolean isUri;

    public ProductImageAdapter(ArrayList<Uri> uriArrayList, ArrayList<String> stringArrayList, boolean isUri) {
        this.uriArrayList = uriArrayList;
        this.stringArrayList = stringArrayList;
        this.isUri = isUri;
    }

    @NonNull
    @Override
    public ProductImageAdapter.ProductImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_product, parent, false);
        return new ProductImageAdapter.ProductImageViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageAdapter.ProductImageViewHolder holder, int position) {
        if (isUri)
            holder.imageView.setImageURI(uriArrayList.get(position));
        else
            Glide.with(holder.itemView.getContext()).load(stringArrayList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return isUri ? uriArrayList.size() : stringArrayList.size();
    }

    public void setUriArrayList(boolean isUri, ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
        this.isUri = isUri;
        notifyDataSetChanged();
    }

    public class ProductImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ProductImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
