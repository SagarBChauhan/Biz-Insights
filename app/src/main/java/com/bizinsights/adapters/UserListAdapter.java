package com.bizinsights.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bizinsights.R;
import com.bizinsights.models.User;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private final ArrayList<User.Rows> userListData;

    public UserListAdapter(ArrayList<User.Rows> userListData) {
        this.userListData = userListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View list_item = inflater.inflate(R.layout.item_user_list, parent, false);
        return new ViewHolder(list_item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User.Rows user = userListData.get(position);
        holder.text_user_name.setText(user.first_name + " " + user.last_name);
        holder.text_user_email.setText(user.email_id);
        holder.text_user_mobile_no.setText(user.mobile_no);
        holder.text_user_address.setText(user.address);
    }


    @Override
    public int getItemCount() {
        return userListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView text_user_name, text_user_email, text_user_mobile_no, text_user_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.text_user_name = itemView.findViewById(R.id.text_user_name);
            this.text_user_email = itemView.findViewById(R.id.text_user_email);
            this.text_user_mobile_no = itemView.findViewById(R.id.text_user_mobile_no);
            this.text_user_address = itemView.findViewById(R.id.text_user_address);
        }
    }
}
