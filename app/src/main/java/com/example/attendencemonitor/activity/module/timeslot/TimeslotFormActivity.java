package com.example.attendencemonitor.activity.module.timeslot;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.auth.RegisterActivity;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.AppData;
import com.example.attendencemonitor.service.TimeslotService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.ITimeslotService;
import com.example.attendencemonitor.service.model.TimeslotModel;
import com.example.attendencemonitor.service.model.UserType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeslotFormActivity extends BaseMenuActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    private EditText name, date, startTime, endTime;
    private final SimpleDateFormat dateFormatter =  new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private boolean isStartTime = true;
    private final ITimeslotService timeslotService = new TimeslotService();
    public static final String EXTRA_MODULE_ID = "MODULE_ID";
    public static final String EXTRA_TIMESLOT_ID = "TIMESLOT_ID";
    private int moduleId = -1;
    private TimeslotModel model;
    private Button delTimeslot;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Timeslot Form", true);
        super.onCreate(savedInstanceState);

        //read information for calling intent (module id and timeslotId if its to edit the timeslot)
        Intent received = getIntent();
        moduleId = received.getIntExtra(EXTRA_MODULE_ID, -1);
        int timeslotId = received.getIntExtra(EXTRA_TIMESLOT_ID, -1);

        if (moduleId == -1)
        {
            finish();
        }

        //if edit than load it from backend else create a new
        if(timeslotId != -1)
        {
            //request from backend, and register the callback handling the response
            timeslotService.getById(timeslotId, new GetTimeslotCallback());
        }
        else{
            TimeslotModel module = new TimeslotModel();
            module.setStartDate(new Date());
            module.setEndDate(new Date());
            initView(module);
        }

        //init validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.add_timeslot_name, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
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
        delTimeslot = findViewById(R.id.btn_delete_timeslot);

        //setup date selection
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
        if(awesomeValidation.validate())
        {
            //save if validation passes
            model.setName(name.getText().toString());
            if(model.getStartDate().getTime() > model.getEndDate().getTime())
            {

                Toast.makeText(getApplicationContext(), "Start time cannot be after end time, please check your input", Toast.LENGTH_LONG).show();
                return;
            }

            timeslotService.saveOrUpdate(moduleId, model, new SaveOrUpdateTimeslotCallback());
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please check your input", Toast.LENGTH_SHORT).show();
        }

    }

    //Event callback on date set
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

    //Event callback on time set
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

    /***
     * Show the date picker dialog so that the user can input date
     */
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

    /***
     * Show the date picker dialog so that the user can input time
     */
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

    /***
     * Callback for response of backend on Get timeslot if id is given
     */
    private class GetTimeslotCallback implements ICallback<TimeslotModel> {

        @Override
        public void onSuccess(TimeslotModel value)
        {
            initView(value);

            //setup delete if its edit
            delTimeslot.setVisibility(View.VISIBLE);
            delTimeslot.setOnClickListener(v -> {
                if(AppData.getInstance().getUserType() == UserType.STUDENT)
                {
                    return;
                }


                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            timeslotService.delete(value, new DeleteCallback());
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(TimeslotFormActivity.this);
                builder.setMessage("Do you want to delete the timeslot?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            });
        }

        @Override
        public void onError(Throwable error)
        {
            initView(null);
        }
    }

    /***
     * Callback for response of backend on Save timeslot
     */
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
            Toast.makeText(TimeslotFormActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * Callback for response of backend on delete timeslot
     */
    private class DeleteCallback implements IActionCallback
    {
        @Override
        public void onSuccess()
        {
            Toast.makeText(TimeslotFormActivity.this, "Timeslot has been deleted!", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(TimeslotFormActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}