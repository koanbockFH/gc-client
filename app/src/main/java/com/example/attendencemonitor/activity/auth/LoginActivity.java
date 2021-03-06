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
    private final IUserService userService = new UserService();
    private EditText eUsername, ePassword;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeMenu("Login", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        //save input into variables for later use
        eUsername = findViewById(R.id.login_username);
        ePassword = findViewById(R.id.login_password);

        //submit login request on "Enter" in password box
        ePassword.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            {
                onSubmit(view);
                return true;
            }
            return false;
        });

        //init validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.login_username, RegexTemplate.NOT_EMPTY, R.string.must_not_be_empty);
        awesomeValidation.addValidation(this,R.id.login_password,RegexTemplate.NOT_EMPTY,R.string.must_not_be_empty);
    }

    public void onSubmit(View v) {
        //validate input
        if(!awesomeValidation.validate())
        {
            return;
        }

        //if ok, setup request access token
        LoginFormDto dto = new LoginFormDto();
        dto.setCodeOrMail(eUsername.getText().toString());
        dto.setPassword(ePassword.getText().toString());

        //send request, and register callback to handle response
        userService.login(this, dto, new LoginCallback());
    }

    private class LoginCallback implements IActionCallback
    {
        @Override
        public void onSuccess()
        {
            //start App-Session
            Intent intent = new Intent(LoginActivity.this, SessionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        @Override
        public void onError(Throwable error)
        {
            //display error e.g. wrong password
            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}