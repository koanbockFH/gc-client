package com.example.attendencemonitor.activity.module.detail;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.ModuleStatisticModel;
import com.google.android.material.tabs.TabLayout;

public class ModuleDetailActivity extends BaseMenuActivity
{
    public static final String EXTRA_MODULE_ID = "MODULE_ID";
    IModuleService moduleService = new ModuleService();
    IAttendanceService attendanceService = new AttendanceService();
    private ModuleModel module;
    private ModuleStatisticModel moduleStats;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Module", true);
        super.onCreate(savedInstanceState);

        Intent received = getIntent();
        int moduleId = received.getIntExtra(EXTRA_MODULE_ID, -1);

        if (moduleId == -1)
        {
            finish();
        }

        moduleService.getById(moduleId, new GetCallback());
    }

    private void initView()
    {
        setContentView(R.layout.activity_module_detail);
        ModulePagerAdapter sectionsPagerAdapter = new ModulePagerAdapter(getSupportFragmentManager(), module, moduleStats);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private class GetCallback implements ICallback<ModuleModel>
    {
        @Override
        public void onSuccess(ModuleModel value)
        {
            module = value;
            attendanceService.getModuleStats(value.getId(), new GetStatsCallback());
        }

        @Override
        public void onError(Throwable error){ }
    }

    private class GetStatsCallback implements ICallback<ModuleStatisticModel>
    {
        @Override
        public void onSuccess(ModuleStatisticModel value)
        {
            moduleStats = value;
            initView();
        }

        @Override
        public void onError(Throwable error){ }
    }
}
