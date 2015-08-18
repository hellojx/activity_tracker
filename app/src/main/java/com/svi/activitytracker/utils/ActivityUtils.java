package com.svi.activitytracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.svi.activitytracker.common.Constants;


public class ActivityUtils {

    private static SharedPreferences prefs;

    public static void setIsWalking(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.WALKING, value);
        editor.commit();
    }

    public static boolean getIsWalking(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.WALKING, false);
        return value;
    }

    public static void setIsDriving(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.DRIVING, value);
        editor.commit();
    }

    public static boolean getIsDriving(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.DRIVING, false);
        return value;
    }

    public static void setIsRidingBus(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.RIDING_BUS, value);
        editor.commit();
    }

    public static boolean getIsRidingBus(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.RIDING_BUS, false);
        return value;
    }

    public static void setIsCycling(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.CYCLING, value);
        editor.commit();
    }

    public static boolean getIsCycling(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.CYCLING, false);
        return value;
    }

    public static void setIsRunning(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.RUNNING, value);
        editor.commit();
    }

    public static boolean getIsRunning(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.RUNNING, false);
        return value;
    }

    public static void setIsSwimming(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.SWIMMING, value);
        editor.commit();
    }

    public static boolean getIsSwimming(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.SWIMMING, false);
        return value;
    }

    public static void setIsBasketball(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.BASKETBALL, value);
        editor.commit();
    }

    public static boolean getIsBasketball(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.BASKETBALL, false);
        return value;
    }

    public static void setIsRidingTrain(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.RIDING_TRAIN, value);
        editor.commit();
    }

    public static boolean getIsRidingTrain(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.RIDING_TRAIN, false);
        return value;
    }

    // google account login check
    public static void setIsLoggedIn(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN, value);
        editor.commit();
    }

    public static boolean getIsLoggedIn(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.IS_LOGGED_IN, false);
        return value;
    }

    public static void setIsFirstTimeSeeManageActivities(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.IS_FIRST_TIME, value);
        editor.commit();
    }

    public static boolean getIsFirstTimeSeeManageActivities(Context context){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(Constants.IS_FIRST_TIME, false);
        return value;
    }


    public static void resetPrefsValues(Context context, boolean value){
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN, value);
        editor.putBoolean(Constants.IS_FIRST_TIME, true);
        // reset activities
        editor.putBoolean(Constants.WALKING, value);
        editor.putBoolean(Constants.DRIVING, value);
        editor.putBoolean(Constants.RIDING_BUS, value);
        editor.putBoolean(Constants.CYCLING, value);
        editor.putBoolean(Constants.RUNNING, value);
        editor.putBoolean(Constants.SWIMMING, value);
        editor.putBoolean(Constants.BASKETBALL, value);
        editor.putBoolean(Constants.RIDING_TRAIN, value);
        editor.commit();
    }

}
