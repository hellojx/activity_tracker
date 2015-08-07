package com.svi.activitytracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svi.activitytracker.R;

public class ActivityDetailfragment extends AbsActivityFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_detail_layout, container, false);




        return view;
    }
}
