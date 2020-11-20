package com.example.attendencemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.UserService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.UserModel;

public class AddModule extends AppCompatActivity {

    IUserService userService = new UserService();
    IModuleService moduleService = new ModuleService();


    private EditText eModulename;
    private EditText eCode;
    private EditText eTeacher;
    private EditText eClass;
    private Button eAddModule;

    String modulename = "";
    String moduleCode = "";
    String mteacher = "";
    String mClass = "";

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

        //Buttons/Textfields
        eModulename = (EditText) findViewById(R.id.add_module_name);
        eCode = (EditText) findViewById(R.id.add_module_code);
        eTeacher = (EditText) findViewById(R.id.add_module_teacher);
        eClass = (EditText) findViewById(R.id.add_module_class);
        eAddModule = (Button) findViewById(R.id.btn_add_module);

        //validation
//???
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.add_module_name,RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.add_module_code,".{3,}",R.string.invalid_code);
        //TODO teacher awesomeValidation.addValidation(this,R.id.add_module_teacher, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        //TODO class awesomeValidation.addValidation(this,R.id.add_module_class,".{3,}",R.string.invalid_code);


        eAddModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modulename = eModulename.getText().toString();
                mteacher = eTeacher.getText().toString();
                moduleCode = eCode.getText().toString();
                mClass = eClass.getText().toString();

                ModuleModel dto = new ModuleModel();
                dto.setName(modulename);
                dto.setCode(moduleCode);
                dto.setDescription("some description");
                //TODO add students and teacher to dto

                moduleService.saveOrUpdate(dto, new AddModuleCallback());

                startActivity(new Intent(AddModule.this, Modules.class));

            }
        });

    }



    private class AddModuleCallback implements ICallback<ModuleModel>
    {

        @Override
        public void onSuccess(ModuleModel moduleModel)
        {
            Toast.makeText(getApplicationContext(), "Module added successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddModule.this, Modules.class));
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(AddModule.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }



}


