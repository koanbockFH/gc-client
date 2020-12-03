package com.example.attendencemonitor.activity.module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.AppData;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.UserType;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.List;

public class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ItemViewAdapter>{
    private List<ModuleModel> moduleList;
    private final IRecyclerViewItemEventListener<ModuleModel> listener;

    public static class ItemViewAdapter extends RecyclerView.ViewHolder{
        private final TextView tv_module_code;
        private final TextView tv_module_name;
        private final TextView tv_teacher;
        private final LinearLayout ll_moduleContainer;
        private final ImageButton ib_edit;
        private final ImageButton ib_open;

        public ItemViewAdapter(@NonNull View itemView) {
            super(itemView);
            tv_module_code = itemView.findViewById(R.id.tv_module_code);
            tv_module_name = itemView.findViewById(R.id.tv_module_name);
            tv_teacher = itemView.findViewById(R.id.tv_teacher);
            ll_moduleContainer = itemView.findViewById(R.id.ll_moduleContainer);
            ib_edit = itemView.findViewById(R.id.ib_module_edit);
            ib_open = itemView.findViewById(R.id.ib_module_open);
        }

    }
    public ModuleListAdapter(List<ModuleModel> moduleList, IRecyclerViewItemEventListener<ModuleModel> listener) {
        this.moduleList = moduleList;
        this.listener = listener;
    }

    public void setItems(List<ModuleModel> items)
    {
        moduleList = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleListAdapter.ItemViewAdapter holder, int position) {
        if(AppData.getInstance().getUserType() != UserType.ADMIN)
        {
            holder.ib_edit.setVisibility(View.GONE);
        }

        ModuleModel currentItem = moduleList.get(position);
        holder.tv_module_code.setText(currentItem.getCode());
        holder.tv_module_name.setText(currentItem.getName());
        holder.tv_teacher.setText(currentItem.getTeacher().getFullName());

        holder.ib_edit.setOnClickListener(v -> {listener.onActionClick(currentItem);});
        holder.ib_open.setOnClickListener(v -> {listener.onClick(currentItem);});
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

