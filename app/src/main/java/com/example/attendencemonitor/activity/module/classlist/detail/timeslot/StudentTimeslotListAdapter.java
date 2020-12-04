package com.example.attendencemonitor.activity.module.classlist.detail.timeslot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.TimeslotModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class StudentTimeslotListAdapter extends RecyclerView.Adapter<StudentTimeslotListAdapter.ItemViewAdapter>{
    private List<TimeslotModel> itemList;

    public static class ItemViewAdapter extends RecyclerView.ViewHolder{
        private final TextView tv_name;
        private final TextView tv_start;
        private final LinearLayout ll_timeslotContainer;
        private final ImageButton ib_edit, ib_stats, ib_scan;
        private final TextView tv_attendance;

        public ItemViewAdapter(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_timeslot_name);
            tv_start = itemView.findViewById(R.id.tv_start_date);
            ll_timeslotContainer = itemView.findViewById(R.id.ll_timeslotContainer);
            ib_edit = itemView.findViewById(R.id.ib_edit);
            ib_scan = itemView.findViewById(R.id.ib_scan);
            ib_stats = itemView.findViewById(R.id.ib_stats);
            tv_attendance = itemView.findViewById(R.id.tv_attendance);
        }

    }
    public StudentTimeslotListAdapter(List<TimeslotModel> itemList) {
        this.itemList = itemList;
    }

    public void setItems(List<TimeslotModel> items)
    {
        itemList = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StudentTimeslotListAdapter.ItemViewAdapter holder, int position) {
        TimeslotModel currentItem = itemList.get(position);
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        holder.tv_name.setText(currentItem.getName());
        holder.tv_start.setText(String.format("%s - %s", dateTimeFormatter.format(currentItem.getStartDate()), timeFormatter.format(currentItem.getEndDate())));

        holder.tv_attendance.setVisibility(View.GONE);
        holder.ib_edit.setVisibility(View.GONE);
        holder.ib_scan.setVisibility(View.GONE);
        holder.ib_stats.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public StudentTimeslotListAdapter.ItemViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeslot_list_item, parent, false);
        return new StudentTimeslotListAdapter.ItemViewAdapter(v);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

