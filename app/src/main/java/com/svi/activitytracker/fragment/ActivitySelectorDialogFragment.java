package com.svi.activitytracker.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.svi.activitytracker.R;
import com.svi.activitytracker.common.Constants;
import com.svi.activitytracker.common.Utils;

import java.util.ArrayList;

public class ActivitySelectorDialogFragment extends DialogFragment {

    private Spinner mActivitiesSpinner;
    private ArrayList<Integer> mActivityIdList;
    private int mActivityType = -1;
    private Button mDoneBtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setTitle("Edit Activity");
        View v = inflater.inflate(R.layout.activity_selector, null);

        mDoneBtn = (Button) v.findViewById(R.id.done_btn);
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mActivitiesSpinner.getSelectedItemPosition();
                if (mActivityType == mActivityIdList.get(position)) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("activity_type", mActivityIdList.get(position));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        mActivityIdList = new ArrayList<>();
        mActivityIdList.add(Constants.ACTIVITY_TYPE_WALKING);
        mActivityIdList.add(Constants.ACTIVITY_TYPE_IN_VEHICLE);
        mActivityIdList.add(Constants.ACTIVITY_TYPE_BIKING);
        mActivityIdList.add(Constants.ACTIVITY_TYPE_RUNNING);
        mActivityIdList.add(Constants.ACTIVITY_TYPE_SWIMMING);

        mActivitiesSpinner = (Spinner) v.findViewById(R.id.activity_spinner);

        mActivitiesSpinner.setAdapter(new ActivitySpinnerAdapter(mActivityIdList));

        return v;
    }

    public void setActivityType(int activityType) {
        mActivityType = activityType;
    }

    private class ActivitySpinnerAdapter implements SpinnerAdapter {
        private ArrayList<Integer> mActivityIdList;

        public ActivitySpinnerAdapter(ArrayList<Integer> activityIdList) {
            mActivityIdList = activityIdList;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return mActivityIdList.size();
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
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.spinner_item, null);
            }

            ((TextView) convertView.findViewById(R.id.activity_title)).setText(
                    Utils.getActivityNameString(getActivity(), mActivityIdList.get(position)));
            ((ImageView) convertView.findViewById(R.id.activity_icon)).setImageDrawable(
                    Utils.getActivityIconDrawable(getActivity(), mActivityIdList.get(position))
            );


            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
