package com.svi.activitytracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.svi.activitytracker.constants.Constants;


public class ActivityUtils implements Constants{

    private static SharedPreferences prefs;


    public static void setIsWalking(Context context, boolean value){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(WALKING, value);
        editor.commit();
    }

    public static boolean getIsWalking(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(WALKING, false);
        return value;
    }

    public static void setIsDriving(Context context, boolean value){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(DRIVING, value);
        editor.commit();
    }

    public static boolean getIsDriving(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(DRIVING, false);
        return value;
    }

    public static void setIsRidingBus(Context context, boolean value){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(RIDING_BUS, value);
        editor.commit();
    }

    public static boolean getIsRidingBus(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(RIDING_BUS, false);
        return value;
    }

    public static void setIsCycling(Context context, boolean value){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(CYCLING, value);
        editor.commit();
    }

    public static boolean getIsCycling(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(CYCLING, false);
        return value;
    }

    public static void setIsRunning(Context context, boolean value){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(RUNNING, value);
        editor.commit();
    }

    public static boolean getIsRunning(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(RUNNING, false);
        return value;
    }

    public static void setIsSwimming(Context context, boolean value){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SWIMMING, value);
        editor.commit();
    }

    public static boolean getIsSwimming(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(SWIMMING, false);
        return value;
    }

    public static void setIsBasketball(Context context, boolean value){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(BASKETBALL, value);
        editor.commit();
    }

    public static boolean getIsBasketball(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(BASKETBALL, false);
        return value;
    }

    public static void setIsRidingTrain(Context context, boolean value){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(RIDING_TRAIN, value);
        editor.commit();
    }

    public static boolean getIsRidingTrain(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(RIDING_TRAIN, false);
        return value;
    }


}
