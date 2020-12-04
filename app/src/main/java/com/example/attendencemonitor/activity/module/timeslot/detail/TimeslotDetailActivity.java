package com.example.attendencemonitor.activity.module.timeslot.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.activity.module.classlist.detail.StudentDetailPagerAdapter;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.model.TimeslotStatisticDetailModel;
import com.example.attendencemonitor.service.model.TimeslotStatisticModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TimeslotDetailActivity extends BaseMenuActivity
{
    public static final String EXTRA_TIMESLOT_ID = "TIMESLOT_ID";
    public static final String EXTRA_TIMESLOT_NAME = "STUDENT_NAME";
    private final IAttendanceService attendanceService = new AttendanceService();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent received = getIntent();
        int timeslotId = received.getIntExtra(EXTRA_TIMESLOT_ID, -1);
        String title = received.getStringExtra(EXTRA_TIMESLOT_NAME);
        if (timeslotId == -1)
        {
            finish();
        }
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