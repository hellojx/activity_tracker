package com.svi.activitytracker.lib;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.svi.activitytracker.common.Constants;
import com.svi.activitytracker.common.Display;
import com.svi.activitytracker.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class History {
    private GoogleApiClient client;
    private ArrayList<HistoryItem> mHistoryList = new ArrayList<>();
    private HistoryGotListener mHistoryGotListener;

    public History(GoogleApiClient client) {
        this.client = client;
    }

    public void read(long start, long end) {

        DataReadRequest readRequest = new DataReadRequest.Builder()
                //.aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                //.aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                //.aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY)
                //.bucketByActivitySegment(1, TimeUnit.MINUTES)
                .read(DataType.TYPE_ACTIVITY_SEGMENT)
                .setTimeRange(start, end, TimeUnit.MILLISECONDS)
                .build();

        Fitness.HistoryApi.readData(client, readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(DataReadResult dataReadResult) {
                mHistoryList.clear();
                if (dataReadResult.getDataSets().size() > 0) {
                    for (DataSet dataSet : dataReadResult.getDataSets()) {

                        for (DataPoint dp : dataSet.getDataPoints()) {
                            describeDataPoint(dp);
                        }
                    }
                }
                mHistoryGotListener.historyGot(mHistoryList);
            }
        });
    }

    public interface HistoryGotListener {
        void historyGot(ArrayList<HistoryItem> list);
    }

    public void describeDataPoint(DataPoint dp) {

        List<Field> fields = dp.getDataType().getFields();
        int activityType = Integer.valueOf(dp.getValue(fields.get(0)).toString());
        Context context = client.getContext();
        if ((activityType == Constants.ACTIVITY_TYPE_IN_VEHICLE &&
                !(ActivityUtils.getIsDriving(context) || ActivityUtils.getIsRidingBus(context) || ActivityUtils.getIsRidingTrain(context)))
                || (activityType == Constants.ACTIVITY_TYPE_BIKING && !(ActivityUtils.getIsCycling(context)))
                || (activityType == Constants.ACTIVITY_TYPE_RUNNING && !(ActivityUtils.getIsRunning(context)))
                || (activityType == Constants.ACTIVITY_TYPE_WALKING && !(ActivityUtils.getIsWalking(context)))
                || (activityType == Constants.ACTIVITY_TYPE_SWIMMING && !(ActivityUtils.getIsSwimming(context)))
                ) {
            return;
        }

        switch (activityType) {
            case Constants.ACTIVITY_TYPE_IN_VEHICLE:
            case Constants.ACTIVITY_TYPE_BIKING:
            case Constants.ACTIVITY_TYPE_RUNNING:
            case Constants.ACTIVITY_TYPE_WALKING:
            case Constants.ACTIVITY_TYPE_SWIMMING:
                mHistoryList.add(new HistoryItem(null, activityType, dp.getStartTime(TimeUnit.MILLISECONDS),
                        dp.getEndTime(TimeUnit.MILLISECONDS),
                        dp.getEndTime(TimeUnit.MILLISECONDS) - dp.getStartTime(TimeUnit.MILLISECONDS), 0, null));
                break;
            case Constants.ACTIVITY_TYPE_STILL: //not moving activity
            case Constants.ACTIVITY_TYPE_UNKNOWN: //skip unknown activity
                break;
            default:
                mHistoryList.add(new HistoryItem("activityType = " + activityType, 0, 0, 0, 0, 0, null));
                break;
        }
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