package com.svi.activitytracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.ListView;

import com.svi.activitytracker.FitPagerAdapter;
import com.svi.activitytracker.ItemFragment;
import com.svi.activitytracker.R;
import com.svi.activitytracker.common.Constants;
import com.svi.activitytracker.common.Display;
import com.svi.activitytracker.common.InMemoryLog;
import com.svi.activitytracker.lib.Client;
import com.svi.activitytracker.lib.History;
import com.svi.activitytracker.lib.Recording;
import com.svi.activitytracker.lib.Sensors;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ItemFragment.LogProvider {

    private static final String TAG = MainActivity.class.getName();

    private ViewPager viewPager;
    private FitPagerAdapter pagerAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatePicker = (DatePicker) findViewById(R.id.date_to_show);
        mHistoryView = (ListView) findViewById(R.id.history_view);
        Date date = new Date();
        mDatePicker.init(date.getYear() + 1900, date.getMonth(), date.getDate(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (history != null) {
                    history.getHistory(year, monthOfYear, dayOfMonth, mHistoryView);
                }
            }
        });

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        pagerAdapter = new FitPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        display.show("client initialization");
        client = new Client(this,
                new Client.Connection() {
                    @Override
                    public void onConnected() {
                        display.show("client connected");
//                we can call specific api only after GoogleApiClient connection succeeded

//                        sensors demo
                        initSensors();
                        display.show("list datasources");
                        sensors.listDatasourcesAndSubscribe();

//                        recording demo
                        pagerAdapter.getItem(FitPagerAdapter.FragmentIndex.RECORDING);
                        recording = new Recording(client.getClient(), new Display(Recording.class.getName()) {
                            @Override
                            public void show(String msg) {
                                log(msg);

                                add(FitPagerAdapter.FragmentIndex.RECORDING, msg);
//                                InMemoryLog.getInstance().add(FitPagerAdapter.FragmentIndex.RECORDING, msg);
                            }
                        });
                        recording.subscribe();
                        recording.listSubscriptions();

//                        history demo
                        history = new History(MainActivity.this, client.getClient(), new Display(History.class.getName()) {
                            @Override
                            public void show(String msg) {
                                log(msg);

                                add(FitPagerAdapter.FragmentIndex.HISTORY, msg);
//                                InMemoryLog.getInstance().add(FitPagerAdapter.FragmentIndex.HISTORY, msg);
                            }
                        });
                        //history.readWeekBefore(new Date());
                        Date date = new Date();
                        history.getHistory(date.getYear() + 1900, date.getMonth(), date.getDate(), mHistoryView);
                    }
                },
                new Display(Client.class.getName()) {
                    @Override
                    public void show(String msg) {
                        log(msg);
                    }
                });
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
                        for (String d:datasources) {
                            display.show(d);
                        }

                        clear(FitPagerAdapter.FragmentIndex.DATASOURCES);
                        addAll(FitPagerAdapter.FragmentIndex.DATASOURCES, datasources);
                    }
                },
                new Display(Sensors.class.getName()) {
                    @Override
                    public void show(String msg) {
                        log(msg);
                        add(FitPagerAdapter.FragmentIndex.SENSORS, msg);
                    }
                });
    }

    private void add(int fragId, String msg) {
        InMemoryLog.getInstance().add(fragId, msg);
        Intent intent = new Intent();
        intent.setAction(Constants.Action.ADD);
        intent.putExtra(Constants.FRAG_ID, fragId);
        intent.putExtra(Constants.DATA, msg);
        sendBroadcast(intent);
    }

    private void addAll(int fragId, ArrayList<String> data) {
        InMemoryLog.getInstance().addAll(fragId, data);
        Intent intent = new Intent();
        intent.setAction(Constants.Action.ADD_ALL);
        intent.putExtra(Constants.FRAG_ID, fragId);
        intent.putStringArrayListExtra(Constants.DATA, data);
        sendBroadcast(intent);
    }

    private void clear(int fragId) {
        InMemoryLog.getInstance().clear(fragId);
        Intent intent = new Intent();
        intent.setAction(Constants.Action.CLEAR);
        intent.putExtra(Constants.FRAG_ID, fragId);
        sendBroadcast(intent);
    }

    @Override
    public InMemoryLog getLog() {
        return InMemoryLog.getInstance();
    }
}
