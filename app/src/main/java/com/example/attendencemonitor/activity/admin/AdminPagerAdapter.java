package com.example.attendencemonitor.activity.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.attendencemonitor.activity.admin.user.UserListFragment;

public class AdminPagerAdapter extends FragmentPagerAdapter
{
    /***
     * Create a new Instance of the AdminPagerAdapter for the tablayout
     * @param fm Fragmentmanager from the current activity
     */
    public AdminPagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
                //create the module tab fragment
                return ModuleListFragment.newInstance();
            case 1:
                //create the student tab fragment
                return UserListFragment.newInstance(false);
            case 2:
                //create the teacher tab fragment
                return UserListFragment.newInstance(true);
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
        //return the amount of tabs to be displayed
        return 3;
    }
}
