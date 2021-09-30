package com.example.roomfinderapp.adapter_classes;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.roomfinderapp.fragments.MapsFragment;
import com.example.roomfinderapp.fragments.HomeFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context myContext;
    int totalTabs;

    public ViewPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        this.myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new MapsFragment();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return totalTabs;
    }
}
