package com.example.attendencemonitor.activity.module.classlist.detail.timeslot;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.model.StudentTimeslotStatisticModel;
import com.example.attendencemonitor.service.model.TimeslotModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentTimeslotFragment extends Fragment implements DatePickerDialog.OnDateSetListener
{
    private final IAttendanceService attendanceService = new AttendanceService();
    private List<TimeslotModel> timeslotList = new ArrayList<>();
    private int moduleId, studentId;
    private StudentTimeslotListAdapter adapter;
    private EditText date, searchBox;
    private SimpleDateFormat dateFormatter =  new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    private Date searchDate;
    private boolean isAttending;

    public StudentTimeslotFragment()
    {
        // Required empty public constructor
    }

    public static StudentTimeslotFragment newInstance(int moduleId, int studentId, boolean isAttending)
    {
        StudentTimeslotFragment fragment = new StudentTimeslotFragment();
        Bundle args = new Bundle();
        fragment.moduleId = moduleId;
        fragment.studentId = studentId;
        fragment.isAttending = isAttending;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_timeslot, container, false);

        RecyclerView rv = view.findViewById(R.id.rv_timeslot_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        adapter = new StudentTimeslotListAdapter(timeslotList);

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);

        view.findViewById(R.id.fab_timeslot_add).setVisibility(View.GONE);

        ImageButton delDateSearch = view.findViewById(R.id.ib_delete_date_search);
        delDateSearch.setOnClickListener(v -> {
            searchDate = null;
            date.setText(null);
            displayData();
        });

        date = view.findViewById(R.id.search_timeslot_date);
        date.setOnClickListener((View v) -> showDatePickerDialog());

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
                displayData();
            }
        });

        ImageButton delSearch = view.findViewById(R.id.ib_delete_search);
        delSearch.setOnClickListener(v -> {
            searchBox.setText("");
        });
        return view;
    }

    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(searchDate == null ? new Date() : searchDate);
        DatePickerDialog datePicker = new DatePickerDialog(
                getActivity(),
                this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(searchDate == null ? new Date() : searchDate);
        cal.set(year, month, dayOfMonth);
        searchDate = cal.getTime();

        date.setText(String.format("Date: %s", dateFormatter.format(searchDate.getTime())));
        displayData();
    }

    private List<TimeslotModel> filter(String searchValue)
    {
        if(searchValue.isEmpty())
        {
            searchValue = null;
        }

        List<TimeslotModel> filteredList = new ArrayList<>();

        for(TimeslotModel u: timeslotList)
        {
            boolean matchSearch = searchValue == null, matchDate = searchDate == null;
            if(searchValue != null && u.getName().toLowerCase().contains(searchValue.toLowerCase()))
            {
                matchSearch = true;
            }
            if(searchDate != null &&
                    (dateFormatter.format(u.getStartDate().getTime()).equals(dateFormatter.format(searchDate.getTime())) ||
                            dateFormatter.format(u.getEndDate().getTime()).equals(dateFormatter.format(searchDate.getTime()))))
            {
                matchDate = true;
            }

            if(matchDate && matchSearch)
            {
                filteredList.add(u);
            }
        }

        return filteredList;
    }

    private void loadData(){
        attendanceService.getStudentTimeslotStats(moduleId, studentId, new GetCallback());
    }

    private void displayData(){
        adapter.setItems(filter(searchBox.getText().toString()));
    }

    private class GetCallback implements ICallback<StudentTimeslotStatisticModel>
    {
        @Override
        public void onSuccess(StudentTimeslotStatisticModel value)
        {
            timeslotList = isAttending ? value.getAttended() : value.getAbsent();
            displayData();
        }

        @Override
        public void onError(Throwable error)
        {

        }
    }
}