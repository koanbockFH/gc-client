package com.example.attendencemonitor.activity.module;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.user.UserSearchActivity;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.service.model.UserType;

import java.util.ArrayList;

public class ModuleAdd extends AppCompatActivity {

    IModuleService moduleService = new ModuleService();
    private static final int REQUEST_STUDENT_SELECTION = 1;
    private static final int REQUEST_TEACHER_SELECTION = 2;

    private EditText eName;
    private EditText eCode;
    private EditText eTeacher;
    private EditText eDescription;
    private EditText eStudents;

    UserModel mTeacher = null;
    ArrayList<UserModel> mStudents = new ArrayList<>();

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_add);

        //Buttons/Textfields
        eName = (EditText) findViewById(R.id.add_module_name);
        eCode = (EditText) findViewById(R.id.add_module_code);
        eTeacher = (EditText) findViewById(R.id.add_module_teacher);
        eDescription = (EditText) findViewById(R.id.add_module_description);
        eStudents = (EditText) findViewById(R.id.add_module_class);

        //validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.add_module_name,RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.add_module_code,".{3,}",R.string.invalid_code);
        awesomeValidation.addValidation(this,R.id.add_module_teacher, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == REQUEST_STUDENT_SELECTION && data != null){
            mStudents = data.getParcelableArrayListExtra(UserSearchActivity.EXTRA_CURRENT_SELECTED);
            eStudents.setText(String.format("%s %d", getString(R.string.add_module_student_value), mStudents.size()));
        }
        if(requestCode == REQUEST_TEACHER_SELECTION && data != null){
            ArrayList<UserModel> items = data.getParcelableArrayListExtra(UserSearchActivity.EXTRA_CURRENT_SELECTED);
            if(items.size() > 0)
            {
                mTeacher = items.get(0);
                eTeacher.setText(mTeacher.getFullName());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClassChange(View view)
    {
        Intent classIntent = new Intent(this, UserSearchActivity.class);

        classIntent.putParcelableArrayListExtra(UserSearchActivity.EXTRA_CURRENT_SELECTED, mStudents);
        classIntent.putExtra(UserSearchActivity.EXTRA_USER_TYPE, UserType.STUDENT.getKey());
        startActivityForResult(classIntent, REQUEST_STUDENT_SELECTION);
    }

    public void onTeacherChange(View view)
    {
        Intent classIntent = new Intent(this, UserSearchActivity.class);
        ArrayList<UserModel> currentSelection = new ArrayList<>();
        if(mTeacher != null)
        {
            currentSelection.add(mTeacher);
        }

        classIntent.putParcelableArrayListExtra(UserSearchActivity.EXTRA_CURRENT_SELECTED, currentSelection);
        classIntent.putExtra(UserSearchActivity.EXTRA_USER_TYPE, UserType.TEACHER.getKey());
        startActivityForResult(classIntent, REQUEST_TEACHER_SELECTION);
    }

    public void onSubmit(View view)
    {
        ModuleModel dto = new ModuleModel();
        dto.setName(eName.getText().toString());
        dto.setStudents(mStudents);
        dto.setCode(eCode.getText().toString());
        dto.setDescription(eDescription.getText().toString());
        dto.setTeacher(mTeacher);

        if(awesomeValidation.validate())
        {
            moduleService.saveOrUpdate(dto, new AddModuleCallback());
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please check your input", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddModuleCallback implements ICallback<ModuleModel>
    {
        @Override
        public void onSuccess(ModuleModel moduleModel)
        {
            Toast.makeText(getApplicationContext(), "Module added successfully", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(ModuleAdd.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}


