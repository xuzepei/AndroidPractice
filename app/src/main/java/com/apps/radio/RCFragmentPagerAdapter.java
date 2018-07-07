package com.apps.radio;

/**
 * Created by xuzepei on 2018/2/7.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class RCFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public RCFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }
}
