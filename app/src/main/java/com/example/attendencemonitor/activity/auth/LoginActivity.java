package com.example.attendencemonitor.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.admin.AdminHomeActivity;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.UserService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.dto.LoginFormDto;

public class LoginActivity extends BaseMenuActivity
{
    IUserService userService = new UserService();

    private EditText eUsername;
    private EditText ePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeMenu("Login", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        eUsername = findViewById(R.id.username);
        ePassword = findViewById(R.id.password);
    }

    public void onSubmit(View v) {
        LoginFormDto dto = new LoginFormDto();
        dto.setCodeOrMail(eUsername.getText().toString());
        dto.setPassword(ePassword.getText().toString());

        userService.login(this, dto, new LoginCallback());
    }

    private class LoginCallback implements IActionCallback
    {
        @Override
        public void onSuccess()
        {
            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}