package com.example.attendencemonitor.activity.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.UserModel;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder>{

    private final ArrayList<UserModel> userList;
    private final boolean isSingleSelection;
    private final ArrayList<UserModel> currentSelection;

    public ArrayList<UserModel> getCurrentSelection()
    {
        return currentSelection;
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_username;
        private final TextView tv_code;
        private final TextView tv_usermail;
        private final CheckBox cb_selected;
        private final RadioButton rb_selected;
        private final LinearLayout userContainer;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username  = itemView.findViewById(R.id.tv_username);
            tv_code  = itemView.findViewById(R.id.tv_user_code);
            tv_usermail  = itemView.findViewById(R.id.tv_usermail);
            cb_selected = itemView.findViewById(R.id.cb_user);
            rb_selected = itemView.findViewById(R.id.rb_user);
            userContainer = itemView.findViewById(R.id.ll_userContainer);
        }

    }

    public UserListAdapter(ArrayList<UserModel> userList, boolean isSingleSelection, ArrayList<UserModel> currentSelection) {
        this.userList = userList;
        this.isSingleSelection = isSingleSelection;
        this.currentSelection = currentSelection;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserListViewHolder holder, int position) {
        UserModel currentItem = userList.get(position);
        holder.tv_username.setText(currentItem.getFullName());
        holder.tv_usermail.setText(currentItem.getMail());
        holder.tv_code.setText(currentItem.getCode());

        if(isSingleSelection){
            holder.cb_selected.setVisibility(View.GONE);
            holder.rb_selected.setVisibility(View.VISIBLE);
            holder.rb_selected.setChecked(currentSelection.stream().anyMatch(o -> o.getId() == currentItem.getId()));
        }
        else{
            holder.rb_selected.setVisibility(View.GONE);
            holder.cb_selected.setVisibility(View.VISIBLE);
            holder.cb_selected.setChecked(currentSelection.stream().anyMatch(o -> o.getId() == currentItem.getId()));
        }

        holder.rb_selected.setOnCheckedChangeListener((compoundButton, b) -> {
            currentSelection.clear();
            currentSelection.add(currentItem);
            try{
                notifyItemRangeChanged(0, userList.size());
            }
            catch(Exception ignored) { }
        });

        holder.cb_selected.setOnCheckedChangeListener((compoundButton, newValue) -> {
            if(newValue)
            {
                currentSelection.add(currentItem);
            }
            else{
                currentSelection.remove(currentItem);
            }
        });

        holder.userContainer.setOnClickListener(view -> {
            if(isSingleSelection)
            {
                holder.rb_selected.setChecked(true);
            }
            else{
                holder.cb_selected.setChecked(!holder.cb_selected.isChecked());
            }
        });
    }

    @NonNull
    @Override
    public UserListAdapter.UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
       return new  UserListAdapter.UserListViewHolder(v);
    }



    @Override
    public int getItemCount() {
        return userList.size();
    }
}