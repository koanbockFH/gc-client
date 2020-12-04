package com.example.attendencemonitor.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.module.ModuleFormActivity;
import com.example.attendencemonitor.activity.module.ModuleListAdapter;
import com.example.attendencemonitor.activity.module.classlist.ClasslistFragment;
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
import java.util.Collections;
import java.util.List;

public class ModuleListFragment extends Fragment
{
    IModuleService moduleService = new ModuleService();
    private List<ModuleModel> modules;
    private ModuleListAdapter adapter;
    private View view;

    public ModuleListFragment()
    {
        // Required empty public constructor
    }

    public static ModuleListFragment newInstance()
    {
        ModuleListFragment fragment = new ModuleListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        moduleService.getAll(new ModuleListCallback());
        view = inflater.inflate(R.layout.activity_module_list, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_module_add);
        if(AppData.getInstance().getUserType() != UserType.ADMIN)
        {
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ModuleFormActivity.class));
        });

        EditText searchBox = view.findViewById(R.id.et_searchbox);
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

        ImageButton delSearch = view.findViewById(R.id.ib_delete_search_module);
        delSearch.setOnClickListener(v -> {
            searchBox.setText("");
        });
        return view;
    }

    private void readValues(ModuleModel[] values)
    {
        ArrayList<ModuleModel> items = new ArrayList<>();
        Collections.addAll(items, values);
        modules = items;

        RecyclerView rv = view.findViewById(R.id.rv_module_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        adapter = new ModuleListAdapter(filter(null),  new ListItemListener());

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        moduleService.getAll(new ModuleListCallback());
    }

    private void openDetails(ModuleModel item){
        Intent i = new Intent(getActivity(), ModuleDetailActivity.class);
        i.putExtra(ModuleDetailActivity.EXTRA_MODULE_ID, item.getId());
        i.putExtra(ModuleDetailActivity.EXTRA_MODULE_TITLE, item.getName());
        startActivity(i);
    }

    private void edit(ModuleModel item){
        Intent i = new Intent(getActivity(), ModuleFormActivity.class);
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
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}