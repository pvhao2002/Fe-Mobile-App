package com.app.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.app.R;
import com.app.app.callback.UserCallback;
import com.app.app.model.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    ArrayList<User> userArrayList;
    UserCallback userCallback;

    public UserAdapter(ArrayList<User> userArrayList, UserCallback userCallback) {
        this.userArrayList = userArrayList;
        this.userCallback = userCallback;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_row, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.textViewUsername.setText(user.getUsername());
        holder.textViewFullName.setText(user.getFullName());

        holder.textViewBtnBlock.setOnClickListener(v -> userCallback.onBlockClick(user.getUserId()));
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewFullName, textViewBtnBlock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername1);
            textViewFullName = itemView.findViewById(R.id.textViewFullname1);
            textViewBtnBlock = itemView.findViewById(R.id.textViewBtnBlock);
        }
    }
}
