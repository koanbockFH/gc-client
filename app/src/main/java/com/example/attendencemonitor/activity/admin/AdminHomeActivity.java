package com.example.attendencemonitor.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.auth.RegisterActivity;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.module.ModuleListActivity;
import com.example.attendencemonitor.activity.module.detail.ModulePagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class AdminHomeActivity extends BaseMenuActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Administration", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        AdminPagerAdapter sectionsPagerAdapter = new AdminPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}