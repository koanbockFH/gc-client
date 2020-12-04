package com.example.attendencemonitor.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.SessionActivity;
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
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeMenu("Login", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        eUsername = findViewById(R.id.login_username);
        ePassword = findViewById(R.id.login_password);

        ePassword.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            {
                onSubmit(view);
                return true;
            }
            return false;
        });

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.login_username, RegexTemplate.NOT_EMPTY, R.string.must_not_be_empty);
        awesomeValidation.addValidation(this,R.id.login_password,RegexTemplate.NOT_EMPTY,R.string.must_not_be_empty);
    }

    public void onSubmit(View v) {
        if(!awesomeValidation.validate())
        {
            return;
        }

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
            startActivity(new Intent(LoginActivity.this, SessionActivity.class));
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}