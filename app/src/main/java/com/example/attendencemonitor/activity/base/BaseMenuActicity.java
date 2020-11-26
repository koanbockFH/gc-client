package com.example.attendencemonitor.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendencemonitor.Login;
import com.example.attendencemonitor.R;

public abstract class BaseMenuActicity extends AppCompatActivity
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
        inflater.inflate(R.menu.menu, menu);

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
            Intent intent = new Intent(this.getApplicationContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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
}
