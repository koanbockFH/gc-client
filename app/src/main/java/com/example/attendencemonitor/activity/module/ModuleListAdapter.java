package com.example.attendencemonitor.activity.module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.ArrayList;

public class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ItemViewAdapter>{
    private final ArrayList<ModuleModel> moduleList;
    private final IRecyclerViewItemEventListener<ModuleModel> listener;

    public static class ItemViewAdapter extends RecyclerView.ViewHolder{
        private final TextView tv_module_code;
        private final TextView tv_module_name;
        private final TextView tv_teacher;
        private final LinearLayout ll_moduleContainer;

        public ItemViewAdapter(@NonNull View itemView) {
            super(itemView);
            tv_module_code = itemView.findViewById(R.id.tv_module_code);
            tv_module_name = itemView.findViewById(R.id.tv_module_name);
            tv_teacher = itemView.findViewById(R.id.tv_teacher);
            ll_moduleContainer = itemView.findViewById(R.id.ll_moduleContainer);
        }

    }
    public ModuleListAdapter(ArrayList<ModuleModel> moduleList, IRecyclerViewItemEventListener<ModuleModel> listener) {
        this.moduleList = moduleList;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleListAdapter.ItemViewAdapter holder, int position) {
        ModuleModel currentItem = moduleList.get(position);
        holder.tv_module_code.setText(currentItem.getCode());
        holder.tv_module_name.setText(currentItem.getName());
        holder.tv_teacher.setText(currentItem.getTeacher().getFullName());
        holder.ll_moduleContainer.setOnClickListener(view -> {
            listener.onClick(currentItem);
        });
        holder.ll_moduleContainer.setOnLongClickListener(view -> {
            listener.onLongPress(currentItem);
            return true;
        });
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

