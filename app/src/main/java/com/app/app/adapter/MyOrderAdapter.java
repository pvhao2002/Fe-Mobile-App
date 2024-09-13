package com.app.app.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.R;
import com.app.app.callback.OrderCallback;
import com.app.app.model.Order;
import com.app.app.model.Product;
import com.app.app.model.StatusOrderEnum;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    ArrayList<Order> orderArrayList;
    OrderCallback orderCallback;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    boolean isShowButton;

    public MyOrderAdapter(ArrayList<Order> orderArrayList, boolean isShowButton, OrderCallback orderCallback) {
        this.orderArrayList = orderArrayList;
        this.isShowButton = isShowButton;
        this.orderCallback = orderCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_myorder, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderArrayList.get(position);
        holder.textViewTotalMyOrder.setText("Tổng tiền: " + CURRENCY_FORMAT.format(order.getTotal()));
        holder.textViewDateMyOrder.setText(order.getOrderDate());

        int quantity = order.getOrderItems().get(0).getQuantity();
        holder.textViewQuantityMyOrder.setText("x" + quantity);

        Product product = order.getOrderItems().get(0).getProduct();
        String status = StatusOrderEnum.valueOf(order.getOrderStatus()).toString();
        holder.textViewPriceMyOrder.setText(Html.fromHtml(String.format("<font color='#05990a'>%s</font> - <del>%s</del> <font color='#FF0000'>%s</font>", status, CURRENCY_FORMAT.format(product.getPrice()), CURRENCY_FORMAT.format(product.getNetPrice())), Html.FROM_HTML_MODE_COMPACT));
        holder.textViewProductName.setText(product.getName());
        Glide.with(holder.itemView.getContext()).load(product.getThumbnail()).into(holder.imageViewProductImage);

        if (isShowButton) {
            if ("PENDING".equalsIgnoreCase(order.getOrderStatus())) {
                holder.textViewCancelOrder.setVisibility(View.VISIBLE);
                holder.textViewAcceptOrder.setVisibility(View.VISIBLE);
            } else if ("ACCEPTED".equalsIgnoreCase(order.getOrderStatus())) {
                holder.textViewAcceptOrder.setVisibility(View.VISIBLE);
                holder.textViewAcceptOrder.setText("Đã nhận hàng");
                holder.textViewAcceptOrder.setTag("DONE");
                holder.linearLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                holder.linearLayout.setWeightSum(2);
                holder.textViewCancelOrder.setVisibility(View.GONE);
            } else {
                holder.textViewCancelOrder.setVisibility(View.GONE);
                holder.textViewAcceptOrder.setVisibility(View.GONE);
            }
        } else {
            if ("PENDING".equalsIgnoreCase(order.getOrderStatus())) {
                holder.textViewCancelOrder.setVisibility(View.VISIBLE);
                holder.linearLayout.setWeightSum(2);
            } else {
                holder.textViewCancelOrder.setVisibility(View.GONE);
            }
            holder.textViewAcceptOrder.setVisibility(View.GONE);
        }
        if (orderCallback != null) {
            holder.textViewCancelOrder.setOnClickListener(v -> {
                this.orderCallback.onUpdateStatusOrder(order.getOrderId(), "CANCELLED");
            });
            holder.textViewAcceptOrder.setOnClickListener(v -> {
                Object tag = holder.textViewAcceptOrder.getTag();
                if ("DONE".equals(tag)) {
                    this.orderCallback.onUpdateStatusOrder(order.getOrderId(), "DONE");
                    return;
                }
                this.orderCallback.onUpdateStatusOrder(order.getOrderId(), "ACCEPTED");
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public void setOrderArrayList(ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTotalMyOrder, textViewDateMyOrder, textViewQuantityMyOrder, textViewPriceMyOrder, textViewProductName, textViewCancelOrder, textViewAcceptOrder;
        ImageView imageViewProductImage;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTotalMyOrder = itemView.findViewById(R.id.textViewTotalMyOrder);
            textViewDateMyOrder = itemView.findViewById(R.id.textViewDateMyOrder);
            textViewQuantityMyOrder = itemView.findViewById(R.id.textViewQuantityMyOrder);
            textViewPriceMyOrder = itemView.findViewById(R.id.textViewPriceMyOrder);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            imageViewProductImage = itemView.findViewById(R.id.imageViewProductImage);
            textViewCancelOrder = itemView.findViewById(R.id.textViewCancelOrder);
            textViewAcceptOrder = itemView.findViewById(R.id.textViewAcceptOrder);
            linearLayout = itemView.findViewById(R.id.layoutButtonOrder);
        }
    }
}
