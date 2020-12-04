package com.example.attendencemonitor.activity.module.classlist.detail.module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.ModuleStatisticModelBase;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.List;
import java.util.Locale;

public class StudentModuleListAdapter extends RecyclerView.Adapter<StudentModuleListAdapter.ItemViewAdapter>{
    private List<ModuleStatisticModelBase> moduleList;
    private IRecyclerViewItemEventListener<ModuleStatisticModelBase> listener;

    public static class ItemViewAdapter extends RecyclerView.ViewHolder{
        private final TextView tv_module_code, tv_module_name, tv_teacher, tv_attendance;
        private final ImageButton ib_module_open;
        private final LinearLayout ll_moduleContainer;

        public ItemViewAdapter(@NonNull View itemView) {
            super(itemView);
            tv_module_code = itemView.findViewById(R.id.tv_module_code);
            tv_module_name = itemView.findViewById(R.id.tv_module_name);
            tv_teacher = itemView.findViewById(R.id.tv_teacher);
            tv_attendance = itemView.findViewById(R.id.tv_attendance);
            ib_module_open = itemView.findViewById(R.id.ib_module_open);
            ll_moduleContainer = itemView.findViewById(R.id.ll_moduleContainer);
        }

    }
    public StudentModuleListAdapter(List<ModuleStatisticModelBase> moduleList) {
        this.moduleList = moduleList;
        this.listener = null;
    }

    public StudentModuleListAdapter(List<ModuleStatisticModelBase> moduleList, IRecyclerViewItemEventListener<ModuleStatisticModelBase> listener) {
        this.moduleList = moduleList;
        this.listener = listener;
    }

    public void setItems(List<ModuleStatisticModelBase> items)
    {
        moduleList = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StudentModuleListAdapter.ItemViewAdapter holder, int position) {
        ModuleStatisticModelBase currentItem = moduleList.get(position);

        holder.tv_module_code.setText(currentItem.getCode());
        holder.tv_module_name.setText(currentItem.getName());
        holder.tv_teacher.setText(currentItem.getTeacher().getFullName());
        holder.tv_attendance.setText(String.format(Locale.getDefault(), "%d/%d", currentItem.getAttended(), currentItem.getTotalTimeslots()));

        if(listener != null)
        {
            holder.ib_module_open.setVisibility(View.VISIBLE);
            holder.ib_module_open.setOnClickListener(v -> {listener.onClick(currentItem);});
            holder.ll_moduleContainer.setOnClickListener(v -> {listener.onClick(currentItem);});
        }
    }

    @NonNull
    @Override
    public StudentModuleListAdapter.ItemViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_module_list_item, parent, false);
        return new StudentModuleListAdapter.ItemViewAdapter(v);
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }
}

