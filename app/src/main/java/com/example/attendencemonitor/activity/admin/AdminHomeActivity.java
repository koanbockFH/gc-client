package com.example.attendencemonitor.activity.admin;

import android.os.Bundle;
import android.widget.EditText;

import androidx.viewpager.widget.ViewPager;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.google.android.material.tabs.TabLayout;

public class AdminHomeActivity extends BaseMenuActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Administration", false);
        super.onCreate(savedInstanceState);
        //initialize Tablayout for admin screen (modules, students, teacher)
        setContentView(R.layout.activity_admin_home);
        AdminPagerAdapter sectionsPagerAdapter = new AdminPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}