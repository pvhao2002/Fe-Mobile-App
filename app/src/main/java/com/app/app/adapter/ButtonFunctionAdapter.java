package com.app.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.R;
import com.app.app.model.ButtonFunction;

import java.util.ArrayList;

public class ButtonFunctionAdapter extends RecyclerView.Adapter<ButtonFunctionAdapter.ViewHolder>{
    ArrayList<ButtonFunction> buttonFunctions;
    Context context;
    public ButtonFunctionAdapter(ArrayList<ButtonFunction> buttonFunctions, Context context) {
        this.buttonFunctions = buttonFunctions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_btn_function, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ButtonFunction buttonFunction = buttonFunctions.get(position);
        holder.btn.setText(buttonFunction.getName());
        holder.btn.setOnClickListener(v -> {
            Toast.makeText(context, buttonFunction.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, buttonFunction.getCls());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return buttonFunctions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.button);
        }
    }
}
