package com.example.attendencemonitor.activity.auth;

import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.UserService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.dto.RegisterFormDto;
import com.example.attendencemonitor.service.model.UserType;

public class RegisterActivity extends BaseMenuActivity
{
    IUserService userService = new UserService();

    private EditText eFirstName;
    private EditText eLastName;
    private EditText eEmail;
    private EditText eCode;
    private EditText ePassword;
    private RadioGroup eUserTypeGroup;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeMenu("Register", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_register);

        eUserTypeGroup = findViewById(R.id.reg_user_type);
        eFirstName = findViewById(R.id.reg_firstname);
        eLastName = findViewById(R.id.reg_lastname);
        eEmail = findViewById(R.id.reg_mail);
        eCode = findViewById(R.id.reg_code);
        ePassword = findViewById(R.id.reg_password);
        EditText confPassword = findViewById(R.id.reg_conf_password);

        confPassword.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            {
                onSubmit(view);
                return true;
            }
            return false;
        });

        //validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.reg_firstname, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.reg_lastname,RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.reg_mail, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.reg_code,".{3,}",R.string.invalid_code);
        awesomeValidation.addValidation(this,R.id.reg_password,".{6,}",R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.reg_conf_password,R.id.reg_password,R.string.invalid_conf_password);
    }

    public void onSubmit(View v) {
        View radioButton = eUserTypeGroup.findViewById(eUserTypeGroup.getCheckedRadioButtonId());
        int userType = eUserTypeGroup.indexOfChild(radioButton);

        RegisterFormDto regDto = new RegisterFormDto();

        regDto.setFirstName(eFirstName.getText().toString());
        regDto.setLastName(eLastName.getText().toString());
        regDto.setMail(eEmail.getText().toString());
        regDto.setPassword(ePassword.getText().toString());
        regDto.setCode(eCode.getText().toString());
        switch(userType) {
            case 0:
                regDto.setUserType(UserType.TEACHER);
                break;
            default:
            case 1:
                regDto.setUserType(UserType.STUDENT);
                break;
        }

        //validation
        if(awesomeValidation.validate())
        {
            userService.register(regDto, new RegistrationCallback());
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please check your input", Toast.LENGTH_SHORT).show();
        }
    }

    private class RegistrationCallback implements IActionCallback
    {
        @Override
        public void onSuccess()
        {
            Toast.makeText(getApplicationContext(), "Register successful", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}