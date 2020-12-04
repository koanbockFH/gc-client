package com.example.attendencemonitor.activity.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.attendencemonitor.activity.admin.user.UserListFragment;

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
                return UserListFragment.newInstance(true);
            case 1:
                return UserListFragment.newInstance(false);
            case 2:
                return UserListFragment.newInstance(true);
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
