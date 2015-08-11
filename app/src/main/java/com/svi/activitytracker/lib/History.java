package com.svi.activitytracker.lib;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;

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
import com.svi.activitytracker.common.Constants;
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
    private RecyclerView currentHistoryView;
    private ArrayList<HistoryItem> mHistoryList = new ArrayList<>();
    private Context mContext;
    private HistoryGotListener mHistoryGotListener;

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
                //.aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                //.aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                .aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY)
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
                } /*else if (dataReadResult.getDataSets().size() > 0) {
                    //display.show("dataSet.size(): " + dataReadResult.getDataSets().size());
                    for (DataSet dataSet : dataReadResult.getDataSets()) {
                        display.show("dataType: " + dataSet.getDataType().getName());

                        for (DataPoint dp : dataSet.getDataPoints()) {
                            describeDataPoint(dp, dateFormat);
                        }
                    }
                }*/
                mHistoryGotListener.historyGot(mHistoryList);
            }
        });
    }

    public interface HistoryGotListener {
        void historyGot(ArrayList<HistoryItem> list);
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

            mHistoryList.add(new HistoryItem(null, Constants.ACTIVITY_TYPE_WALKING, dp.getStartTime(TimeUnit.MILLISECONDS),
                    dp.getEndTime(TimeUnit.MILLISECONDS),
                    dp.getEndTime(TimeUnit.MILLISECONDS) - dp.getStartTime(TimeUnit.MILLISECONDS),
                    steps, null));

        } /*else if (dp.getDataType().equals(DataType.TYPE_DISTANCE_DELTA)) {

            mHistoryList.add(new HistoryItem(Constants.ACTIVITY_TYPE_WALKING, dp.getStartTime(TimeUnit.MILLISECONDS),
                    dp.getEndTime(TimeUnit.MILLISECONDS) - dp.getStartTime(TimeUnit.MILLISECONDS),
                    steps, null));

        }*/ else if (dp.getDataType().equals(DataType.AGGREGATE_ACTIVITY_SUMMARY)) {

            msg = "dataPoint: "
                    + "type: " + dp.getDataType().getName() + "\n"
                    + ", range: [" + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + "-" + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + "]\n"
                    + ", fields: [";

            for (Field field : dp.getDataType().getFields()) {
                msg += field.getName() + "=" + dp.getValue(field) + " ";
            }

            msg += "]";


            List<Field> fields = dp.getDataType().getFields();
            int activityType = Integer.valueOf(dp.getValue(fields.get(0)).toString());
            long activityDuration = Long.valueOf(dp.getValue(fields.get(1)).toString());
            switch (activityType) {
                case Constants.ACTIVITY_TYPE_IN_VEHICLE:
                case Constants.ACTIVITY_TYPE_BIKING:
                case Constants.ACTIVITY_TYPE_RUNNING:
                case Constants.ACTIVITY_TYPE_WALKING:
                case Constants.ACTIVITY_TYPE_SWIMMING:
                    mHistoryList.add(new HistoryItem(null, activityType, dp.getStartTime(TimeUnit.MILLISECONDS),
                            dp.getEndTime(TimeUnit.MILLISECONDS), activityDuration, 0, null));
                    break;
                case Constants.ACTIVITY_TYPE_STILL: //not moving activity
                case Constants.ACTIVITY_TYPE_UNKNOWN: //skip unknown activity
                    break;
                default:
                    mHistoryList.add(new HistoryItem(msg, 0, 0, 0, 0, 0, null));
                    break;
            }
        }
        display.show(msg);
    }

    public void getHistory(int year, int monthOfYear, int dayOfMonth, final HistoryGotListener listener) {

        mHistoryGotListener = listener;

        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);

        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = cal.getTimeInMillis();

        read(startTime, endTime);
    }

    public class HistoryItem {
        public String description;
        public int activityType;
        public long startTime;
        public long endTime;
        public long timeInterval;
        public long distance;
        public Location location;
        public HistoryItem(String description, int activityType, long startTime, long endTime, long timeInterval, long distance, Location location) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.timeInterval = timeInterval;
            this.distance = distance;
            this.location = location;
            this.activityType = activityType;
            this.description = description;
        }
    }


}