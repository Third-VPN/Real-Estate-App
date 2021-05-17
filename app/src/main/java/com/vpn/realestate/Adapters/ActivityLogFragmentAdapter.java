package com.vpn.realestate.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vpn.realestate.ContactedPropertyFragment;
import com.vpn.realestate.MyPropertyFragment;

public class ActivityLogFragmentAdapter extends FragmentPagerAdapter {
    private int tabNo;

    public ActivityLogFragmentAdapter(@NonNull FragmentManager fm, int behavior, int tabNo) {
        super(fm, behavior);
        this.tabNo = tabNo;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new MyPropertyFragment();

            case 1:
                return new ContactedPropertyFragment();

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return tabNo;
    }
}
