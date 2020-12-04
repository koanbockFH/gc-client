package com.example.attendencemonitor.activity.module;

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
import com.example.attendencemonitor.service.AppData;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.UserType;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.List;

public class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ItemViewAdapter>{
    private SortedList<ModuleModel> moduleList;
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

    public ModuleListAdapter(List<ModuleModel> ModuleList, IRecyclerViewItemEventListener<ModuleModel> listener) {
        this.moduleList = new SortedList<ModuleModel>(ModuleModel.class, new SortedList.Callback<ModuleModel>() {
            @Override
            public int compare(ModuleModel o1, ModuleModel o2) {
                return o1.getName().compareTo(o2.getName());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(ModuleModel oldItem, ModuleModel newItem) {
                return oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areItemsTheSame(ModuleModel item1, ModuleModel item2) {
                return item1.getName().equals(item2.getName());
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
        moduleList.addAll(ModuleList);
        notifyDataSetChanged();
        this.listener = listener;
    }

    public void setItems(List<ModuleModel> items)
    {
        moduleList.clear();
        moduleList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleListAdapter.ItemViewAdapter holder, int position) {
        ModuleModel currentItem = moduleList.get(position);
        if(AppData.getInstance().getUserType() != UserType.ADMIN)
        {
            holder.ib_edit.setVisibility(View.GONE);
        }
        else{
            holder.tv_module_name.setOnClickListener(v -> {listener.onPrimaryClick(currentItem);});
            holder.ib_edit.setOnClickListener(v -> {listener.onPrimaryClick(currentItem);});
        }

        holder.tv_module_code.setText(currentItem.getCode());
        holder.tv_module_name.setText(currentItem.getName());
        holder.tv_teacher.setText(currentItem.getTeacher().getFullName());

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

