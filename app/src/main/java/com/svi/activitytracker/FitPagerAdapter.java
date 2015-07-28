package com.svi.activitytracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class FitPagerAdapter extends FragmentPagerAdapter {

    public static class FragmentDescription {
        private int index;
        private String title;

        public int getIndex() {
            return index;
        }

        public String getTitle() {
            return title;
        }

        public FragmentDescription(int index, String title) {
            this.index = index;
            this.title = title;
        }
    }

    public FragmentDescription[] getFragmentDescriptions() {
        return fragmentDescriptions;
    }

    private FragmentDescription[] fragmentDescriptions = {
            new FragmentDescription(FragmentIndex.DATASOURCES, "Datasources"),
            new FragmentDescription(FragmentIndex.SENSORS, "Sensors"),
            new FragmentDescription(FragmentIndex.RECORDING, "Recording"),
            new FragmentDescription(FragmentIndex.HISTORY, "History")
    };

    public static class FragmentIndex {
        public static final int DATASOURCES = 0;
        public static final int SENSORS = 1;
        public static final int RECORDING = 2;
        public static final int HISTORY = 3;
    }

    public FitPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position < fragmentDescriptions.length) {
            return ItemFragment.newInstance(position);
        }

        return null;
    }

    @Override
    public int getCount() {
        return fragmentDescriptions.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < fragmentDescriptions.length) {
            return fragmentDescriptions[position].getTitle();
        }

        return super.getPageTitle(position);
    }
}

