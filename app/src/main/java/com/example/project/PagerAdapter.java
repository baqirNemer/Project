package com.example.project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 2; // Number of tabs

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Return the Fragment for each tab
        switch (position) {
            case 0:
                return new RecordsFragment(); // Fragment for the first tab
            case 1:
                return new ReservationsFragment(); // Fragment for the second tab
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS; // Number of tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Set tab titles
        switch (position) {
            case 0:
                return "Records";
            case 1:
                return "Reservations";
            default:
                return null;
        }
    }
}
