package com.svi.activitytracker.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.svi.activitytracker.common.Constants;
import com.svi.activitytracker.common.Utils;
import com.svi.activitytracker.ui.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActivityDetailfragment extends AbsActivityFragment {

    private long mStartTime = -1;
    private long mEndTime = -1;
    private int mActivityType = -1;

    private TextView mDurationValue;
    private TextView mDistanceValue;
    private TextView mLocationValue;
    private TextView mSpeedValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_detail_layout, container, false);

        mDurationValue = (TextView) view.findViewById(R.id.duration_value);
        mDistanceValue = (TextView) view.findViewById(R.id.distance_value);
        mLocationValue = (TextView) view.findViewById(R.id.location_value);
        mSpeedValue = (TextView) view.findViewById(R.id.speed_value);

        mDurationValue.setText(TimeUnit.MILLISECONDS.toMinutes(mEndTime - mStartTime) + " minutes");

        int icon = Utils.getActivityIcon(mActivityType);
        ImageView actImage = (ImageView) view.findViewById(R.id.activity_icon);
        actImage.setImageResource(icon);

        TextView actDate = (TextView) view.findViewById(R.id.activity_date);
        TextView actTime = (TextView) view.findViewById(R.id.activity_time);

        final DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        final DateFormat timeFormat = new SimpleDateFormat("h:m a");

        String date = dateFormat.format(mStartTime);
        String time = timeFormat.format(mStartTime);

        actDate.setText(date);
        actTime.setText(time);


        DataReadRequest readRequest = new DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
            .aggregate(DataType.TYPE_LOCATION_SAMPLE, DataType.AGGREGATE_LOCATION_BOUNDING_BOX)
            .aggregate(DataType.TYPE_SPEED, DataType.AGGREGATE_SPEED_SUMMARY)
            .bucketByActivitySegment(1, TimeUnit.MINUTES)
            .setTimeRange(mStartTime, mEndTime, TimeUnit.MILLISECONDS)
            .build();

        Fitness.HistoryApi.readData(((MainActivity) getActivity()).getClient().getClient(), readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(DataReadResult dataReadResult) {

                if (dataReadResult.getBuckets().size() > 0) {
                    for (Bucket bucket : dataReadResult.getBuckets()) {
                        List<DataSet> dataSets = bucket.getDataSets();
                        for (DataSet dataSet : dataSets) {

                            for (DataPoint dp : dataSet.getDataPoints()) {
                                describeDataPoint(dp);
                            }
                        }
                    }
                } else if (dataReadResult.getDataSets().size() > 0) {
                    for (DataSet dataSet : dataReadResult.getDataSets()) {

                        for (DataPoint dp : dataSet.getDataPoints()) {
                            describeDataPoint(dp);
                        }
                    }
                }
            }
        });


        return view;
    }

    public void describeDataPoint(DataPoint dp) {
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String msg = "";
        if (dp.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
            Field field = dp.getDataType().getFields().get(0);
            if (mActivityType == Constants.ACTIVITY_TYPE_WALKING) {
                mDistanceValue.setText(dp.getValue(field) + " " + field.getName());
            }
        } else {
            for (Field field : dp.getDataType().getFields()) {
                msg += dp.getValue(field) + " " + field.getName();
            }
            Log.e("detail", msg);
        }
    }

    public void updateArguments(Bundle data) {
        mStartTime = data.getLong("startTime");
        mEndTime = data.getLong("endTime");
        mActivityType = data.getInt("activityType");
    }
}
