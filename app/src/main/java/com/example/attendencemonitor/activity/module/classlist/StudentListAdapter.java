package com.example.attendencemonitor.activity.module.classlist;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.StudentModuleStatisticModel;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentListViewHolder>{
    private SortedList<StudentModuleStatisticModel> studentList;
    private final IRecyclerViewItemEventListener<StudentModuleStatisticModel> listener;

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

    public StudentListAdapter(List<StudentModuleStatisticModel> StudentList, IRecyclerViewItemEventListener<StudentModuleStatisticModel> listener) {
        this.studentList = new SortedList<StudentModuleStatisticModel>(StudentModuleStatisticModel.class, new SortedList.Callback<StudentModuleStatisticModel>() {
            @Override
            public int compare(StudentModuleStatisticModel o1, StudentModuleStatisticModel o2) {
                return o1.getFullName().compareTo(o2.getFullName());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(StudentModuleStatisticModel oldItem, StudentModuleStatisticModel newItem) {
                return oldItem.getFullName().equals(newItem.getFullName());
            }

            @Override
            public boolean areItemsTheSame(StudentModuleStatisticModel item1, StudentModuleStatisticModel item2) {
                return item1.getFullName().equals(item2.getFullName());
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
        studentList.addAll(StudentList);
        notifyDataSetChanged();
        this.listener = listener;
    }

    public void setItems(List<StudentModuleStatisticModel> items)
    {
        studentList.clear();
        studentList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.StudentListViewHolder holder, int position) {
        StudentModuleStatisticModel currentItem = studentList.get(position);
        holder.tv_username.setText(currentItem.getFullName());
        holder.tv_usermail.setText(currentItem.getMail());
        holder.tv_code.setText(currentItem.getCode());
        holder.tv_attendance.setText(String.format("%s/%s", currentItem.getAttended(), currentItem.getTotalTimeslots()));
        holder.userContainer.setOnClickListener(v -> {listener.onClick(currentItem);});
    }

    @NonNull
    @Override
    public StudentListAdapter.StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new StudentListAdapter.StudentListViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
