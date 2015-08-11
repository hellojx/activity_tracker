package com.svi.activitytracker.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
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

import java.util.concurrent.TimeUnit;

public class EditFragment extends AbsActivityFragment {
    private long mStartTime = -1;
    private long mEndTime = -1;
    private int mActivityType = -1;

    private EditText mActivityLat;
    private EditText mActivityLng;
    private EditText mActivitySpeed;
    private EditText mActivityDistance;
    private EditText mActivityStepCount;
    private TextView mActivityTypeTextView;
    private Button mSaveBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_edit_layout, container, false);

        mActivityLat = (EditText) view.findViewById(R.id.activity_lat);
        mActivityLng = (EditText) view.findViewById(R.id.activity_lng);
        mActivitySpeed = (EditText) view.findViewById(R.id.activity_speed);
        mActivityDistance = (EditText) view.findViewById(R.id.activity_distance);
        mActivityStepCount = (EditText) view.findViewById(R.id.step_count);
        mActivityTypeTextView = (TextView) view.findViewById(R.id.activity_type);
        mSaveBtn = (Button) view.findViewById(R.id.btn_save);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivityDistance.getText().length() > 0) {
                    float distance = Float.valueOf(mActivityDistance.getText().toString());
                }

                if (mActivityStepCount.getText().length() > 0) {
                    float stepCnt = Float.valueOf(mActivityStepCount.getText().toString());
                }

                //save speed
                if (mActivitySpeed.getText().length() > 0) {
                    float speed = Float.valueOf(mActivitySpeed.getText().toString());
                    DataSource dataSource = new DataSource.Builder()
                            .setAppPackageName(getActivity())
                            .setDataType(DataType.TYPE_SPEED)
                            .setName("speed")
                            .setType(DataSource.TYPE_RAW)
                            .build();
                    DataSet dataSet = DataSet.create(dataSource);

                    DataPoint dataPoint = dataSet.createDataPoint();
                    dataPoint.getValue(Field.FIELD_SPEED).setFloat(speed);
                    dataPoint.setTimeInterval(mStartTime, mStartTime + 100, TimeUnit.MILLISECONDS);
                    dataSet.add(dataPoint);

                    Fitness.HistoryApi.insertData(((MainActivity) getActivity()).getClient().getClient(), dataSet);
                }

                //save location
                if (mActivityLat.getText().length() > 0 && mActivityLng.getText().length() > 0) {
                    float lat = Float.valueOf(mActivityLat.getText().toString());
                    float lng = Float.valueOf(mActivityLng.getText().toString());
                    DataSource dataSource = new DataSource.Builder()
                            .setAppPackageName(getActivity())
                            .setDataType(DataType.TYPE_LOCATION_SAMPLE)
                            .setName("location")
                            .setType(DataSource.TYPE_RAW)
                            .build();
                    DataSet dataSet = DataSet.create(dataSource);

                    DataPoint dataPoint = dataSet.createDataPoint();
                    dataPoint.getValue(Field.FIELD_LATITUDE).setFloat(lat);
                    dataPoint.getValue(Field.FIELD_LONGITUDE).setFloat(lng);
                    dataPoint.getValue(Field.FIELD_ACCURACY).setFloat(1);
                    dataPoint.getValue(Field.FIELD_ALTITUDE).setFloat(0);
                    dataPoint.setTimeInterval(mStartTime, mEndTime, TimeUnit.MILLISECONDS);
                    dataSet.add(dataPoint);
                    Fitness.HistoryApi.insertData(((MainActivity) getActivity()).getClient().getClient(), dataSet);
                }

            }
        });

        mActivityTypeTextView.setText(Utils.getActivityName(mActivityType));

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


    public void describeDataPoint(DataPoint dp) {
        if (dp.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
            if (mActivityType == Constants.ACTIVITY_TYPE_WALKING) {
                mActivityStepCount.setText(dp.getValue(Field.FIELD_STEPS).toString());
            }
        } else if (dp.getDataType().equals(DataType.TYPE_DISTANCE_DELTA)) {
            float distance = Float.valueOf(dp.getValue(Field.FIELD_DISTANCE).toString()) / 1000;
            mActivityDistance.setText(distance + " km");
        } else if (dp.getDataType().equals(DataType.TYPE_LOCATION_SAMPLE)) {
            mActivityLat.setText(dp.getValue(Field.FIELD_LATITUDE).toString());
            mActivityLng.setText(dp.getValue(Field.FIELD_LONGITUDE).toString());
        } else if (dp.getDataType().equals(DataType.TYPE_SPEED)) {
            String speed = dp.getValue(Field.FIELD_SPEED).toString();
            mActivitySpeed.setText(speed + "m/s");
        }
    }

    public void updateArguments(Bundle data) {
        mStartTime = data.getLong("startTime");
        mEndTime = data.getLong("endTime");
        mActivityType = data.getInt("activityType");
    }
}
