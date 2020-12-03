package com.example.attendencemonitor.activity.module.classlist.detail.timeslot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendencemonitor.R;

public class StudentTimeslotFragment extends Fragment
{
    public StudentTimeslotFragment()
    {
        // Required empty public constructor
    }

    public static StudentTimeslotFragment newInstance()
    {
        StudentTimeslotFragment fragment = new StudentTimeslotFragment();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_student_timeslot, container, false);
    }
}