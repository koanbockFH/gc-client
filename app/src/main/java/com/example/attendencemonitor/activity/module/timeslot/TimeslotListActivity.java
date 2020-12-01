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
import com.example.attendencemonitor.activity.qr.ScannerActivity;
import com.example.attendencemonitor.service.TimeslotService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.ITimeslotService;
import com.example.attendencemonitor.service.model.TimeslotModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

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

        timeslotService.getAll(moduleId, new TimeslotListCallback());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        timeslotService.getAll(moduleId, new TimeslotListCallback());
    }

    private void readValues(TimeslotModel[] values)
    {
        RecyclerView rv = findViewById(R.id.rv_timeslot_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        ArrayList<TimeslotModel> items = new ArrayList<>();
        Collections.addAll(items, values);
        TimeslotListAdapter adapter = new TimeslotListAdapter(items, new ListItemListener());

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    public void onAdd(View view)
    {
        Intent addTimeslot = new Intent(this, TimeslotFormActivity.class);
        addTimeslot.putExtra(TimeslotFormActivity.EXTRA_MODULE_ID, moduleId);
        startActivity(addTimeslot);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ZXING_CAMERA_PERMISSION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent scanner = new Intent(this, ScannerActivity.class);
                this.startActivity(scanner);
            }
            else
            {
                Toast.makeText(this, R.string.onRequestPermissionsResult, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onOpenScanner(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }
        else {
            Intent scanner = new Intent(this, ScannerActivity.class);
            this.startActivity(scanner);
        }
    }

    private void onDeleteTimeslot(TimeslotModel item){
        timeslotService.delete(item, new DeleteTimeslotCallback());
    }

    private void onEditTimeslot(TimeslotModel timeslot) {
        Intent i = new Intent(this, TimeslotFormActivity.class);
        i.putExtra(TimeslotFormActivity.EXTRA_MODULE_ID, moduleId);
        i.putExtra(TimeslotFormActivity.EXTRA_TIMESLOT_ID, timeslot.getId());
        startActivity(i);
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

    private class DeleteTimeslotCallback implements IActionCallback{

        @Override
        public void onSuccess()
        {
            Toast.makeText(TimeslotListActivity.this, "Timeslot has been deleted!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(TimeslotListActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ListItemListener implements IRecyclerViewItemEventListener<TimeslotModel>
    {
        @Override
        public void onClick(TimeslotModel item)
        {
            onOpenScanner();
        }

        @Override
        public void onLongPress(TimeslotModel item)
        {
            onDeleteTimeslot(item);
        }

        @Override
        public void onActionClick(TimeslotModel item)
        {
            onEditTimeslot(item);
        }
    }

}