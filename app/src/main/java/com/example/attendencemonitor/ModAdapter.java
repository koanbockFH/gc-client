package com.example.attendencemonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.service.model.ModuleModel;

import java.util.ArrayList;

public class ModAdapter extends RecyclerView.Adapter<ModAdapter.MViewHolder>{

    private ArrayList<ModuleModel> mModuleList;

    public static class MViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.module);
        }
    }

    public ModAdapter(ArrayList<ModuleModel> moduleList) {
        mModuleList = moduleList;
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        ModuleModel currentItem = mModuleList.get(position);

        holder.mTextView.setText(currentItem.getName());
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item, parent, false);
        MViewHolder evh = new MViewHolder(v);
        return evh;
    }

    @Override
    public int getItemCount() {
        return mModuleList.size();
    }
}
