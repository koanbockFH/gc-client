package com.example.attendencemonitor.activity.admin.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.module.classlist.detail.StudentDetailActivity;
import com.example.attendencemonitor.activity.module.classlist.detail.module.StudentModuleListAdapter;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.model.ModuleStatisticModelBase;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/***
 * Shows All Modules of a student for the administrator, with the ability to get further information on them
 */
public class StudentModuleStatisticActivity extends BaseMenuActivity
{
    public static final String EXTRA_STUDENT_ID = "STUDENT_ID";
    public static final String EXTRA_STUDENT_NAME = "STUDENT_NAME";

    private int studentId;
    private List<ModuleStatisticModelBase> moduleList = new ArrayList<>();
    private StudentModuleListAdapter adapter;
    private final IAttendanceService attendanceService = new AttendanceService();
    private EditText searchBox;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //read the student in question from the intent that started the activity
        Intent received = getIntent();
        studentId = received.getIntExtra(EXTRA_STUDENT_ID, -1);
        title = received.getStringExtra(EXTRA_STUDENT_NAME);

        //initialize the menu and reuse the title from the opening student detail activity
        initializeMenu(title + " | Modules", true);
        super.onCreate(savedInstanceState);

        if (studentId == -1)
        {
            finish();
        }

        //inflate recycler view with all modules of the student
        setContentView(R.layout.fragment_student_module);
        RecyclerView rv = findViewById(R.id.rv_module_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        adapter = new StudentModuleListAdapter(moduleList, new ListItemListener());

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);

        loadData();

        //init search box
        searchBox = findViewById(R.id.et_searchbox);
        searchBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void afterTextChanged(Editable editable)
            {
                displayData();
            }
        });

        ImageButton delSearch = findViewById(R.id.ib_delete_search_smodule);
        delSearch.setOnClickListener(v -> searchBox.setText(""));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadData();
    }


    /***
     * Filter the displayed module list based on a search value (name, code, teacher)
     * @param searchValue searchvalue
     * @return list of modules to be displayed
     */
    private List<ModuleStatisticModelBase> filter(String searchValue)
    {
        List<ModuleStatisticModelBase> filteredList = new ArrayList<>();

        for(ModuleStatisticModelBase u: moduleList)
        {
            if(searchValue == null || searchValue.isEmpty())
            {
                filteredList.add(u);
            }
            else if(u.getName().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getCode().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getTeacher().getFullName().toLowerCase().contains(searchValue.toLowerCase()))
            {
                filteredList.add(u);
            }
        }

        return filteredList;
    }

    private void loadData(){
        attendanceService.getStudentModuleStats(studentId, new GetCallback());
    }

    private void displayData(){
        adapter.setItems(filter(searchBox.getText().toString()));
    }

    /***
     * open details of specific module
     * @param module module in question
     */
    private void onOpenstudentDetails(ModuleStatisticModelBase module) {
        Intent i = new Intent(this, StudentDetailActivity.class);
        //add all additional information for the students details
        i.putExtra(StudentDetailActivity.EXTRA_STUDENT_ID, studentId);
        i.putExtra(StudentDetailActivity.EXTRA_MODULE_ID, module.getId());
        i.putExtra(StudentDetailActivity.EXTRA_STUDENT_NAME, String.format(Locale.getDefault(), "%s | %s", title, module.getName()));
        i.putExtra(StudentDetailActivity.EXTRA_STUDENT_ATTENDED, module.getAttended());
        i.putExtra(StudentDetailActivity.EXTRA_STUDENT_ABSENT, module.getAbsent());
        i.putExtra(StudentDetailActivity.EXTRA_WITHOUT_MODULES, true);
        startActivity(i);
    }

    /***
     * Callback for response of backend on Get All Modules with statistical information
     */
    private class GetCallback implements ICallback<List<ModuleStatisticModelBase>>
    {
        @Override
        public void onSuccess(List<ModuleStatisticModelBase> value)
        {
            moduleList = value;
            displayData();
        }

        @Override
        public void onError(Throwable error)
        {

        }
    }

    /***
     * ListItem listener for recyclerview and handling of events from each row
     */
    private class ListItemListener implements IRecyclerViewItemEventListener<ModuleStatisticModelBase>
    {
        @Override
        public void onClick(ModuleStatisticModelBase item)
        {
            onOpenstudentDetails(item);
        }

        @Override
        public void onLongPress(ModuleStatisticModelBase item)
        {
            // not used
        }

        @Override
        public void onPrimaryClick(ModuleStatisticModelBase item)
        {

        }

        @Override
        public void onSecondaryActionClick(ModuleStatisticModelBase item)
        {
            //not used
        }
    }
}