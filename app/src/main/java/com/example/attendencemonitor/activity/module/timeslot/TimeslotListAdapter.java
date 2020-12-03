package com.example.attendencemonitor.activity.module.timeslot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.TimeslotStatisticModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TimeslotListAdapter extends RecyclerView.Adapter<TimeslotListAdapter.ItemViewAdapter>{
    private List<TimeslotStatisticModel> itemList;
    private final IRecyclerViewItemEventListener<TimeslotStatisticModel> listener;

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
    public TimeslotListAdapter(List<TimeslotStatisticModel> itemList, IRecyclerViewItemEventListener<TimeslotStatisticModel> listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    public void setItems(List<TimeslotStatisticModel> items)
    {
        itemList = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull TimeslotListAdapter.ItemViewAdapter holder, int position) {
        TimeslotStatisticModel currentItem = itemList.get(position);
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        holder.tv_attendance.setText(String.format("%s/%s", currentItem.getAttended(), currentItem.getTotalStudents()));
        holder.tv_name.setText(currentItem.getName());
        holder.tv_start.setText(String.format("%s - %s", dateTimeFormatter.format(currentItem.getStartDate()), timeFormatter.format(currentItem.getEndDate())));

        holder.ib_stats.setOnClickListener(v -> {listener.onSecondaryActionClick(currentItem);});
        holder.ib_edit.setOnClickListener(v -> {listener.onPrimaryClick(currentItem);});
        holder.ib_scan.setOnClickListener(v -> {listener.onClick(currentItem);});
        holder.tv_name.setOnClickListener(view -> {
            listener.onPrimaryClick(currentItem);
        });
        holder.ll_timeslotContainer.setOnLongClickListener(view -> {
            listener.onLongPress(currentItem);
            return true;
        });

    }

    @NonNull
    @Override
    public TimeslotListAdapter.ItemViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeslot_list_item, parent, false);
        return new TimeslotListAdapter.ItemViewAdapter(v);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

