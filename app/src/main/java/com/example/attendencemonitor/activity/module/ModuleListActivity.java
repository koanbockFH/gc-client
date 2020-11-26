package com.example.attendencemonitor.activity.module;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.dto.UserSearchDto;
import com.example.attendencemonitor.service.model.ModuleModel;

import java.util.ArrayList;
import java.util.Collections;

public class ModuleListActivity extends BaseMenuActivity
{
    IModuleService moduleService = new ModuleService();
    private final UserSearchDto dto = new UserSearchDto();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Modules", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);

        moduleService.getAll(new ModuleListCallback());
    }

    private void readValues(ModuleModel[] values)
    {
        RecyclerView rv = findViewById(R.id.rv_module_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        ArrayList<ModuleModel> items = new ArrayList<>();
        Collections.addAll(items, values);
        ModuleListAdapter adapter = new ModuleListAdapter(items);

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        moduleService.getAll(new ModuleListCallback());
    }

    public void onAdd(View view)
    {
        startActivity(new Intent(this, ModuleAdd.class));
    }

    private class ModuleListCallback implements ICallback<ModuleModel[]>
    {

        @Override
        public void onSuccess(ModuleModel[] value)
        {
            readValues(value);
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(ModuleListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}