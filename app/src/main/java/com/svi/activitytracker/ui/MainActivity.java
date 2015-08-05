package com.svi.activitytracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.svi.activitytracker.R;
import com.svi.activitytracker.common.Display;
import com.svi.activitytracker.lib.Client;
import com.svi.activitytracker.lib.History;
import com.svi.activitytracker.lib.Recording;
import com.svi.activitytracker.lib.Sensors;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

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
                        history = new History(MainActivity.this, client.getClient(), new Display(History.class.getName()) {
                            @Override
                            public void show(String msg) {
                                log(msg);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_manage:
                startActivity(new Intent(getApplicationContext(), ManageActivity.class));
                return true;
            case R.id.action_logout:
                Toast.makeText(getApplicationContext(), "Logout pressed...", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
