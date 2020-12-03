package com.example.attendencemonitor.activity.module.classlist.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.attendencemonitor.activity.module.classlist.detail.timeslot.StudentTimeslotFragment;

import java.util.Locale;

public class StudentDetailPagerAdapter extends FragmentPagerAdapter
{
    private final int moduleId;
    private final int studentId;
    private final int attended;
    private final int absent;

    public StudentDetailPagerAdapter(@NonNull FragmentManager fm, int moduleId, int studentId, int attended, int absent)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.moduleId = moduleId;
        this.studentId = studentId;
        this.attended = attended;
        this.absent = absent;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            default:
            case 0:
                return StudentTimeslotFragment.newInstance(moduleId, studentId, true);
            case 1:
                return StudentTimeslotFragment.newInstance(moduleId, studentId, false);
            case 2:
                return StudentTimeslotFragment.newInstance(moduleId, studentId, true);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            default:
            case 0:
                return String.format(Locale.getDefault(), "Attended (%d)", attended);
            case 1:
                return String.format(Locale.getDefault(), "Absent (%d)", absent);
            case 2:
                return "Other Modules";
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
