package com.svi.activitytracker.lib;

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

import com.svi.activitytracker.common.Display;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class History {
    private GoogleApiClient client;
    private Display display;

    public History(GoogleApiClient client, Display display) {
        this.client = client;
        this.display = display;
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
                .bucketByActivitySegment(5, TimeUnit.MINUTES)
                .setTimeRange(start, end, TimeUnit.MILLISECONDS)
                .build();

        Fitness.HistoryApi.readData(client, readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(DataReadResult dataReadResult) {
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

            }
        });
    }

    public void describeDataPoint(DataPoint dp, DateFormat dateFormat) {
        String msg = "";
        if (dp.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
            for (Field field : dp.getDataType().getFields()) {
                msg += dp.getValue(field) + " " + field.getName();
            }

            String startTime = dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS));
            msg += " walking\nat " + startTime + "\n";

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
}
