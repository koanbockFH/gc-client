package com.example.attendencemonitor.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.auth.RegisterActivity;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.module.ModuleListActivity;

public class AdminHomeActivity extends BaseMenuActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Administration", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void onRegister(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void onModules(View v) {
        startActivity(new Intent(this, ModuleListActivity.class));
    }
}