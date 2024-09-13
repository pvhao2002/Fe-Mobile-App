package com.app.app.adapter;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.R;
import com.app.app.callback.CartCallback;
import com.app.app.model.Cart;
import com.app.app.model.CartItem;
import com.bumptech.glide.Glide;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Cart cart;
    CartCallback cartCallback;
    ArrayList<CartItem> cartItemArray;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public CartAdapter(Cart c, CartCallback cartCallback) {
        this.cart = c;
        this.cartCallback = cartCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_cart, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItemArray.get(position);
        holder.textViewProductName.setText(MessageFormat.format("{0}\n{1}",
                "Tên sản phẩm",
                cartItem.getProduct().getName())
        );
        holder.textViewProductPrice.setText(Html.fromHtml(MessageFormat.format("{0}\n{1}\n<del>{2}</del>",
                "Giá",
                CURRENCY_FORMAT.format(cartItem.getProduct().getNetPrice()),
                CURRENCY_FORMAT.format(cartItem.getProduct().getPrice())),
                Html.FROM_HTML_MODE_COMPACT)
        );
        holder.textViewProductQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.textViewProductTotalPrice.setText(MessageFormat.format("{0}\n{1}",
                "Tổng tiền",
                CURRENCY_FORMAT.format(cartItem.getTotal()))
        );
        Glide.with(holder.imageViewProduct.getContext()).load(cartItem.getProduct().getThumbnail()).into(holder.imageViewProduct);

        Integer productId = cartItem.getProduct().getProductId();

        holder.textViewAddQuantity.setOnClickListener(v -> updateCart(productId, 1));

        holder.textViewSubQuantity.setOnClickListener(v -> updateCart(productId, -1));
    }

    private void updateCart(Integer pid, Integer quantity) {
        cartCallback.onCartUpdated(pid, quantity);
    }

    @Override
    public int getItemCount() {
        return cartItemArray == null ? 0 : cartItemArray.size();
    }

    public void setCart(Cart cart) {
        this.cart = cart;

        if (this.cart != null && this.cart.getCartItems() != null) {
            this.cartItemArray = this.cart.getCartItems();
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName, textViewProductPrice, textViewProductQuantity, textViewProductTotalPrice, textViewAddQuantity, textViewSubQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewCart);
            textViewProductName = itemView.findViewById(R.id.textViewProductNameCart);
            textViewProductPrice = itemView.findViewById(R.id.textViewPriceOfProduct);
            textViewProductQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewProductTotalPrice = itemView.findViewById(R.id.textViewTotalItem);
            textViewAddQuantity = itemView.findViewById(R.id.textViewAddQuantity);
            textViewSubQuantity = itemView.findViewById(R.id.textViewMinusQuantity);
        }
    }
}
