package com.svi.activitytracker.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.svi.activitytracker.R;
import com.svi.activitytracker.common.Constants;
import com.svi.activitytracker.common.Utils;
import com.svi.activitytracker.ui.MainActivity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActivityDetailfragment extends AbsActivityFragment
        implements OnMapReadyCallback {

    private static final String TAG = ActivityDetailfragment.class.getSimpleName();

    private static String MAP_FRAGMENT_TAG = "map_fragment";

    private GoogleMap mGoogleMap;

    private long mStartTime = -1;
    private long mEndTime = -1;
    private int mActivityType = -1;

    private TextView mDurationValue;
    private TextView mDistanceValue;
    private TextView mLocationValue;
    private TextView mSpeedValue;
    private TextView mStepCount;

    private Button dailyLog;

    private ArrayList<LatLng> mLocationList = new ArrayList<>();

    private DataSet mAcyivitySummarySet;
    private GoogleApiClient mClient;

    private ActivitySelectorDialogFragment mActivitySelectorDialogFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_detail_layout, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        mClient = ((MainActivity) activity).getClient().getClient();
        FragmentManager fm = activity.getFragmentManager();
        Fragment fragment = fm.findFragmentByTag(MAP_FRAGMENT_TAG);
        if (fragment != null) {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.remove(fragment);
        }

        final RelativeLayout activityMap = (RelativeLayout) view.findViewById(R.id.activity_map);
        activityMap.setVisibility(View.INVISIBLE);

        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.map_container, mapFragment, MAP_FRAGMENT_TAG).commit();

        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.activity_details_toolbar);
        Cursor cursor = getActivity().getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        if( cursor != null && cursor.moveToFirst() ){
            String ownerName = cursor.getString(cursor.getColumnIndex("display_name"));
            toolbar.setTitle(ownerName);
            cursor.close();
        }
        /*toolbar.inflateMenu(R.menu.menu_details);
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
        });*/

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
                        .read(DataType.TYPE_ACTIVITY_SEGMENT)
                        .setTimeRange(mStartTime, mEndTime, TimeUnit.MILLISECONDS)
                        .build();

        Fitness.HistoryApi.readData(((MainActivity) getActivity()).getClient().getClient(), readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(DataReadResult dataReadResult) {


                for (DataSet dataSet : dataReadResult.getDataSets()) {

                    if (dataSet.getDataType().equals(DataType.TYPE_ACTIVITY_SEGMENT)) {

                        mAcyivitySummarySet = dataSet;
                    } else {
                        for (DataPoint dp : dataSet.getDataPoints()) {
                            describeDataPoint(dp);
                        }
                    }
                }

                if (mLocationList.size() > 0) {
                    activityMap.setVisibility(View.VISIBLE);
                    if (mGoogleMap != null) {
                        addMarkers();
                    }
                }
            }
        });

        mActivitySelectorDialogFragment = new ActivitySelectorDialogFragment();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 100:

                if (resultCode == Activity.RESULT_OK) {

                    int newActType = data.getIntExtra("activity_type", -1);
                    editActivityType(newActType);
                } else if (resultCode == Activity.RESULT_CANCELED){
                }

                break;
        }
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
            mLocationList.add(new LatLng(Double.valueOf(lat), Double.valueOf(lng)));
            Utils.getPlaceDescription(getActivity(), Float.valueOf(lat), Float.valueOf(lng), mLocationValue);
        } else if (dp.getDataType().equals(DataType.TYPE_SPEED)) {
            Double speed = Double.valueOf(dp.getValue(Field.FIELD_SPEED).toString());
            mSpeedValue.setText(roundTwoDecimals(speed) + "km/h");
        } else {
            String msg = "";
            for (Field field : dp.getDataType().getFields()) {
                msg += dp.getValue(field) + " " + field.getName();
            }
            Log.e("detail", msg);
        }
    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public void updateArguments(Bundle data) {
        mStartTime = data.getLong("startTime");
        mEndTime = data.getLong("endTime");
        mActivityType = data.getInt("activityType");
    }

    private void addMarkers() {
        if (mGoogleMap == null || mLocationList.size() == 0) {
            return;
        }

        if (mLocationList.size() <= 2) {
            for (LatLng latLng : mLocationList) {
                MarkerOptions options = new MarkerOptions().position(latLng);
                mGoogleMap.addMarker(options);
            }
        } else {
            PolylineOptions mPolylineOptions = new PolylineOptions();
            mPolylineOptions.color(Color.RED);
            mPolylineOptions.width(3);
            mPolylineOptions.addAll(mLocationList);
            mGoogleMap.addPolyline(mPolylineOptions);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : mLocationList) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
        mGoogleMap.moveCamera(cu);

        mLocationList.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        addMarkers();
    }

    private void editActivityType(final int activityType) {
        if (mAcyivitySummarySet == null || mActivityType == activityType) {
            return;
        }
        mActivityType = activityType;

        DataDeleteRequest request = new DataDeleteRequest.Builder()
                .setTimeInterval(mStartTime, mEndTime, TimeUnit.MILLISECONDS)
                .addDataType(DataType.TYPE_ACTIVITY_SEGMENT)
                .build();

        Fitness.HistoryApi.deleteData(mClient, request)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {

                            /*DataSource dataSource = new DataSource.Builder()
                                    .setAppPackageName(getActivity())
                                    .setDataType(DataType.TYPE_ACTIVITY_SEGMENT)
                                    .setName("segment")
                                    .setType(DataSource.TYPE_RAW)
                                    .build();
                            DataSet dataSet = DataSet.create(dataSource);

                            for (DataPoint dp : mAcyivitySummarySet.getDataPoints()) {

                                List<Field> fields = dp.getDataType().getFields();

                                DataPoint dataPoint = dataSet.createDataPoint();
                                dataPoint.getValue(fields.get(0)).setInt(activityType);
                                //dataPoint.getValue(fields.get(1)).setInt(Integer.valueOf(dp.getValue(fields.get(1)).toString()));
                                dataPoint.setTimeInterval(mStartTime, mEndTime, TimeUnit.MILLISECONDS);

                                dataSet.add(dataPoint);
                            }
                            Fitness.HistoryApi.insertData(((MainActivity) getActivity()).getClient().getClient(), dataSet);*/

                        } else {
                            Log.i(TAG, "Failed to delete today's step count data");
                        }
                    }
                });
    }
}
