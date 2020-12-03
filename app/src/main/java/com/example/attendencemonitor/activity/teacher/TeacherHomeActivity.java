package com.example.attendencemonitor.activity.teacher;

import android.os.Bundle;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;

public class TeacherHomeActivity extends BaseMenuActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Teacher", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
    }
}