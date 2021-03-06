package com.example.attendencemonitor.activity.module.classlist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.module.classlist.detail.StudentDetailActivity;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.ModuleStatisticModel;
import com.example.attendencemonitor.service.model.StudentModuleStatisticModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.ArrayList;
import java.util.List;

/***
 * Show all students of a specific module
 */
public class ClasslistFragment extends Fragment
{
    private final IAttendanceService attendanceService = new AttendanceService();
    private ModuleModel module;
    private StudentListAdapter adapter;
    private ModuleStatisticModel moduleStats;
    private EditText searchBox;

    public ClasslistFragment()
    {
        // Required empty public constructor
    }

    //static creation of fragment - best practice described in official android documentation
    public static ClasslistFragment newInstance(ModuleModel module)
    {
        ClasslistFragment fragment = new ClasslistFragment();
        fragment.module = module;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        attendanceService.getModuleStats(module.getId(), new GetStatsCallback());
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        attendanceService.getModuleStats(module.getId(), new GetStatsCallback());
        View view = inflater.inflate(R.layout.fragment_classlist, container, false);

        //init recycler view
        RecyclerView rv = view.findViewById(R.id.rv_user_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        adapter = new StudentListAdapter(filter(null), new ListItemListener());

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);

        //init search
        searchBox = view.findViewById(R.id.et_searchbox);
        searchBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void afterTextChanged(Editable editable)
            {
                String searchValue = editable.toString();
                adapter.setItems(filter(searchValue));
            }
        });

        ImageButton delSearch = view.findViewById(R.id.ib_delete_search_classlist);
        delSearch.setOnClickListener(v -> searchBox.setText(""));

        return view;
    }

    /***
     * Filter the displayed user list based on a search value (name, code, mail)
     * @param searchValue searchvalue
     * @return list of modules to be displayed
     */
    private List<StudentModuleStatisticModel> filter(String searchValue)
    {
        List<StudentModuleStatisticModel> filteredList = new ArrayList<>();
        if(moduleStats == null)
        {
            return filteredList;
        }

        for(StudentModuleStatisticModel u: moduleStats.getStudents())
        {
            if(searchValue == null || searchValue.isEmpty())
            {
                filteredList.add(u);
            }
            else if(u.getMail().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getCode().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getFullName().toLowerCase().contains(searchValue.toLowerCase()))
            {
                filteredList.add(u);
            }
        }

        return filteredList;
    }

    /***
     * open student details
     * @param student student in question
     */
    private void onOpenstudentDetails(StudentModuleStatisticModel student) {
        searchBox.setText("");
        Intent i = new Intent(getActivity(), StudentDetailActivity.class);
        i.putExtra(StudentDetailActivity.EXTRA_STUDENT_ID, student.getId());
        i.putExtra(StudentDetailActivity.EXTRA_MODULE_ID, module.getId());
        i.putExtra(StudentDetailActivity.EXTRA_STUDENT_NAME, student.getFullName());
        i.putExtra(StudentDetailActivity.EXTRA_STUDENT_ATTENDED, student.getAttended());
        i.putExtra(StudentDetailActivity.EXTRA_STUDENT_ABSENT, student.getAbsent());
        startActivity(i);
    }

    private void makeToast(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /***
     * Callback for response of backend on Get All students of the module
     */
    private class GetStatsCallback implements ICallback<ModuleStatisticModel>
    {
        @Override
        public void onSuccess(ModuleStatisticModel value)
        {
            moduleStats = value;
            adapter.setItems(filter(null));
        }

        @Override
        public void onError(Throwable error){ makeToast(error.getMessage());}
    }

    /***
     * ListItem listener for recyclerview and handling of events from each row
     */
    private class ListItemListener implements IRecyclerViewItemEventListener<StudentModuleStatisticModel>
    {

        @Override
        public void onClick(StudentModuleStatisticModel item)
        {
            onOpenstudentDetails(item);
        }

        @Override
        public void onLongPress(StudentModuleStatisticModel item)
        {

        }

        @Override
        public void onPrimaryClick(StudentModuleStatisticModel item)
        {

        }

        @Override
        public void onSecondaryActionClick(StudentModuleStatisticModel item)
        {

        }
    }
}