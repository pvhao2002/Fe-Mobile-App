package com.app.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.CategoryInfoActivity;
import com.app.app.ProductInfoActivity;
import com.app.app.R;
import com.app.app.callback.ProductCallback;
import com.app.app.model.Product;
import com.app.app.utils.Constant;
import com.bumptech.glide.Glide;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    ArrayList<Product> productArrayList;
    Context context;
    ProductCallback productCallback;
    public ProductAdapter(ArrayList<Product> productArrayList, Context context, ProductCallback productCallback) {
        this.productArrayList = productArrayList;
        this.context = context;
        this.productCallback = productCallback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        Glide.with(holder.itemView.getContext()).load(product.getThumbnail()).into(holder.productImage);
        holder.productName.setText(product.getName());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        holder.productPrice.setText(currencyFormat.format(product.getPrice()));

        String textPriceHtml = MessageFormat.format("<del>{0}</del> \t <font color=\"#FF0000\">{1}</font>", currencyFormat.format(product.getPrice()), currencyFormat.format(product.getNetPrice()));
        holder.productPrice.setText(Html.fromHtml(textPriceHtml, HtmlCompat.FROM_HTML_MODE_LEGACY));

        holder.addToCart.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.PRODUCT, product);
            intent.putExtras(bundle);
            intent.putExtra(Constant.IS_ADD, false);
            productCallback.openProductInfo(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, addToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageProductRowItem);
            productName = itemView.findViewById(R.id.textProductName);
            productPrice = itemView.findViewById(R.id.textPriceProduct);
            addToCart = itemView.findViewById(R.id.textViewAddToCart);
        }
    }
}
