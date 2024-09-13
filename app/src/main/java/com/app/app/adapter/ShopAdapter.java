package com.app.app.adapter;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.R;
import com.app.app.callback.CartCallback;
import com.app.app.model.Product;
import com.app.app.model.User;
import com.app.app.utils.Constant;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    ArrayList<Product> productArrayList;
    User currentUser;
    CartCallback cartCallback;

    public ShopAdapter(ArrayList<Product> productArrayList, User currentUser, CartCallback cartCallback) {
        this.productArrayList = productArrayList;
        this.currentUser = currentUser;
        this.cartCallback = cartCallback;
    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        Glide.with(holder.itemView.getContext()).load(product.getThumbnail()).into(holder.productImage);
        holder.productName.setText(product.getName());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Format giá
        String oldPrice = currencyFormat.format(product.getPrice());
        String newPrice = currencyFormat.format(product.getNetPrice());

        String textPriceHtml = String.format("<del>%s</del> <font color='#FF0000'>%s</font>",
                oldPrice,
                newPrice
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.productPrice.setText(Html.fromHtml(textPriceHtml, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else {
            holder.productPrice.setText(Html.fromHtml(textPriceHtml));
        }

        holder.addToCart.setVisibility(Constant.ROLE_ADMIN.equalsIgnoreCase(currentUser.getRole()) ? View.GONE : View.VISIBLE);

        holder.addToCart.setOnClickListener(v -> cartCallback.onCartUpdated(product.getProductId(), 1));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, addToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageProductRowItem);
            productName = itemView.findViewById(R.id.textProductName);
            productPrice = itemView.findViewById(R.id.textPriceProduct);
            addToCart = itemView.findViewById(R.id.textViewAddToCart);
        }
    }
}
