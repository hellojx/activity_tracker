package com.svi.activitytracker.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.svi.activitytracker.R;
import com.svi.activitytracker.common.Constants;
import com.svi.activitytracker.common.Display;
import com.svi.activitytracker.fragment.AbsActivityFragment;
import com.svi.activitytracker.fragment.ActivityDetailfragment;
import com.svi.activitytracker.fragment.ActivityListFragment;
import com.svi.activitytracker.fragment.EditFragment;
import com.svi.activitytracker.lib.Client;
import com.svi.activitytracker.lib.History;
import com.svi.activitytracker.lib.Recording;
import com.svi.activitytracker.lib.Sensors;
import com.svi.activitytracker.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private AbsActivityFragment mActivityListFragment;
    private AbsActivityFragment mActivityDetailsFragment;
    private AbsActivityFragment mActivityEditFragment;
    private AbsActivityFragment mSelectedFragment;

    private Client client;
    private Sensors sensors;
    private Recording recording;
    private History history;
    private Display display = new Display(MainActivity.class.getName()) {
        @Override
        public void show(String msg) {
            log(msg);
        }
    };
    private DatePicker mDatePicker;
    private ListView mHistoryView;

    public Client getClient() {
        return client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display.show("client initialization");
        client = new Client(this,
                new Client.Connection() {
                    @Override
                    public void onConnected() {
                        display.show("client connected");
//                        sensors demo
                        initSensors();
                        display.show("list datasources");
                        sensors.listDatasourcesAndSubscribe();

//                        recording demo
                        recording = new Recording(client.getClient(), new Display(Recording.class.getName()) {
                            @Override
                            public void show(String msg) {
                                log(msg);
                            }
                        });
                        recording.subscribe();
                        recording.listSubscriptions();

//                        history demo
                        /*history = new History(MainActivity.this, client.getClient(), new Display(History.class.getName()) {
                            @Override
                            public void show(String msg) {
                                log(msg);
                            }
                        });*/
                        //history.readWeekBefore(new Date());
                        //Date date = new Date();
                        //history.getHistory(date.getYear() + 1900, date.getMonth(), date.getDate(), mHistoryView);
                        ActivityUtils.setIsLoggedIn(getApplicationContext(), true);
                        changeFragment(Constants.FRAGMENT_ACTIVITY_LIST, null);
                    }
                },
                new Display(Client.class.getName()) {
                    @Override
                    public void show(String msg) {
                        log(msg);
                    }
                });


        getFragmentManager().addOnBackStackChangedListener(getListener());
    }

    public void changeFragment(int fragmentId, Bundle data) {
        AbsActivityFragment fragment = null;
        String tag = null;
        switch (fragmentId) {
            case Constants.FRAGMENT_ACTIVITY_LIST:
                if (mActivityListFragment == null) {
                    mActivityListFragment = new ActivityListFragment();
                }
                mActivityListFragment.updateArguments(data);
                fragment = mActivityListFragment;
                tag = Constants.FRAGMENT_ACTIVITY_LIST_TAG;
                break;
            case Constants.FRAGMENT_ACTIVITY_DETAILS:
                if (mActivityDetailsFragment == null) {
                    mActivityDetailsFragment = new ActivityDetailfragment();
                }
                mActivityDetailsFragment.updateArguments(data);
                fragment = mActivityDetailsFragment;
                tag = Constants.FRAGMENT_ACTIVITY_DETAILS_TAG;
                break;
            case Constants.FRAGMENT_ACTIVITY_EDIT:
                if (mActivityEditFragment == null) {
                    mActivityEditFragment = new EditFragment();
                }
                mActivityEditFragment.updateArguments(data);
                fragment = mActivityEditFragment;
                tag = Constants.FRAGMENT_ACTIVITY_EDIT_TAG;
                break;
        }
        if (fragment == null || mSelectedFragment == fragment) {
            return;
        }
        final FragmentManager fm = getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        mSelectedFragment = fragment;
        if (!fragment.isAdded()) {
            ft.add(R.id.fragment_container, fragment, tag)
                    .show(fragment)
                    .addToBackStack("bs")
                    .commit();
        }
    }

    private AbsActivityFragment getCurrentFragment() {
        return (AbsActivityFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private FragmentManager.OnBackStackChangedListener getListener(){
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener()
        {
            public void onBackStackChanged()
            {
                mSelectedFragment = getCurrentFragment();
            }
        };

        return result;
    }

    @Override
    public void onBackPressed() {
        if (mSelectedFragment == null) {
            return;
        }
        mSelectedFragment = null;
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

//        disable should be called only for revoking authorization in GoogleFit
//        client.revokeAuth();

        display.show("unsubscribe");
        if (sensors != null)
            sensors.unsubscribe();
        if (recording != null)
            recording.unsubscribe();

        display.show("client disconnect");
        if (client != null)
            client.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        display.show("client connect");
//        start connection - required
        client.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        handle OAuth dialog callbacks
        display.show("onActivityResult");
        client.onActivityResult(requestCode, resultCode, data);
    }

    private void initSensors() {
        display.show("init sensors");
        sensors = new Sensors(client.getClient(),
                new Sensors.DatasourcesListener() {
                    @Override
                    public void onDatasourcesListed() {
                        display.show("datasources listed");
                        ArrayList<String> datasources = sensors.getDatasources();
                        for (String d : datasources) {
                            display.show(d);
                        }
                    }
                },
                new Display(Sensors.class.getName()) {
                    @Override
                    public void show(String msg) {
                        log(msg);
                    }
                });
    }

}
