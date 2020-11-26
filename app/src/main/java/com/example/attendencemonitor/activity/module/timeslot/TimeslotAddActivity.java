package com.example.attendencemonitor.activity.module.timeslot;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.attendencemonitor.activity.module.ModuleAdd;
import com.example.attendencemonitor.service.TimeslotService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.ITimeslotService;
import com.example.attendencemonitor.service.model.TimeslotModel;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeslotAddActivity extends BaseMenuActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    EditText date, startTime, endTime;
    SimpleDateFormat dateFormatter, timeFormatter;
    Calendar startCal, endCal;
    boolean isStartTime = true;
    ITimeslotService timeslotService = new TimeslotService();
    public static final String EXTRA_MODULE_ID = "MODULE_ID";
    private int moduleId = -1;
    TimeslotModel model = new TimeslotModel();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Add Timeslot", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeslot_add);

        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        dateFormatter = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();

        handleStartTimeTextField();
        handleEndTimeTextField();
        handleDateTextField();

        Intent received = getIntent();
        moduleId = received.getIntExtra(EXTRA_MODULE_ID, -1);

        if (moduleId == -1)
        {
            finish();
        }
    }

    public void onAdd(View v)
    {
        EditText timeslot = findViewById(R.id.add_timeslot_name);
        model.setName(timeslot.getText().toString());
        model.setEndDate(endCal.getTime());
        model.setStartDate(startCal.getTime());
        timeslotService.saveOrUpdate(moduleId, model, new AddTimeslotCallback());
    }

    private class AddTimeslotCallback implements ICallback<TimeslotModel>{

        @Override
        public void onSuccess(TimeslotModel value)
        {
            Toast.makeText(getApplicationContext(), "Timeslot added successfully", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(TimeslotAddActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        startCal.set(
                year,
                month,
                dayOfMonth,
                startCal.get(Calendar.HOUR),
                startCal.get(Calendar.MINUTE));
        endCal.set(
                year,
                month,
                dayOfMonth,
                startCal.get(Calendar.HOUR),
                startCal.get(Calendar.MINUTE));
        date.setText("Date: "+dateFormatter.format(startCal.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if(isStartTime){
            startCal.set(
                    startCal.get(Calendar.YEAR),
                    startCal.get(Calendar.MONTH),
                    startCal.get(Calendar.DAY_OF_MONTH),
                    hourOfDay,
                    minute);
            startTime.setText("Start Time: "+timeFormatter.format(startCal.getTime()));
        }else {
            endCal.set(
                    startCal.get(Calendar.YEAR),
                    startCal.get(Calendar.MONTH),
                    startCal.get(Calendar.DAY_OF_MONTH),
                    hourOfDay,
                    minute);
            endTime.setText("End Time: "+timeFormatter.format(endCal.getTime()));
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void showTimePickerDialog(boolean startTime){
        TimePickerDialog timePicker = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
        );
        isStartTime = startTime;
        timePicker.show();
    }

    private void handleStartTimeTextField(){
        startTime = findViewById(R.id.add_timeslot_startTime);
        startTime.setText("Start Time: "+timeFormatter.format(startCal.getTime()));
        startTime.setOnClickListener((View v) -> showTimePickerDialog(true));
    }

    private void handleEndTimeTextField(){
        endTime = findViewById(R.id.add_timeslot_endTime);
        endTime.setText("End Time: "+timeFormatter.format(endCal.getTime()));
        endTime.setOnClickListener((View v) -> showTimePickerDialog(false));
    }

    private void handleDateTextField(){
        date = findViewById(R.id.add_timeslot_date);
        date.setText("Date: "+dateFormatter.format(startCal.getTime()));
        date.setOnClickListener((View v) -> showDatePickerDialog());
    }
}