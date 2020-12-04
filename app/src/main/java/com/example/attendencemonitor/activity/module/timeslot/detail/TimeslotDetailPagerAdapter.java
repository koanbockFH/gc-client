package com.example.attendencemonitor.activity.module.timeslot.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.attendencemonitor.activity.module.timeslot.detail.student.TimeslotStudentFragment;
import com.example.attendencemonitor.service.model.TimeslotStatisticDetailModel;

import java.util.Locale;

public class TimeslotDetailPagerAdapter extends FragmentPagerAdapter
{
    private final TimeslotStatisticDetailModel statisticDetailModel;

    public TimeslotDetailPagerAdapter(@NonNull FragmentManager fm, TimeslotStatisticDetailModel statisticDetailModel)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.statisticDetailModel = statisticDetailModel;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            default:
            case 0:
                return TimeslotStudentFragment.newInstance(statisticDetailModel.getAttendees());
            case 1:
                return TimeslotStudentFragment.newInstance(statisticDetailModel.getAbsentees());
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            default:
            case 0:
                return String.format(Locale.getDefault(), "Attendees (%d)", statisticDetailModel.getAttended());
            case 1:
                return String.format(Locale.getDefault(), "Absentees (%d)", statisticDetailModel.getAbsent());
        }
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
