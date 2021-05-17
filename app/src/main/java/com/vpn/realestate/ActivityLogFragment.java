package com.vpn.realestate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.vpn.realestate.Adapters.ActivityLogFragmentAdapter;

public class ActivityLogFragment extends Fragment {
    TabLayout tlLog;
    TabItem tiMyProperty, tiContProperty;
    ViewPager vpFragContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_log, container, false);

        tlLog = view.findViewById(R.id.tlLog);

        tiMyProperty = view.findViewById(R.id.tiMyProperty);
        tiContProperty = view.findViewById(R.id.tiContProperty);

        vpFragContainer = view.findViewById(R.id.vpFragContainer);

        ActivityLogFragmentAdapter fragmentAdapter = new ActivityLogFragmentAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tlLog.getTabCount());
        vpFragContainer.setAdapter(fragmentAdapter);

        //tab select event
        tlLog.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpFragContainer.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //page swipe
        vpFragContainer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlLog));

        return view;
    }



}
