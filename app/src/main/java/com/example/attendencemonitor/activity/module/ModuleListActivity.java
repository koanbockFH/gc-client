package com.example.attendencemonitor.activity.module;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.module.detail.ModuleDetailActivity;
import com.example.attendencemonitor.service.AppData;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.TimeslotModel;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.service.model.UserType;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleListActivity extends BaseMenuActivity
{
    IModuleService moduleService = new ModuleService();
    private List<ModuleModel> modules;
    private ModuleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Modules", AppData.getInstance().getUserType() == UserType.ADMIN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);

        FloatingActionButton fab = findViewById(R.id.fab_module_add);
        if(AppData.getInstance().getUserType() != UserType.ADMIN)
        {
            fab.setVisibility(View.GONE);
        }

        moduleService.getAll(new ModuleListCallback());

        EditText searchBox = findViewById(R.id.et_searchbox);
        searchBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void afterTextChanged(Editable editable)
            {
                String searchValue = editable.toString();
                adapter.setItems(filter(searchValue));
            }
        });
    }

    private void readValues(ModuleModel[] values)
    {
        ArrayList<ModuleModel> items = new ArrayList<>();
        Collections.addAll(items, values);
        modules = items;

        RecyclerView rv = findViewById(R.id.rv_module_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        adapter = new ModuleListAdapter(filter(null),  new ListItemListener());

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
        startActivity(new Intent(this, ModuleFormActivity.class));
    }

    private void openDetails(ModuleModel item){
        Intent timeslots = new Intent(this, ModuleDetailActivity.class);
        timeslots.putExtra(ModuleDetailActivity.EXTRA_MODULE_ID, item.getId());
        startActivity(timeslots);
    }

    private void deleteModule(ModuleModel item)
    {
        if(AppData.getInstance().getUserType() != UserType.ADMIN)
        {
            return;
        }

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    moduleService.delete(item, new DeleteCallback());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete the module?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void edit(ModuleModel item){
        Intent i = new Intent(this, ModuleFormActivity.class);
        i.putExtra(ModuleFormActivity.EXTRA_MODULE_ID, item.getId());
        startActivity(i);
    }

    private List<ModuleModel> filter(String searchValue)
    {
        List<ModuleModel> filteredList = new ArrayList<>();

        for(ModuleModel u: modules)
        {
            if(searchValue == null || searchValue.isEmpty())
            {
                filteredList.add(u);
            }
            else if(u.getName().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getCode().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getTeacher().getFullName().toLowerCase().contains(searchValue.toLowerCase()))
            {
                filteredList.add(u);
            }
        }

        return filteredList;
    }

    private class ListItemListener implements IRecyclerViewItemEventListener<ModuleModel>
    {
        @Override
        public void onClick(ModuleModel item)
        {
            openDetails(item);
        }

        @Override
        public void onLongPress(ModuleModel item)
        {
            deleteModule(item);
        }

        @Override
        public void onActionClick(ModuleModel item)
        {
            edit(item);
        }
    }

    private class DeleteCallback implements IActionCallback
    {
        @Override
        public void onSuccess()
        {
            Toast.makeText(ModuleListActivity.this, "Timeslot has been deleted!", Toast.LENGTH_SHORT).show();
            moduleService.getAll(new ModuleListCallback());
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(ModuleListActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
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