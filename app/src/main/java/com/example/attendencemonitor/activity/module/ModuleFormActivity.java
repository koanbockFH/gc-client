package com.example.attendencemonitor.activity.module;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.user.UserSearchActivity;
import com.example.attendencemonitor.service.AppData;
import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.service.model.UserType;

import java.util.ArrayList;
import java.util.Locale;

public class ModuleFormActivity extends BaseMenuActivity
{
    private final IModuleService moduleService = new ModuleService();
    private static final int REQUEST_STUDENT_SELECTION = 1;
    private static final int REQUEST_TEACHER_SELECTION = 2;
    public static final String EXTRA_MODULE_ID = "MODULE_ID";

    private EditText eName;
    private EditText eCode;
    private EditText eTeacher;
    private EditText eDescription;
    private EditText eStudents;

    private Button delModule;

    private ModuleModel model = new ModuleModel();

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeMenu("Module Form", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_form);

        //Buttons/Textfields
        eName = findViewById(R.id.add_module_name);
        eCode = findViewById(R.id.add_module_code);
        eTeacher = findViewById(R.id.add_module_teacher);
        eDescription = findViewById(R.id.add_module_description);
        eStudents = findViewById(R.id.add_module_class);

        delModule = findViewById(R.id.btn_delete_module);

        //validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.add_module_name,RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.add_module_code,".{3,}",R.string.invalid_code);
        awesomeValidation.addValidation(this,R.id.add_module_teacher, RegexTemplate.NOT_EMPTY,R.string.invalid_name);

        //load module if this call is to edit existing module
        Intent received = getIntent();
        int moduleId = received.getIntExtra(EXTRA_MODULE_ID, -1);

        if (moduleId > 0)
        {
            moduleService.getById(moduleId, new GetCallback());
        }
    }

    private void loadModel(){
        eName.setText(model.getName());
        eCode.setText(model.getCode());
        eDescription.setText(model.getDescription());
        eTeacher.setText(model.getTeacher().getFullName());
        eStudents.setText(String.format(Locale.getDefault(), "%s %d", getString(R.string.add_module_student_value), model.getStudents().size()));
    }

    //handle student or teacher selection process
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == REQUEST_STUDENT_SELECTION && data != null){
            model.setStudents(data.getParcelableArrayListExtra(UserSearchActivity.EXTRA_CURRENT_SELECTED));
            eStudents.setText(String.format(Locale.getDefault(),"%s %d", getString(R.string.add_module_student_value), model.getStudents().size()));
        }
        if(requestCode == REQUEST_TEACHER_SELECTION && data != null){
            ArrayList<UserModel> items = data.getParcelableArrayListExtra(UserSearchActivity.EXTRA_CURRENT_SELECTED);
            if(items.size() > 0)
            {
                model.setTeacher(items.get(0));
                eTeacher.setText(model.getTeacher().getFullName());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //open student selection
    public void onClassChange(View view)
    {
        Intent classIntent = new Intent(this, UserSearchActivity.class);

        classIntent.putParcelableArrayListExtra(UserSearchActivity.EXTRA_CURRENT_SELECTED, new ArrayList<>(model.getStudents()));
        classIntent.putExtra(UserSearchActivity.EXTRA_USER_TYPE, UserType.STUDENT.getKey());
        startActivityForResult(classIntent, REQUEST_STUDENT_SELECTION);
    }

    //open teacher selection
    public void onTeacherChange(View view)
    {
        Intent classIntent = new Intent(this, UserSearchActivity.class);
        ArrayList<UserModel> currentSelection = new ArrayList<>();
        if(model.getTeacher() != null)
        {
            currentSelection.add(model.getTeacher());
        }

        classIntent.putParcelableArrayListExtra(UserSearchActivity.EXTRA_CURRENT_SELECTED, currentSelection);
        classIntent.putExtra(UserSearchActivity.EXTRA_USER_TYPE, UserType.TEACHER.getKey());
        startActivityForResult(classIntent, REQUEST_TEACHER_SELECTION);
    }

    //save module to backend
    public void onSubmit(View view)
    {
        model.setName(eName.getText().toString());
        model.setCode(eCode.getText().toString());
        model.setDescription(eDescription.getText().toString());

        if(awesomeValidation.validate())
        {
            moduleService.saveOrUpdate(model, new SaveOrUpdateModuleCallback());
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please check your input", Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * Callback for response of backend on save module
     */
    private class SaveOrUpdateModuleCallback implements ICallback<ModuleModel>
    {
        @Override
        public void onSuccess(ModuleModel moduleModel)
        {
            if(model.getId() > 0)
            {
                Toast.makeText(getApplicationContext(), "Module changed successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Module added successfully", Toast.LENGTH_SHORT).show();
            }
            finish();
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(ModuleFormActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * Callback for response of backend on get module
     */
    private class GetCallback implements ICallback<ModuleModel> {

        @Override
        public void onSuccess(ModuleModel value)
        {
            model = value;
            loadModel();

            //setup delete if module exists
            delModule.setVisibility(View.VISIBLE);
            delModule.setOnClickListener(v -> {
                if(AppData.getInstance().getUserType() != UserType.ADMIN)
                {
                    return;
                }

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            moduleService.delete(value, new DeleteCallback());
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ModuleFormActivity.this);
                builder.setMessage("Do you want to delete the module?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            });
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(ModuleFormActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * Callback for response of backend on sdelete module
     */
    private class DeleteCallback implements IActionCallback
    {
        @Override
        public void onSuccess()
        {
            Toast.makeText(ModuleFormActivity.this, "Module has been deleted!", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(ModuleFormActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}


