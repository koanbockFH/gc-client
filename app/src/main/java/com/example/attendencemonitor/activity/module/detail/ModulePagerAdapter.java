package com.example.attendencemonitor.activity.module.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.attendencemonitor.activity.module.classlist.ClasslistFragment;
import com.example.attendencemonitor.activity.module.timeslot.TimeslotFragment;
import com.example.attendencemonitor.service.model.ModuleModel;

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
        //Return a new Instance of the fragment in question
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
    public CharSequence getPageTitle(int position)
    {
        //define the titles based on the tabs
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
        //return the amount of tabs to be displayed
        return 2;
    }
}
