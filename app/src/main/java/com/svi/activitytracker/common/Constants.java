package com.svi.activitytracker.common;

public class Constants {
    public static final String FRAG_ID = "FRAG_ID";
    public static final int NO_ID = -1;

    public static class Action {
        public static final String ADD = "ADD";
        public static final String ADD_ALL = "ADD_ALL";
        public static final String CLEAR = "CLEAR";
    }
    public static final String DATA = "DATA";

    //activity kinds 
    public static final int ACTIVITY_TYPE_WALKING = 7;
    public static final int ACTIVITY_TYPE_IN_VEHICLE = 0;
    public static final int ACTIVITY_TYPE_STILL = 3;
    public static final int ACTIVITY_TYPE_UNKNOWN = 4;
    public static final int ACTIVITY_TYPE_BIKING = 1;
    public static final int ACTIVITY_TYPE_RUNNING = 8;
    public static final int ACTIVITY_TYPE_SWIMMING = 82;

    //fragments
    public static final int FRAGMENT_ACTIVITY_LIST = 1;
    public static final int FRAGMENT_ACTIVITY_DETAILS = 2;
    public static final int FRAGMENT_ACTIVITY_EDIT = 3;

    public static final String FRAGMENT_ACTIVITY_LIST_TAG = "list";
    public static final String FRAGMENT_ACTIVITY_DETAILS_TAG = "details";
    public static final String FRAGMENT_ACTIVITY_EDIT_TAG = "edit";

    public static final String PREFS_NAME = "activity_prefs";

    public static final String WALKING = "walking";
    public static final String DRIVING = "driving";
    public static final String RIDING_BUS = "riding_bus";
    public static final String CYCLING = "cycling";
    public static final String RUNNING = "running";
    public static final String SWIMMING = "swimming";
    public static final String BASKETBALL = "basketball";
    public static final String RIDING_TRAIN = "riding_train";

    public static final String IS_LOGGED_IN = "is_logged_in";
    public static final String IS_FIRST_TIME = "is_first_time";
}
