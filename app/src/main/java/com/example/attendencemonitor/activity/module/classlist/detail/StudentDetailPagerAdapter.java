package com.example.attendencemonitor.activity.module.classlist.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.attendencemonitor.activity.module.classlist.detail.timeslot.StudentTimeslotFragment;

public class StudentDetailPagerAdapter extends FragmentPagerAdapter
{
    private final int moduleId;
    private final int studentId;

    public StudentDetailPagerAdapter(@NonNull FragmentManager fm, int moduleId, int studentId)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.moduleId = moduleId;
        this.studentId = studentId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            default:
            case 0:
                return StudentTimeslotFragment.newInstance();
            case 1:
                return StudentTimeslotFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            default:
            case 0:
                return "Attendance";
            case 1:
                return "Other Modules";
        }
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
