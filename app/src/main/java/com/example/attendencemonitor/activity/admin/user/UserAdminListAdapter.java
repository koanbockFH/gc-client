package com.example.attendencemonitor.activity.admin.user;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.List;

public class UserAdminListAdapter extends RecyclerView.Adapter<UserAdminListAdapter.StudentListViewHolder>{
    private List<UserModel> studentList;
    private final IRecyclerViewItemEventListener<UserModel> listener;

    public static class StudentListViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_username;
        private final TextView tv_code;
        private final TextView tv_usermail;
        private final TextView tv_attendance;
        private final LinearLayout userContainer;

        public StudentListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username  = itemView.findViewById(R.id.tv_username);
            tv_code  = itemView.findViewById(R.id.tv_user_code);
            tv_usermail  = itemView.findViewById(R.id.tv_usermail);
            tv_attendance = itemView.findViewById(R.id.tv_attendance);
            userContainer = itemView.findViewById(R.id.ll_userContainer);
        }
    }

    public UserAdminListAdapter(List<UserModel> StudentList, IRecyclerViewItemEventListener<UserModel> listener) {
        this.studentList = StudentList;
        this.listener = listener;
    }

    public void setItems(List<UserModel> items)
    {
        studentList = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdminListAdapter.StudentListViewHolder holder, int position) {
        UserModel currentItem = studentList.get(position);
        holder.tv_username.setText(currentItem.getFullName());
        holder.tv_usermail.setText(currentItem.getMail());
        holder.tv_code.setText(currentItem.getCode());
        holder.userContainer.setOnClickListener(v -> {listener.onClick(currentItem);});

        holder.tv_attendance.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public UserAdminListAdapter.StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new UserAdminListAdapter.StudentListViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
