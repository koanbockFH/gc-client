package com.example.attendencemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.UserService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.dto.RegisterFormDto;
import com.example.attendencemonitor.service.model.UserType;

public class AddModule extends AppCompatActivity {

    IUserService userService = new UserService();
    IModuleService moduleService = new ModuleService();

    private EditText eFirstname;
    private EditText eLastname;
    private EditText eEmail;
    private EditText eCode;
    private EditText ePassword;
    private EditText eConfpassword;
    private RadioButton eUsertype;
    private RadioGroup eUsertypeGroup;
    private Button eRegister;

    String userFirstname = "";
    String userLastname ="";
    String userEmail = "";
    String userCode = "";
    String userPassword = "";
    String userConfPassword = "";

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eUsertypeGroup = (RadioGroup) findViewById(R.id.reg_user_type);
        eFirstname = (EditText) findViewById(R.id.reg_firstname);
        eLastname = (EditText) findViewById(R.id.reg_lastname);
        eEmail = (EditText) findViewById(R.id.reg_mail);
        eCode = (EditText) findViewById(R.id.reg_code);
        ePassword = (EditText) findViewById(R.id.reg_password);
        eConfpassword = (EditText) findViewById(R.id.reg_conf_password);

        eRegister = (Button) findViewById(R.id.btn_submit_register);

        //validation

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.reg_firstname,RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.reg_lastname,RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.reg_mail, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.reg_code,".{3,}",R.string.invalid_code);
        awesomeValidation.addValidation(this,R.id.reg_password,".{6,}",R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.reg_conf_password,R.id.reg_password,R.string.invalid_conf_password);



        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFirstname = eFirstname.getText().toString();
                userLastname = eLastname.getText().toString();
                userEmail = eEmail.getText().toString();
                userCode = eCode.getText().toString();
                userPassword = ePassword.getText().toString();


                int selUserTypeId = eUsertypeGroup.getCheckedRadioButtonId();
                eUsertype = (RadioButton) findViewById(selUserTypeId);

                RegisterFormDto regDto = new RegisterFormDto();

                regDto.setFirstName(userFirstname);
                regDto.setLastName(userLastname);
                regDto.setMail(userEmail);
                regDto.setPassword(userPassword);
                regDto.setCode(userCode);
                switch(eUsertype.getText().toString()) {
                    case "Teacher":
                        regDto.setUserType(UserType.TEACHER);
                        break;
                    default:
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
        });

    }


    private class RegistrationCallback implements IActionCallback
    {

        @Override
        public void onSuccess()
        {
            Log.i("registration", "successful");
            Toast.makeText(getApplicationContext(), "Register successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddModule.this, HomeAdmin.class));
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(AddModule.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

}

