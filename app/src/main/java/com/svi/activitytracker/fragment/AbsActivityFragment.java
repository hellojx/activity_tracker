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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_manage:
                startActivity(new Intent(getActivity(), ManageActivity.class));
                return true;
            case R.id.action_logout:
                Toast.makeText(getActivity(), "Logout pressed...", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }

}
