package com.example.attendencemonitor.activity.module.timeslot.detail;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.model.TimeslotStatisticDetailModel;
import com.google.android.material.tabs.TabLayout;

public class TimeslotDetailActivity extends BaseMenuActivity
{
    public static final String EXTRA_TIMESLOT_ID = "TIMESLOT_ID";
    public static final String EXTRA_TIMESLOT_NAME = "STUDENT_NAME";
    private final IAttendanceService attendanceService = new AttendanceService();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //get additional data from calling intent
        Intent received = getIntent();
        int timeslotId = received.getIntExtra(EXTRA_TIMESLOT_ID, -1);
        String title = received.getStringExtra(EXTRA_TIMESLOT_NAME);
        if (timeslotId == -1)
        {
            finish();
        }

        //request from backend, and register the callback handling the response
        attendanceService.getTimeslotStats(timeslotId, new GetCallback());

        initializeMenu(title, true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeslot_detail);
    }

    private void loadData(TimeslotStatisticDetailModel values)
    {
        TimeslotDetailPagerAdapter sectionsPagerAdapter = new TimeslotDetailPagerAdapter(getSupportFragmentManager(), values);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    /***
     * Callback for response of backend on Get timeslot details
     */
    private class GetCallback implements ICallback<TimeslotStatisticDetailModel>
    {

        @Override
        public void onSuccess(TimeslotStatisticDetailModel value)
        {
            loadData(value);
        }

        @Override
        public void onError(Throwable error)
        {
            finish();
        }
    }
}