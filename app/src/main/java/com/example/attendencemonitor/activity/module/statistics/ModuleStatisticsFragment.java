package com.example.attendencemonitor.activity.module.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.attendencemonitor.R;

public class ModuleStatisticsFragment extends Fragment
{
    public ModuleStatisticsFragment()
    {
        // Required empty public constructor
    }

    public static ModuleStatisticsFragment newInstance()
    {
        ModuleStatisticsFragment fragment = new ModuleStatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_module_statistics, container, false);
    }
}