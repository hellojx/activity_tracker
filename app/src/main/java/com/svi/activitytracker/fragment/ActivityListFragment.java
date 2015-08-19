package com.svi.activitytracker.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.svi.activitytracker.R;
import com.svi.activitytracker.common.Constants;
import com.svi.activitytracker.common.Utils;
import com.svi.activitytracker.lib.Client;
import com.svi.activitytracker.lib.History;
import com.svi.activitytracker.ui.MainActivity;
import com.svi.activitytracker.ui.ManageActivity;
import com.svi.activitytracker.ui.SplashActivity;
import com.svi.activitytracker.utils.ActivityUtils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ActivityListFragment extends AbsActivityFragment {

    private RecyclerView mActivityList;
    private LinearLayout mCalendarBtn;
    private LinearLayout mEmptyView;
    private TextView mDateText;
    private TextView mMonthText;
    private Toolbar toolbar;
    private ProgressDialog dialog;

    private Client mClient;
    private History mHistory;
    private History.HistoryGotListener mHistoryGotListener;
    private HistoryAdapter mHistoryAdapter;

    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDate;

    private static final String TAG = "ActivityListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list_layout, container, false);

        mActivityList = (RecyclerView) view.findViewById(R.id.activities_list_view);

        mDateText = (TextView) view.findViewById(R.id.date_text);
        mMonthText = (TextView) view.findViewById(R.id.month_text);

        toolbar = (Toolbar) view.findViewById(R.id.list_toolbar);

        Date date = new Date();
        mHistory = new History(((MainActivity) getActivity()).getClient().getClient());

        mEmptyView = (LinearLayout) view.findViewById(R.id.activityEmptyView);
        mHistoryAdapter = new HistoryAdapter();
        mHistoryAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkAdapterIsEmpty();
            }
        });
        mHistoryGotListener = new History.HistoryGotListener() {
            @Override
            public void historyGot(ArrayList<History.HistoryItem> list) {
                mHistoryAdapter.setHistoryList(list);
            }
        };
        mActivityList.setAdapter(mHistoryAdapter);
        checkAdapterIsEmpty();

        mSelectedYear = date.getYear() + 1900;
        mSelectedMonth = date.getMonth();
        mSelectedDate = date.getDate();

        mHistory.getHistory(mSelectedYear, mSelectedMonth, mSelectedDate, mHistoryGotListener);

        mDateText.setText(String.valueOf(date.getDate()));
        mMonthText.setText(new DateFormatSymbols().getMonths()[date.getMonth()]);

        mCalendarBtn = (LinearLayout) view.findViewById(R.id.calendar_btn);
        mCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog tpd = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDateText.setText(String.valueOf(dayOfMonth));
                        mMonthText.setText(new DateFormatSymbols().getMonths()[monthOfYear]);

                        mSelectedYear = year;
                        mSelectedMonth = monthOfYear;
                        mSelectedDate = dayOfMonth;
                        mHistory.getHistory(year, monthOfYear, dayOfMonth, mHistoryGotListener);
                    }
                }, mSelectedYear, mSelectedMonth, mSelectedDate);
                tpd.getDatePicker().setCalendarViewShown(true);
                tpd.getDatePicker().setSpinnersShown(false);
                tpd.show();
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mActivityList.setLayoutManager(layoutManager);

        Cursor cursor = getActivity().getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        if( cursor != null && cursor.moveToFirst() ){
            String ownerName = cursor.getString(cursor.getColumnIndex("display_name"));
            toolbar.setTitle(ownerName);
            cursor.close();
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_manage:
                        startActivity(new Intent(getActivity(), ManageActivity.class));
                        break;
                    case R.id.action_logout:
                        disableGoogleFit();
                        break;
                }
                return true;
            }
        });
        dialog = new ProgressDialog(getActivity());
        checkGoogleFitAPI();
    }

    private void checkGoogleFitAPI(){
        Log.d(TAG, "checkGoogleFitAPI");
        mClient = MainActivity.mClient;
        if(!mClient.getClient().isConnected()){
            Log.e(TAG, "Google Fit wasn't connected");
            return;
        } else {
            Log.d(TAG, "Google Fit is connected");
        }
    }

    private void disableGoogleFit(){
        dialog.setMessage(getActivity().getResources().getString(R.string.text_logging_out));
        dialog.show();

        PendingResult<Status> pendingResult = Fitness.ConfigApi.disableFit(mClient.getClient());
        pendingResult.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    Log.i(TAG, "Google Fit disabled");
                    ActivityUtils.resetPrefsValues(getActivity(), false);
                    Intent intent = new Intent(getActivity(), SplashActivity.class);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "Google Fit wasn't disabled " + status);
                }
                dialog.dismiss();
            }
        });
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ActivityListViewHolder> {

        private ArrayList<History.HistoryItem> mHistoryList;

        public void setHistoryList(ArrayList<History.HistoryItem> historyList) {
            mHistoryList = historyList;
            notifyDataSetChanged();
        }

        @Override
        public ActivityListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
            ActivityListViewHolder holder = new ActivityListViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = mActivityList.getChildAdapterPosition(v);
                    History.HistoryItem item = mHistoryList.get(pos);
                    Bundle b = new Bundle();
                    b.putLong("startTime", item.startTime);
                    b.putLong("endTime", item.endTime);
                    b.putInt("activityType", item.activityType);
                    ((MainActivity) getActivity()).changeFragment(Constants.FRAGMENT_ACTIVITY_DETAILS, b);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ActivityListViewHolder activityListViewHolder, int i) {
            History.HistoryItem item = mHistoryList.get(i);
            if (item.description != null) {
                activityListViewHolder.mActivityType.setText(item.description);
                activityListViewHolder.mActivityTimeInterval.findViewById(R.id.activity_time_interval);
                activityListViewHolder.mActivityTime.findViewById(R.id.activity_time);
                activityListViewHolder.mActivityLocation.findViewById(R.id.activity_location);
                activityListViewHolder.mActivitySpeed.findViewById(R.id.activity_speed);
                activityListViewHolder.mActivityDistance.findViewById(R.id.activity_distance);
            } else {
                final DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String date = dateFormat.format(item.startTime);
                String location = "";
                if(item.location != null) {
                    location = Utils.locationStringFromLocation(item.location);
                } else {
                    location = "No location";
                }
                String speed = "0 km\\h";
                String distance = String.valueOf(item.distance) + " m";

                activityListViewHolder.mActivityIcon.setImageResource(Utils.getActivityIcon(item.activityType));
                activityListViewHolder.mActivityType.setText(Utils.getActivityName(item.activityType));
                activityListViewHolder.mActivityTimeInterval.setText(TimeUnit.MILLISECONDS.toMinutes(item.timeInterval) + " min");
                activityListViewHolder.mActivityTime.setText(date);

                activityListViewHolder.mActivityLocation.setText(location);
                activityListViewHolder.mActivitySpeed.setText(speed);
                activityListViewHolder.mActivityDistance.setText(distance);
            }
        }

        public class ActivityListViewHolder extends RecyclerView.ViewHolder {
            protected ImageView mActivityIcon;
            protected TextView mActivityType;
            protected TextView mActivityTimeInterval;
            protected TextView mActivityTime;
            protected TextView mActivityLocation;
            protected TextView mActivityDistance;
            protected TextView mActivitySpeed;

            public ActivityListViewHolder(View v) {
                super(v);
                mActivityIcon = (ImageView) v.findViewById(R.id.activity_icon);
                mActivityType = (TextView) v.findViewById(R.id.activity_type);
                mActivityTimeInterval = (TextView) v.findViewById(R.id.activity_time_interval);
                mActivityTime = (TextView) v.findViewById(R.id.activity_time);
                mActivityLocation = (TextView) v.findViewById(R.id.activity_location);
                mActivityDistance = (TextView) v.findViewById(R.id.activity_distance);
                mActivitySpeed = (TextView) v.findViewById(R.id.activity_speed);
            }
        }

        @Override
        public int getItemCount() {
            if (mHistoryList == null) {
                return 0;
            }
            return mHistoryList.size();
        }
    }

    private void checkAdapterIsEmpty() {
        if (mHistoryAdapter.getItemCount() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

}
