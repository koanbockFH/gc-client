package com.example.attendencemonitor.activity.module.classlist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.module.detail.ModuleDetailActivity;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.ModuleStatisticModel;
import com.example.attendencemonitor.service.model.StudentModuleStatisticModel;
import com.example.attendencemonitor.service.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ClasslistFragment extends Fragment
{
    private ModuleModel module;
    private StudentListAdapter adapter;
    IAttendanceService attendanceService = new AttendanceService();
    private ModuleStatisticModel moduleStats;

    public ClasslistFragment()
    {
        // Required empty public constructor
    }

    public static ClasslistFragment newInstance(ModuleModel module)
    {
        ClasslistFragment fragment = new ClasslistFragment();
        fragment.module = module;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        attendanceService.getModuleStats(module.getId(), new GetStatsCallback());
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        attendanceService.getModuleStats(module.getId(), new GetStatsCallback());
        View view = inflater.inflate(R.layout.fragment_classlist, container, false);

        RecyclerView rv = view.findViewById(R.id.rv_user_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        adapter = new StudentListAdapter(filter(null));

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
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

        return view;
    }

    private List<StudentModuleStatisticModel> filter(String searchValue)
    {
        List<StudentModuleStatisticModel> filteredList = new ArrayList<>();
        if(moduleStats == null)
        {
            return filteredList;
        }

        for(StudentModuleStatisticModel u: moduleStats.getStudents())
        {
            if(searchValue == null || searchValue.isEmpty())
            {
                filteredList.add(u);
            }
            else if(u.getMail().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getCode().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getFullName().toLowerCase().contains(searchValue.toLowerCase()))
            {
                filteredList.add(u);
            }
        }

        return filteredList;
    }

    private class GetStatsCallback implements ICallback<ModuleStatisticModel>
    {
        @Override
        public void onSuccess(ModuleStatisticModel value)
        {
            moduleStats = value;
            adapter.setItems(filter(null));
        }

        @Override
        public void onError(Throwable error){ }
    }
}