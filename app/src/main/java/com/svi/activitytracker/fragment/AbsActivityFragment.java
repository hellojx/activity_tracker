package com.svi.activitytracker.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.svi.activitytracker.R;
import com.svi.activitytracker.ui.ManageActivity;

public abstract class AbsActivityFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void updateArguments(Bundle data) {}

}
