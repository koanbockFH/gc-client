package com.example.attendencemonitor.activity.module.classlist.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.module.detail.ModuleDetailActivity;
import com.example.attendencemonitor.activity.module.detail.ModulePagerAdapter;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.StudentModuleStatisticModel;
import com.google.android.material.tabs.TabLayout;

public class StudentDetailActivity extends BaseMenuActivity
{
    public static final String EXTRA_STUDENT_ID = "STUDENT_ID";
    public static final String EXTRA_MODULE_ID = "MODULE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Module", true);
        super.onCreate(savedInstanceState);

        Intent received = getIntent();
        int studentId = received.getIntExtra(EXTRA_STUDENT_ID, -1);
        int moduleId = received.getIntExtra(EXTRA_MODULE_ID, -1);

        if (studentId == -1 || moduleId == -1)
        {
            finish();
        }

        initView();
    }

    private void initView()
    {
        setContentView(R.layout.activity_student_detail);
        StudentDetailPagerAdapter sectionsPagerAdapter = new StudentDetailPagerAdapter(getSupportFragmentManager(), 38, 84);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}