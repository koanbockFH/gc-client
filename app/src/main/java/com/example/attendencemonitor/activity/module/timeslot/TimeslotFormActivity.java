package com.example.attendencemonitor.activity.module.timeslot;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.TimeslotService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.ITimeslotService;
import com.example.attendencemonitor.service.model.TimeslotModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeslotFormActivity extends BaseMenuActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    EditText name, date, startTime, endTime;
    SimpleDateFormat dateFormatter =  new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
    boolean isStartTime = true;
    ITimeslotService timeslotService = new TimeslotService();
    public static final String EXTRA_MODULE_ID = "MODULE_ID";
    public static final String EXTRA_TIMESLOT_ID = "TIMESLOT_ID";
    private int moduleId = -1;
    TimeslotModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Timeslot Form", true);
        super.onCreate(savedInstanceState);

        Intent received = getIntent();
        moduleId = received.getIntExtra(EXTRA_MODULE_ID, -1);
        int timeslotId = received.getIntExtra(EXTRA_TIMESLOT_ID, -1);

        if (moduleId == -1)
        {
            finish();
        }

        if(timeslotId != -1)
        {
            timeslotService.getById(timeslotId, new GetTimeslotCallback());
        }
        else{
            TimeslotModel module = new TimeslotModel();
            module.setStartDate(new Date());
            module.setEndDate(new Date());
            initView(module);
        }
    }

    private void initView(TimeslotModel currentValue) {
        if(currentValue != null)
        {
            model = currentValue;
        }

        setContentView(R.layout.activity_timeslot_form);

        name = findViewById(R.id.add_timeslot_name);
        startTime = findViewById(R.id.add_timeslot_startTime);
        endTime = findViewById(R.id.add_timeslot_endTime);
        date = findViewById(R.id.add_timeslot_date);

        startTime.setOnClickListener((View v) -> showTimePickerDialog(true));
        endTime.setOnClickListener((View v) -> showTimePickerDialog(false));
        date.setOnClickListener((View v) -> showDatePickerDialog());

        name.setText(model.getName());
        date.setText(String.format("Date: %s", dateFormatter.format(model.getStartDate().getTime())));
        startTime.setText(String.format("Start Time: %s", timeFormatter.format(model.getStartDate().getTime())));
        endTime.setText(String.format("End Time: %s", timeFormatter.format(model.getEndDate().getTime())));
    }

    public void onSubmit(View v)
    {
        model.setName(name.getText().toString());
        timeslotService.saveOrUpdate(moduleId, model, new SaveOrUpdateTimeslotCallback());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(model.getStartDate());
        cal.set(year, month, dayOfMonth);
        model.setStartDate(cal.getTime());

        cal.setTime(model.getEndDate());
        cal.set(year, month, dayOfMonth);
        model.setEndDate(cal.getTime());

        date.setText(String.format("Date: %s", dateFormatter.format(model.getStartDate().getTime())));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        if(isStartTime){
            cal.setTime(model.getStartDate());
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            model.setStartDate(cal.getTime());
            startTime.setText(String.format("Start Time: %s", timeFormatter.format(model.getStartDate().getTime())));
        }else {
            cal.setTime(model.getEndDate());
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            model.setEndDate(cal.getTime());
            endTime.setText(String.format("End Time: %s", timeFormatter.format(model.getEndDate().getTime())));
        }
    }

    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(model.getStartDate());
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void showTimePickerDialog(boolean startTime){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime ? model.getStartDate() : model.getEndDate());
        TimePickerDialog timePicker = new TimePickerDialog(
                this,
                this,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
        );
        isStartTime = startTime;
        timePicker.show();
    }

    private class GetTimeslotCallback implements ICallback<TimeslotModel> {

        @Override
        public void onSuccess(TimeslotModel value)
        {
            initView(value);
        }

        @Override
        public void onError(Throwable error)
        {
            initView(null);
        }
    }

    private class SaveOrUpdateTimeslotCallback implements ICallback<TimeslotModel>{

        @Override
        public void onSuccess(TimeslotModel value)
        {
            if(model.getId() > 0)
            {
                Toast.makeText(getApplicationContext(), "Timeslot changed successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Timeslot added successfully", Toast.LENGTH_SHORT).show();
            }
            finish();
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(TimeslotFormActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}