package com.example.attendencemonitor.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.admin.AdminHomeActivity;
import com.example.attendencemonitor.activity.auth.LoginActivity;
import com.example.attendencemonitor.activity.student.StudentHomeActivity;
import com.example.attendencemonitor.service.AppData;
import com.example.attendencemonitor.service.UserService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.model.UserType;

public abstract class BaseMenuActivity extends AppCompatActivity
{
    private String title;
    private boolean enableBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    protected void initializeMenu(String title, boolean enableBackButton)
    {
        this.title = title;
        this.enableBackButton = enableBackButton;
    }

    //enable my created menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        if(this.getClass() == LoginActivity.class)
        {
            inflater.inflate(R.menu.menu_unrestricted, menu);
        }
        else{
            inflater.inflate(R.menu.menu, menu);
        }

        if (getSupportActionBar() != null)
        {
            //disable title
            getSupportActionBar().setDisplayShowTitleEnabled(title.length()>0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(enableBackButton);
            getSupportActionBar().setDisplayShowHomeEnabled(enableBackButton);
            setTitle(title);

        }
        return true;
    }

    //this method passes the selected MenuItem
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.menuitem_logout){
            IUserService userService = new UserService();
            userService.logout(this, new LogoutCallback());
            return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
    private void onLogout()
    {
        Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private class LogoutCallback implements IActionCallback
    {
        @Override
        public void onSuccess()
        {
            onLogout();
        }

        @Override
        public void onError(Throwable error)
        {
            //TODO: Something went wrong - may be only debug problem
            if(error.getMessage().contains("Forbidden"))
            {
                AppData.getInstance().closeSession(BaseMenuActivity.this);
                onLogout();
            }
            else{
                Toast.makeText(BaseMenuActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
