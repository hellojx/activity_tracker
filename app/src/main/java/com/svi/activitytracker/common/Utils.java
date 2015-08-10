package com.svi.activitytracker.common;


import com.svi.activitytracker.R;

public class Utils {

    public static int getActivityIcon(int activityType) {

        int iconId = -1;
        switch (activityType) {
            case Constants.ACTIVITY_TYPE_WALKING:
                iconId = R.drawable.walking;
                break;
            case Constants.ACTIVITY_TYPE_IN_VEHICLE:
                iconId = R.drawable.riding_a_bus;
                break;
            case Constants.ACTIVITY_TYPE_BIKING:
                iconId = R.drawable.cycling;
                break;
        }
        return iconId;
    }
}
