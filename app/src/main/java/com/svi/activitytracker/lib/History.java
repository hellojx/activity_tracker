package com.svi.activitytracker.lib;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import com.svi.activitytracker.R;
import com.svi.activitytracker.common.Display;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class History {
    private GoogleApiClient client;
    private Display display;
    private ListView currentHistoryView;
    private ArrayList<HistoryItem> mHistoryList = new ArrayList<>();
    private HistoryAdapter adapter = new HistoryAdapter();
    private Context mContext;

    public History(Context context, GoogleApiClient client, Display display) {
        this.client = client;
        this.display = display;
        mContext = context;
    }

    public void readWeekBefore(Date date) {
        Calendar cal = Calendar.getInstance();
//        Date now = new Date();
        cal.setTime(date);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = cal.getTimeInMillis();

        read(startTime, endTime);
    }

    public void read(long start, long end) {

        final DateFormat dateFormat = new SimpleDateFormat("HH:mm");// SimpleDateFormat.getDateInstance();
        //display.show("history reading range: " + dateFormat.format(start) + " - " + dateFormat.format(end));
        display.show("Here are your activities for the day:");

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByActivitySegment(1, TimeUnit.MINUTES)
                .setTimeRange(start, end, TimeUnit.MILLISECONDS)
                .build();

        Fitness.HistoryApi.readData(client, readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(DataReadResult dataReadResult) {
                mHistoryList.clear();

                if (dataReadResult.getBuckets().size() > 0) {
                    /*display.show("DataSet.size(): "
                            + dataReadResult.getBuckets().size());*/
                    for (Bucket bucket : dataReadResult.getBuckets()) {
                        List<DataSet> dataSets = bucket.getDataSets();
                        for (DataSet dataSet : dataSets) {
                            //display.show("dataSet.dataType: " + dataSet.getDataType().getName());

                            for (DataPoint dp : dataSet.getDataPoints()) {
                                describeDataPoint(dp, dateFormat);
                            }
                        }
                    }
                } else if (dataReadResult.getDataSets().size() > 0) {
                    //display.show("dataSet.size(): " + dataReadResult.getDataSets().size());
                    for (DataSet dataSet : dataReadResult.getDataSets()) {
                        display.show("dataType: " + dataSet.getDataType().getName());

                        for (DataPoint dp : dataSet.getDataPoints()) {
                            describeDataPoint(dp, dateFormat);
                        }
                    }
                }

                adapter.setHistoryList(mHistoryList);
                adapter.notifyDataSetChanged();

            }
        });
    }

    public void describeDataPoint(DataPoint dp, DateFormat dateFormat) {
        String msg = "";
        if (dp.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
            int steps = 0;
            for (Field field : dp.getDataType().getFields()) {
                msg += dp.getValue(field) + " " + field.getName();
                steps = dp.getValue(field).asInt();
            }

            String startTime = dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS));
            msg += " walking\nat " + startTime + "\n";

            mHistoryList.add(new HistoryItem(dp.getStartTime(TimeUnit.MILLISECONDS), dp.getEndTime(TimeUnit.MILLISECONDS) - dp.getStartTime(TimeUnit.MILLISECONDS),
                    steps, null));

        } else {
            msg = "dataPoint: "
                    + "type: " + dp.getDataType().getName() + "\n"
                    + ", range: [" + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + "-" + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + "]\n"
                    + ", fields: [";

            for (Field field : dp.getDataType().getFields()) {
                msg += field.getName() + "=" + dp.getValue(field) + " ";
            }

            msg += "]";
        }
        display.show(msg);
    }

    public void getHistory(int year, int monthOfYear, int dayOfMonth, final ListView listview) {

        adapter.notifyDataSetChanged();
        currentHistoryView = listview;
        currentHistoryView.setAdapter(adapter);


        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);

        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = cal.getTimeInMillis();

        read(startTime, endTime);
    }

    private class HistoryItem {
        public long time;
        public long timeInterval;
        public long distance;
        public Location location;
        public HistoryItem(long time, long timeInterval, long distance, Location location) {
            this.time = time;
            this.timeInterval = timeInterval;
            this.distance = distance;
            this.location = location;
        }
    }

    private class HistoryAdapter extends BaseAdapter {


        private ArrayList<HistoryItem> mHistoryList;

        public HistoryAdapter() {
        }

        public void setHistoryList(ArrayList<HistoryItem> historyList) {
            mHistoryList = historyList;
        }

        @Override
        public int getCount() {
            if (mHistoryList == null) {
                return 0;
            }
            return mHistoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.history_item, null);
            }

            HistoryItem item = mHistoryList.get(position);
            final DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String date = dateFormat.format(item.time);

            ((TextView) convertView.findViewById(R.id.activity_type)).setText("Walking");
            ((TextView) convertView.findViewById(R.id.activity_time_interval)).setText(TimeUnit.MILLISECONDS.toMinutes(item.timeInterval) + " minutes");
            ((TextView) convertView.findViewById(R.id.activity_time)).setText(date);
            ((TextView) convertView.findViewById(R.id.activity_distance)).setText(item.distance + " steps");


            return convertView;
        }
    }
}
