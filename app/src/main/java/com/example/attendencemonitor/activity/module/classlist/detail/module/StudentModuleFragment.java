package com.example.attendencemonitor.activity.module.classlist.detail.module;

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
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.model.ModuleStatisticModelBase;

import java.util.ArrayList;
import java.util.List;

public class StudentModuleFragment extends Fragment
{
    private int studentId;
    private List<ModuleStatisticModelBase> moduleList = new ArrayList<>();
    private StudentModuleListAdapter adapter;
    private final IAttendanceService attendanceService = new AttendanceService();
    private EditText searchBox;


    public StudentModuleFragment()
    {
        // Required empty public constructor
    }

    public static StudentModuleFragment newInstance(int studentId)
    {
        StudentModuleFragment fragment = new StudentModuleFragment();
        Bundle args = new Bundle();
        fragment.studentId = studentId;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_student_module, container, false);

        RecyclerView rv = view.findViewById(R.id.rv_module_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        adapter = new StudentModuleListAdapter(moduleList);

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);

        loadData();

        searchBox = view.findViewById(R.id.et_searchbox);
        searchBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void afterTextChanged(Editable editable)
            {
                displayData();
            }
        });

        ImageButton delSearch = view.findViewById(R.id.ib_delete_search_smodule);
        delSearch.setOnClickListener(v -> {
            searchBox.setText("");
        });

        return view;
    }

    private List<ModuleStatisticModelBase> filter(String searchValue)
    {
        List<ModuleStatisticModelBase> filteredList = new ArrayList<>();

        for(ModuleStatisticModelBase u: moduleList)
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

    private void loadData(){
        attendanceService.getStudentModuleStats(studentId, new GetCallback());
    }

    private void displayData(){
        adapter.setItems(filter(searchBox.getText().toString()));
    }

    private void makeToast(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    private class GetCallback implements ICallback<List<ModuleStatisticModelBase>>
    {
        @Override
        public void onSuccess(List<ModuleStatisticModelBase> value)
        {
            moduleList = value;
            displayData();
        }

        @Override
        public void onError(Throwable error)
        {
            makeToast(error.getMessage());
        }
    }
}