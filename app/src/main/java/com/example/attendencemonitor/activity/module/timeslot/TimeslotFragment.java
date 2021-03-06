package com.example.attendencemonitor.activity.module.timeslot;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.module.timeslot.detail.TimeslotDetailActivity;
import com.example.attendencemonitor.activity.qr.ScannerActivity;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.TimeslotService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.ITimeslotService;
import com.example.attendencemonitor.service.dto.AttendDto;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.TimeslotModel;
import com.example.attendencemonitor.service.model.TimeslotStatisticModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeslotFragment extends Fragment implements DatePickerDialog.OnDateSetListener
{
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private static final int SCANNER_REQUEST = 2;
    private int timeslotId = -1;
    private ModuleModel module;
    private final IAttendanceService attendanceService = new AttendanceService();
    private TimeslotListAdapter adapter;
    private List<TimeslotStatisticModel> timeslotList;
    private EditText date, searchBox;
    private final SimpleDateFormat dateFormatter =  new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    private Date searchDate;

    public TimeslotFragment()
    {
        // Required empty public constructor
    }

    //static creation of fragment - best practice described in official android documentation
    public static TimeslotFragment newInstance(ModuleModel module)
    {
        TimeslotFragment fragment = new TimeslotFragment();
        fragment.module = module;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            attendanceService.getAllTimeslotStats(module.getId(), new TimeslotListCallback());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_timeslot, container, false);

        view.findViewById(R.id.fab_timeslot_add).setOnClickListener(this::onAdd);

        //init search
        ImageButton delDateSearch = view.findViewById(R.id.ib_delete_date_search);
        delDateSearch.setOnClickListener(v -> {
            searchDate = null;
            date.setText(null);
            adapter.setItems(filter(searchBox.getText().toString()));
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
                String searchValue = editable.toString();
                adapter.setItems(filter(searchValue));
            }
        });

        ImageButton delSearch = view.findViewById(R.id.ib_delete_search);
        delSearch.setOnClickListener(v -> searchBox.setText(""));

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        attendanceService.getAllTimeslotStats(module.getId(), new TimeslotListCallback());
    }

    //request result on permission request -- open scanner activity if camara permission is given
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ZXING_CAMERA_PERMISSION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent scanner = new Intent(getActivity(), ScannerActivity.class);
                this.startActivity(scanner);
            }
            else
            {
                Toast.makeText(getActivity(), R.string.onRequestPermissionsResult, Toast.LENGTH_LONG).show();
            }
        }
    }

    //handle result from scanner
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNER_REQUEST && data != null)
        {
            String studentCode = data.getStringExtra(ScannerActivity.EXTRA_RESULT_PAYLOAD);

            AttendDto dto = new AttendDto();
            dto.setStudentCode(studentCode);
            dto.setTimeslotId(timeslotId);
            attendanceService.attend(dto, new AttendTimeslotCallback());
        }
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

        adapter.setItems(filter(searchBox.getText().toString()));
    }

    /***
     * Filter the displayed timeslot list based on a search value (name, code, mail)
     * @param searchValue searchvalue
     * @return list of timeslot to be displayed
     */
    private List<TimeslotStatisticModel> filter(String searchValue)
    {
        if(searchValue.isEmpty())
        {
            searchValue = null;
        }

        List<TimeslotStatisticModel> filteredList = new ArrayList<>();

        for(TimeslotStatisticModel u: timeslotList)
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

    private void readValues(List<TimeslotStatisticModel> values)
    {
        timeslotList = values;

        //init recycler view
        RecyclerView rv = getActivity().findViewById(R.id.rv_timeslot_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        adapter = new TimeslotListAdapter(filter(searchBox.getText().toString()), new ListItemListener());

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    //handle on adding new timeslots
    public void onAdd(View view)
    {
        searchBox.setText("");
        Intent addTimeslot = new Intent(getActivity(), TimeslotFormActivity.class);
        addTimeslot.putExtra(TimeslotFormActivity.EXTRA_MODULE_ID, module.getId());
        startActivity(addTimeslot);
    }

    //handle opening scanner activity and check for permission to use camera
    private void onOpenScanner(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }
        else {
            Intent scanner = new Intent(getActivity(), ScannerActivity.class);
            this.startActivityForResult(scanner, SCANNER_REQUEST);
        }
        searchBox.setText("");
    }

    //handle editing the timeslot
    private void onEditTimeslot(TimeslotModel timeslot) {
        searchBox.setText("");
        Intent i = new Intent(getActivity(), TimeslotFormActivity.class);
        i.putExtra(TimeslotFormActivity.EXTRA_MODULE_ID, module.getId());
        i.putExtra(TimeslotFormActivity.EXTRA_TIMESLOT_ID, timeslot.getId());
        startActivity(i);
    }

    private void makeToast(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //handle showing timeslot details/statistics
    private void openStats(TimeslotStatisticModel item){
        searchBox.setText("");
        Intent i = new Intent(getActivity(), TimeslotDetailActivity.class);
        i.putExtra(TimeslotDetailActivity.EXTRA_TIMESLOT_ID, item.getId());
        i.putExtra(TimeslotDetailActivity.EXTRA_TIMESLOT_NAME, item.getName());
        startActivity(i);
    }

    /***
     * Callback for response of backend on Get timeslot list
     */
    private class TimeslotListCallback implements ICallback<List<TimeslotStatisticModel>>
    {

        @Override
        public void onSuccess(List<TimeslotStatisticModel> value)
        {
            readValues(value);
        }

        @Override
        public void onError(Throwable error)
        {
            makeToast(error.getMessage());
        }
    }

    /***
     * Callback for response of backend on attending timeslot
     */
    private class AttendTimeslotCallback implements IActionCallback{

        @Override
        public void onSuccess()
        {
            makeToast("Attendance recorded!");
        }

        @Override
        public void onError(Throwable error)
        {
            makeToast(error.getMessage());
        }
    }

    /***
     * ListItem listener for recyclerview and handling of events from each row
     */
    private class ListItemListener implements IRecyclerViewItemEventListener<TimeslotStatisticModel>
    {
        @Override
        public void onClick(TimeslotStatisticModel item)
        {
            timeslotId = item.getId();
            onOpenScanner();
        }

        @Override
        public void onLongPress(TimeslotStatisticModel item)
        {
            timeslotId = item.getId();
        }

        @Override
        public void onPrimaryClick(TimeslotStatisticModel item)
        {
            timeslotId = item.getId();
            onEditTimeslot(item);
        }

        @Override
        public void onSecondaryActionClick(TimeslotStatisticModel item)
        {
            openStats(item);
        }
    }
}