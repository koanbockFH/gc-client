package com.example.attendencemonitor.activity.module;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.module.detail.ModuleDetailActivity;
import com.example.attendencemonitor.service.AppData;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.UserType;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleListActivity extends BaseMenuActivity
{
    public static final String EXTRA_TEACHER_ID = "TEACHER_ID";
    public static final String EXTRA_TITLE = "TITLE";
    private final IModuleService moduleService = new ModuleService();
    private List<ModuleModel> modules;
    private ModuleListAdapter adapter;
    private int teacherId;
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //read additional information from calling intent
        Intent received = getIntent();
        teacherId = received.getIntExtra(EXTRA_TEACHER_ID, -1);
        String title = received.getStringExtra(EXTRA_TITLE);

        initializeMenu(title == null ? "Modules" : title, AppData.getInstance().getUserType() == UserType.ADMIN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);

        FloatingActionButton fab = findViewById(R.id.fab_module_add);
        if(AppData.getInstance().getUserType() != UserType.ADMIN)
        {
            fab.setVisibility(View.GONE);
        }

        //request from backend, and register the callback handling the response
        moduleService.getAll(new ModuleListCallback());

        //init search
        searchBox = findViewById(R.id.et_searchbox);
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

        ImageButton delSearch = findViewById(R.id.ib_delete_search_module);
        delSearch.setOnClickListener(v -> searchBox.setText(""));
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
        searchBox.setText("");
        startActivity(new Intent(this, ModuleFormActivity.class));
    }

    private void openDetails(ModuleModel item){
        searchBox.setText("");
        Intent i = new Intent(this, ModuleDetailActivity.class);
        i.putExtra(ModuleDetailActivity.EXTRA_MODULE_ID, item.getId());
        i.putExtra(ModuleDetailActivity.EXTRA_MODULE_TITLE, item.getName());
        startActivity(i);
    }

    private void edit(ModuleModel item){
        searchBox.setText("");
        Intent i = new Intent(this, ModuleFormActivity.class);
        i.putExtra(ModuleFormActivity.EXTRA_MODULE_ID, item.getId());
        startActivity(i);
    }

    /***
     * Filter the displayed user list based on a search value (name, code, mail)
     * @param searchValue searchvalue
     * @return list of modules to be displayed
     */
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

    /***
     * ListItem listener for recyclerview and handling of events from each row
     */
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
            // not used
        }

        @Override
        public void onPrimaryClick(ModuleModel item)
        {
            edit(item);
        }

        @Override
        public void onSecondaryActionClick(ModuleModel item)
        {
            //not used
        }
    }

    /***
     * Callback for response of backend on Get All modules
     */
    private class ModuleListCallback implements ICallback<ModuleModel[]>
    {
        @Override
        public void onSuccess(ModuleModel[] value)
        {
            if(teacherId != -1)
            {
                List<ModuleModel> list = Arrays.asList(value);
                value = list.stream().filter(e -> e.getTeacher().getId() == teacherId).toArray(ModuleModel[] ::new);
            }
            readValues(value);
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(ModuleListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}