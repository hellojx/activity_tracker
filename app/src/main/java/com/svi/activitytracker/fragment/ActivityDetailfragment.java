package com.svi.activitytracker.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
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
    private TextView mStepCount;

    private Button dailyLog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_detail_layout, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.activity_details_toolbar);
        toolbar.inflateMenu(R.menu.menu_details);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:

                        Bundle b = new Bundle();
                        b.putLong("startTime", mStartTime);
                        b.putLong("endTime", mEndTime);
                        b.putInt("activityType", mActivityType);
                        ((MainActivity) getActivity()).changeFragment(Constants.FRAGMENT_ACTIVITY_EDIT, b);
                        break;
                }

                return true;
            }
        });

        mDurationValue = (TextView) view.findViewById(R.id.duration_value);
        mDistanceValue = (TextView) view.findViewById(R.id.distance_value);
        mLocationValue = (TextView) view.findViewById(R.id.location_value);
        mSpeedValue = (TextView) view.findViewById(R.id.speed_value);
        mStepCount = (TextView) view.findViewById(R.id.step_count);

        dailyLog = (Button) view.findViewById(R.id.daily_log);

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


        //set speed
        /*DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(getActivity())
                .setDataType(DataType.TYPE_SPEED)
                .setName("speed")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet dataSet = DataSet.create(dataSource);

        DataPoint dataPoint = dataSet.createDataPoint();
        dataPoint.getValue(Field.FIELD_SPEED).setFloat(1);
        dataPoint.setTimeInterval(mStartTime, mStartTime + 100, TimeUnit.MILLISECONDS);
        dataSet.add(dataPoint);

        Fitness.HistoryApi.insertData(((MainActivity) getActivity()).getClient().getClient(), dataSet);*/


        //add location manually
        /*DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(getActivity())
                .setDataType(DataType.TYPE_LOCATION_SAMPLE)
                .setName("location")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet dataSet = DataSet.create(dataSource);

        DataPoint dataPoint = dataSet.createDataPoint();
        dataPoint.getValue(Field.FIELD_LATITUDE).setFloat(50.446214f);
        dataPoint.getValue(Field.FIELD_LONGITUDE).setFloat(30.57805f);
        dataPoint.getValue(Field.FIELD_ACCURACY).setFloat(1);
        dataPoint.getValue(Field.FIELD_ALTITUDE).setFloat(0);
        dataPoint.setTimeInterval(mStartTime, mEndTime, TimeUnit.MILLISECONDS);
        dataSet.add(dataPoint);

        Fitness.HistoryApi.insertData(((MainActivity) getActivity()).getClient().getClient(), dataSet);
*/

        DataReadRequest readRequest = new DataReadRequest.Builder()
                        .read(DataType.TYPE_STEP_COUNT_DELTA)
                        .read(DataType.TYPE_DISTANCE_DELTA)
                        .read(DataType.TYPE_LOCATION_SAMPLE)
                        .read(DataType.TYPE_SPEED)
                        .setTimeRange(mStartTime, mEndTime, TimeUnit.MILLISECONDS)
                        .build();

        Fitness.HistoryApi.readData(((MainActivity) getActivity()).getClient().getClient(), readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(DataReadResult dataReadResult) {

                for (DataSet dataSet : dataReadResult.getDataSets()) {

                    for (DataPoint dp : dataSet.getDataPoints()) {
                        describeDataPoint(dp);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        dailyLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    public void describeDataPoint(DataPoint dp) {
        if (dp.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
            if (mActivityType == Constants.ACTIVITY_TYPE_WALKING) {
                mStepCount.setText(dp.getValue(Field.FIELD_STEPS) + " " + Field.FIELD_STEPS.getName());
                mStepCount.setVisibility(View.VISIBLE);
            }
        } else if (dp.getDataType().equals(DataType.TYPE_DISTANCE_DELTA)) {
            float distance = Float.valueOf(dp.getValue(Field.FIELD_DISTANCE).toString()) / 1000;
            mDistanceValue.setText(distance + " km");
        } else if (dp.getDataType().equals(DataType.TYPE_LOCATION_SAMPLE)) {
            String lat = dp.getValue(Field.FIELD_LATITUDE).toString();
            String lng = dp.getValue(Field.FIELD_LONGITUDE).toString();
            Utils.getPlaceDescription(getActivity(), Float.valueOf(lat), Float.valueOf(lng), mLocationValue);
        } else if (dp.getDataType().equals(DataType.TYPE_SPEED)) {
            String speed = dp.getValue(Field.FIELD_SPEED).toString();
            mSpeedValue.setText(speed + "m/s");
        } else {
            String msg = "";
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
