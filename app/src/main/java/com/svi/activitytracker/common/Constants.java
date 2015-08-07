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

    //fragments
    public static final int FRAGMENT_ACTIVITY_LIST = 1;
    public static final int FRAGMENT_ACTIVITY_DETAILS = 2;

    public static final String FRAGMENT_ACTIVITY_LIST_TAG = "list";
    public static final String FRAGMENT_ACTIVITY_DETAILS_TAG = "details";


}
