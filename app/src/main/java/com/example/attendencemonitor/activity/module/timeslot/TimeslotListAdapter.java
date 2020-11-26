package com.example.attendencemonitor.activity.module.timeslot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.TimeslotModel;

import java.util.ArrayList;

public class TimeslotListAdapter extends RecyclerView.Adapter<TimeslotListAdapter.ItemViewAdapter>{
    private final ArrayList<TimeslotModel> itemList;

    public static class ItemViewAdapter extends RecyclerView.ViewHolder{
        private final TextView tv_name;
        private final TextView tv_start;
        private final TextView tv_end;

        public ItemViewAdapter(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_module_code);
            tv_start = itemView.findViewById(R.id.tv_module_name);
            tv_end = itemView.findViewById(R.id.tv_teacher);
        }

    }
    public TimeslotListAdapter(ArrayList<TimeslotModel> itemList) {
        this.itemList = itemList;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeslotListAdapter.ItemViewAdapter holder, int position) {
        TimeslotModel currentItem = itemList.get(position);
        holder.tv_name.setText(currentItem.getName());
        holder.tv_start.setText(currentItem.getStartDate().toString());
        holder.tv_end.setText(currentItem.getEndDate().toString());
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

