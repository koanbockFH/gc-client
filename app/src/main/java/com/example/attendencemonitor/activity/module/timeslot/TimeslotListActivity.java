package com.example.attendencemonitor.activity.module.timeslot;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.TimeslotService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.ITimeslotService;
import com.example.attendencemonitor.service.model.TimeslotModel;

import java.util.ArrayList;
import java.util.Collections;

public class TimeslotListActivity extends BaseMenuActivity
{
    public static final String EXTRA_MODULE_ID = "MODULE_ID";
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private int moduleId = -1;
    ITimeslotService timeslotService = new TimeslotService();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Timeslots", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeslot_list);

        Intent received = getIntent();
        moduleId = received.getIntExtra(EXTRA_MODULE_ID, -1);

        if (moduleId == -1)
        {
            finish();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }

        timeslotService.getAll(moduleId, new TimeslotListCallback());
    }

    private void readValues(TimeslotModel[] values)
    {
        RecyclerView rv = findViewById(R.id.rv_timeslot_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        ArrayList<TimeslotModel> items = new ArrayList<>();
        Collections.addAll(items, values);
        TimeslotListAdapter adapter = new TimeslotListAdapter(items, this);

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        timeslotService.getAll(moduleId, new TimeslotListCallback());
    }

    public void onAdd(View view)
    {
        Intent addTimeslot = new Intent(this, TimeslotAddActivity.class);
        addTimeslot.putExtra(TimeslotAddActivity.EXTRA_MODULE_ID, moduleId);
        startActivity(addTimeslot);
    }

    private class TimeslotListCallback implements ICallback<TimeslotModel[]>
    {

        @Override
        public void onSuccess(TimeslotModel[] value)
        {
            readValues(value);
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(TimeslotListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                return;
        }
    }
}