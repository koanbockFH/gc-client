package com.example.attendencemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.UserService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.dto.LoginFormDto;

public class MainActivity extends AppCompatActivity {

    IUserService userService = new UserService();
    IModuleService moduleService = new ModuleService();

    private EditText eUsername;
    private EditText ePassword;
    private Button eSubmit;

    String userName = "";
    String userPassword = "";

    //validation
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eUsername = findViewById(R.id.username);
        ePassword = findViewById(R.id.password);
        eSubmit = findViewById(R.id.btn_login);

        //validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.username, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.password,".{6,}",R.string.invalid_password);


        eSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = eUsername.getText().toString();
                userPassword = ePassword.getText().toString();

                LoginFormDto dto = new LoginFormDto();
                dto.setCodeOrMail(userName);
                dto.setPassword(userPassword);



                //validation
                if(awesomeValidation.validate())
                {
                    userService.login(dto, new LoginCallback());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please check your input", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class LoginCallback implements IActionCallback
    {

        @Override
        public void onSuccess()
        {
            Log.i("login", "successful");
            startActivity(new Intent(MainActivity.this, HomeAdmin.class));
        }

        @Override
        public void onError(Throwable error)
        {

            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

        }
    }
}