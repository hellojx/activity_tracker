package com.svi.activitytracker.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.svi.activitytracker.R;
import com.svi.activitytracker.common.Constants;
import com.svi.activitytracker.common.Display;
import com.svi.activitytracker.lib.History;
import com.svi.activitytracker.ui.MainActivity;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ActivityListFragment extends AbsActivityFragment {

    private RecyclerView mActivityList;
    private LinearLayout mCalendarBtn;
    private TextView mDateText;
    private TextView mMonthText;

    private History mHistory;
    private History.HistoryGotListener mHistoryGotListener;
    private HistoryAdapter mHistoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list_layout, container, false);

        mActivityList = (RecyclerView) view.findViewById(R.id.activities_list_view);

        mDateText = (TextView) view.findViewById(R.id.date_text);
        mMonthText = (TextView) view.findViewById(R.id.month_text);

        Date date = new Date();
        mHistory = new History(getActivity(), ((MainActivity) getActivity()).getClient().getClient(), new Display(History.class.getName()) {
            @Override
            public void show(String msg) {
                log(msg);
            }
        });


        mHistoryAdapter = new HistoryAdapter();
        mHistoryGotListener = new History.HistoryGotListener() {
            @Override
            public void historyGot(ArrayList<History.HistoryItem> list) {
                mHistoryAdapter.setHistoryList(list);
            }
        };
        mActivityList.setAdapter(mHistoryAdapter);

        mHistory.getHistory(date.getYear() + 1900, date.getMonth(), date.getDate(), mHistoryGotListener);

        mDateText.setText(String.valueOf(date.getDate()));
        mMonthText.setText(new DateFormatSymbols().getMonths()[date.getMonth()]);


        mCalendarBtn = (LinearLayout) view.findViewById(R.id.calendar_btn);
        mCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DatePickerDialog tpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDateText.setText(String.valueOf(dayOfMonth));
                        mMonthText.setText(new DateFormatSymbols().getMonths()[monthOfYear]);

                        mHistory.getHistory(year, monthOfYear, dayOfMonth, mHistoryGotListener);
                    }
                }, date.getYear() + 1900, date.getMonth(), date.getDate());
                tpd.show();
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mActivityList.setLayoutManager(layoutManager);
        return view;
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
                activityListViewHolder.mActivityTimeInerval.findViewById(R.id.activity_time_interval);
                activityListViewHolder.mActivityTime.findViewById(R.id.activity_time);
                activityListViewHolder.mActivityDistance.findViewById(R.id.activity_distance);
            } else {
                final DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String date = dateFormat.format(item.startTime);

                int activityName = R.string.activity_unknown;
                switch (item.activityType) {
                    case Constants.ACTIVITY_TYPE_WALKING:
                        activityName = R.string.activity_walking;
                        break;
                    case Constants.ACTIVITY_TYPE_IN_VEHICLE:
                        activityName = R.string.activity_in_vehicle;
                        break;
                    case Constants.ACTIVITY_TYPE_BIKING:
                        activityName = R.string.activity_biking;
                        break;
                    case Constants.ACTIVITY_TYPE_RUNNING:
                        activityName = R.string.activity_running;
                        break;
                }


                activityListViewHolder.mActivityType.setText(activityName);
                activityListViewHolder.mActivityTimeInerval.setText(TimeUnit.MILLISECONDS.toMinutes(item.timeInterval) + " minutes");
                activityListViewHolder.mActivityTime.setText(date);
                activityListViewHolder.mActivityDistance.setText(item.distance == 0 ? "" : item.distance + " steps");
            }
        }

        public class ActivityListViewHolder extends RecyclerView.ViewHolder {
            protected TextView mActivityType;
            protected TextView mActivityTimeInerval;
            protected TextView mActivityTime;
            protected TextView mActivityDistance;

            public ActivityListViewHolder(View v) {
                super(v);
                mActivityType = (TextView) v.findViewById(R.id.activity_type);
                mActivityTimeInerval = (TextView) v.findViewById(R.id.activity_time_interval);
                mActivityTime = (TextView) v.findViewById(R.id.activity_time);
                mActivityDistance = (TextView) v.findViewById(R.id.activity_distance);
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
}
