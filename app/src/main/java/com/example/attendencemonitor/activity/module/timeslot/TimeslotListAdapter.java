package com.example.attendencemonitor.activity.module.timeslot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.TimeslotModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class TimeslotListAdapter extends RecyclerView.Adapter<TimeslotListAdapter.ItemViewAdapter>{
    private final ArrayList<TimeslotModel> itemList;
    private final IRecyclerViewItemEventListener<TimeslotModel> listener;

    public static class ItemViewAdapter extends RecyclerView.ViewHolder{
        private final TextView tv_name;
        private final TextView tv_start;
        private final LinearLayout ll_timeslotContainer;

        public ItemViewAdapter(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_timeslot_name);
            tv_start = itemView.findViewById(R.id.tv_start_date);
            ll_timeslotContainer = itemView.findViewById(R.id.ll_timeslotContainer);
        }

    }
    public TimeslotListAdapter(ArrayList<TimeslotModel> itemList, IRecyclerViewItemEventListener<TimeslotModel> listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeslotListAdapter.ItemViewAdapter holder, int position) {
        TimeslotModel currentItem = itemList.get(position);
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        holder.tv_name.setText(currentItem.getName());
        holder.tv_start.setText(String.format("%s - %s", dateTimeFormatter.format(currentItem.getStartDate()), timeFormatter.format(currentItem.getEndDate())));
        holder.ll_timeslotContainer.setOnClickListener(view -> {
            listener.onClick(currentItem);
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

