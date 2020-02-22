package com.example.nearbylocaton.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.nearbylocaton.fragments.LocationFragment;
import com.example.nearbylocaton.fragments.NearByFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private static final int total_pages = 2;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LocationFragment();
            case 1:
                return new NearByFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return total_pages;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Your Location";
            case 1:
                return "Search NearBy";
        }
        return null;
    }
}
