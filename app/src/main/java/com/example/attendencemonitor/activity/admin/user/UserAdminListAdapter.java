package com.example.attendencemonitor.activity.admin.user;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.TimeslotStatisticModel;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.List;

/***
 * Simple implementation of the Recycler Adapter for the User Admin lists (Student and Teacher)
 */
public class UserAdminListAdapter extends RecyclerView.Adapter<UserAdminListAdapter.StudentListViewHolder>{
    private final SortedList<UserModel> studentList;
    private final IRecyclerViewItemEventListener<UserModel> listener;

    public static class StudentListViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_username, tv_code, tv_usermail, tv_attendance;
        private final LinearLayout userContainer;
        private final ImageButton ib_student_open;

        public StudentListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username  = itemView.findViewById(R.id.tv_username);
            tv_code  = itemView.findViewById(R.id.tv_user_code);
            tv_usermail  = itemView.findViewById(R.id.tv_usermail);
            tv_attendance = itemView.findViewById(R.id.tv_attendance);
            userContainer = itemView.findViewById(R.id.ll_userContainer);
            ib_student_open = itemView.findViewById(R.id.ib_student_open);
        }
    }

    public UserAdminListAdapter(List<UserModel> StudentList, IRecyclerViewItemEventListener<UserModel> listener) {
        //Setting up the sorting of the user List
        this.studentList = new SortedList<>(UserModel.class, new SortedList.Callback<UserModel>() {
            @Override
            public int compare(UserModel o1, UserModel o2) {
                return o1.getFullName().compareTo(o2.getFullName());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(UserModel oldItem, UserModel newItem) {
                return oldItem.getFullName().equals(newItem.getFullName());
            }

            @Override
            public boolean areItemsTheSame(UserModel item1, UserModel item2) {
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

    public void setItems(List<UserModel> items)
    {
        studentList.clear();
        studentList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdminListAdapter.StudentListViewHolder holder, int position) {
        //setting data for each row
        UserModel currentItem = studentList.get(position);
        holder.tv_username.setText(currentItem.getFullName());
        holder.tv_usermail.setText(currentItem.getMail());
        holder.tv_code.setText(currentItem.getCode());
        //forwarding events to the event listener
        holder.userContainer.setOnClickListener(v -> listener.onClick(currentItem));
        holder.ib_student_open.setOnClickListener(v -> listener.onClick(currentItem));

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
