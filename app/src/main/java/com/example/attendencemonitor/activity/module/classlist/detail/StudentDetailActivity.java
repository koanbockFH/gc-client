package com.example.attendencemonitor.activity.module.classlist.detail;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.google.android.material.tabs.TabLayout;

public class StudentDetailActivity extends BaseMenuActivity
{
    public static final String EXTRA_STUDENT_ID = "STUDENT_ID";
    public static final String EXTRA_MODULE_ID = "MODULE_ID";
    public static final String EXTRA_STUDENT_NAME = "STUDENT_NAME";
    public static final String EXTRA_STUDENT_ATTENDED = "STUDENT_ATTENDED";
    public static final String EXTRA_STUDENT_ABSENT = "STUDENT_ABSENT";
    public static final String EXTRA_WITHOUT_MODULES = "WITHOUT_MODULES";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent received = getIntent();
        int studentId = received.getIntExtra(EXTRA_STUDENT_ID, -1);
        int moduleId = received.getIntExtra(EXTRA_MODULE_ID, -1);
        int attended = received.getIntExtra(EXTRA_STUDENT_ATTENDED, -1);
        int absent = received.getIntExtra(EXTRA_STUDENT_ABSENT, -1);
        boolean withoutModules = received.getBooleanExtra(EXTRA_WITHOUT_MODULES, false);
        String title = received.getStringExtra(EXTRA_STUDENT_NAME);

        initializeMenu(title, true);
        super.onCreate(savedInstanceState);


        if (studentId == -1 || moduleId == -1 || attended == -1 || absent == -1)
        {
            finish();
        }

        setContentView(R.layout.activity_student_detail);
        StudentDetailPagerAdapter sectionsPagerAdapter = new StudentDetailPagerAdapter(getSupportFragmentManager(), moduleId, studentId, attended, absent, withoutModules);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}