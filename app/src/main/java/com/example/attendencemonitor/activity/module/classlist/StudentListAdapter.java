package com.example.attendencemonitor.activity.module.classlist;


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

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentListViewHolder>{

    private List<UserModel> studentList;

    public static class StudentListViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_username;
        private final TextView tv_code;
        private final TextView tv_usermail;
        private final CheckBox cb_selected;
        private final RadioButton rb_selected;

        public StudentListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username  = itemView.findViewById(R.id.tv_username);
            tv_code  = itemView.findViewById(R.id.tv_user_code);
            tv_usermail  = itemView.findViewById(R.id.tv_usermail);
            cb_selected = itemView.findViewById(R.id.cb_user);
            rb_selected = itemView.findViewById(R.id.rb_user);
        }
    }

    public StudentListAdapter(List<UserModel> StudentList) {
        this.studentList = StudentList;
    }

    public void setItems(List<UserModel> items)
    {
        studentList = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.StudentListViewHolder holder, int position) {
        UserModel currentItem = studentList.get(position);
        holder.tv_username.setText(currentItem.getFullName());
        holder.tv_usermail.setText(currentItem.getMail());
        holder.tv_code.setText(currentItem.getCode());
        holder.cb_selected.setVisibility(View.GONE);
        holder.rb_selected.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public StudentListAdapter.StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new StudentListAdapter.StudentListViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
