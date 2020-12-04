package com.example.attendencemonitor.activity.module.timeslot.detail.student;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class TimeslotStudentFragment extends Fragment
{
    private List<UserModel> studentList;
    private TimeslotStudentListAdapter adapter;


    public TimeslotStudentFragment()
    {
        // Required empty public constructor
    }

    public static TimeslotStudentFragment newInstance(List<UserModel> studentList)
    {
        TimeslotStudentFragment fragment = new TimeslotStudentFragment();
        Bundle args = new Bundle();
        fragment.studentList = studentList;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_classlist, container, false);

        RecyclerView rv = view.findViewById(R.id.rv_user_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        adapter = new TimeslotStudentListAdapter(filter(null));

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

        ImageButton delSearch = view.findViewById(R.id.ib_delete_search_classlist);
        delSearch.setOnClickListener(v -> {
            searchBox.setText("");
        });
        return view;
    }
    private List<UserModel> filter(String searchValue)
    {
        List<UserModel> filteredList = new ArrayList<>();

        for(UserModel u: studentList)
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
}