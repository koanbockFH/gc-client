package com.example.attendencemonitor.activity.module.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.attendencemonitor.activity.module.classlist.ClasslistFragment;
import com.example.attendencemonitor.activity.module.statistics.ModuleStatisticsFragment;
import com.example.attendencemonitor.activity.module.timeslot.TimeslotFragment;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.ModuleStatisticModel;

public class ModulePagerAdapter extends FragmentPagerAdapter
{
    private final ModuleModel module;

    public ModulePagerAdapter(@NonNull FragmentManager fm, ModuleModel module)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.module = module;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            default:
            case 0:
                return TimeslotFragment.newInstance(module);
            case 1:
                return ClasslistFragment.newInstance(module);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            default:
            case 0:
                return "Timeslot";
            case 1:
                return "Classlist";
        }
    }



    @Override
    public int getCount()
    {
        return 2;
    }
}
