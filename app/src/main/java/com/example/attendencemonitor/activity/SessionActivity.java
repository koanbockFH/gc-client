package com.example.attendencemonitor.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendencemonitor.activity.admin.AdminHomeActivity;
import com.example.attendencemonitor.activity.auth.LoginActivity;
import com.example.attendencemonitor.activity.module.ModuleListActivity;
import com.example.attendencemonitor.activity.student.StudentHomeActivity;
import com.example.attendencemonitor.service.AppData;
import com.example.attendencemonitor.service.model.UserType;

public class SessionActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSession();
    }

    private void getSession()
    {
        boolean isLoggedIn = AppData.getInstance().getSession(this);
        if(!isLoggedIn)
        {
            Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if(isLoggedIn)
        {
            UserType type = AppData.getInstance().getUserType();
            Class<?> home = null;
            switch(type)
            {
                case ADMIN:
                    home = AdminHomeActivity.class;
                    break;
                case TEACHER:
                    home = ModuleListActivity.class;
                    break;
                case STUDENT:
                    home = StudentHomeActivity.class;
                    break;
            }
            if(home == null)
            {
                return;
            }
            Intent intent = new Intent(this.getApplicationContext(), home);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

}