package com.example.attendencemonitor.activity.module.timeslot;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.module.ModuleAdd;
import com.example.attendencemonitor.service.TimeslotService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.ITimeslotService;
import com.example.attendencemonitor.service.model.TimeslotModel;

import java.util.ArrayList;
import java.util.Collections;

public class TimeslotListActivity extends BaseMenuActivity
{
    ITimeslotService timeslotService = new TimeslotService();
    public static final String EXTRA_MODULE_ID = "MODULE_ID";
    private int moduleId = -1;

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

        timeslotService.getAll(moduleId, new TimeslotListCallback());
    }

    private void readValues(TimeslotModel[] values)
    {
        RecyclerView rv = findViewById(R.id.rv_timeslot_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        ArrayList<TimeslotModel> items = new ArrayList<>();
        Collections.addAll(items, values);
        TimeslotListAdapter adapter = new TimeslotListAdapter(items);

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
        startActivity(new Intent(this, ModuleAdd.class));
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
}