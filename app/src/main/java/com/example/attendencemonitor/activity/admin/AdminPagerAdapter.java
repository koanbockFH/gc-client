package com.example.attendencemonitor.activity.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.attendencemonitor.activity.module.classlist.ClasslistFragment;
import com.example.attendencemonitor.activity.module.timeslot.TimeslotFragment;
import com.example.attendencemonitor.service.model.ModuleModel;

public class AdminPagerAdapter extends FragmentPagerAdapter
{
    public AdminPagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            default:
            case 0:
                return ModuleListFragment.newInstance();
            case 1:
                return new Fragment();
            case 2:
                return new Fragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            default:
            case 0:
                return "Modules";
            case 1:
                return "Students";
            case 2:
                return "Teachers";
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
