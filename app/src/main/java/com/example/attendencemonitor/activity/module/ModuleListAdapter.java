package com.example.attendencemonitor.activity.module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.ModuleModel;

import java.util.ArrayList;

public class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ItemViewAdapter>{
    private ArrayList<ModuleModel> moduleList;

    public static class ItemViewAdapter extends RecyclerView.ViewHolder{
        private TextView tv_module_code;
        private TextView tv_module_name;
        private TextView tv_teacher;

        public ItemViewAdapter(@NonNull View itemView) {
            super(itemView);
            tv_module_code = itemView.findViewById(R.id.tv_module_code);
            tv_module_name = itemView.findViewById(R.id.tv_module_name);
            tv_teacher = itemView.findViewById(R.id.tv_teacher);
        }

    }
    public ModuleListAdapter(ArrayList<ModuleModel> moduleList) {
        this.moduleList = moduleList;
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleListAdapter.ItemViewAdapter holder, int position) {
        ModuleModel currentItem = moduleList.get(position);
        holder.tv_module_code.setText(currentItem.getCode());
        holder.tv_module_name.setText(currentItem.getName());
        holder.tv_teacher.setText(currentItem.getTeacher().getFullName());
    }

    @NonNull
    @Override
    public ModuleListAdapter.ItemViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_list_item, parent, false);
        return new ModuleListAdapter.ItemViewAdapter(v);
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }
}

