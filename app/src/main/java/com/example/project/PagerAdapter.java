package com.example.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {
    private static final int NUM_TABS = 2;
    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                RecordsFragment records = new RecordsFragment();
                records.setArguments(getArgumentsWithPatient());
                return records;
            case 1:
                ReservationsFragment reservations = new ReservationsFragment();
                reservations.setArguments(getArgumentsWithPatient());
                return reservations;
            default: return new RecordsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }

    private Bundle getArgumentsWithPatient() {
        Bundle bundle = new Bundle();
        bundle.putString("USERID", MainActivity.userID);
        return bundle;
    }
}
