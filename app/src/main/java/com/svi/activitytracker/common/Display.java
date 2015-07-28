package com.svi.activitytracker.common;

import android.util.Log;

public abstract class Display {
    private String tag;

    protected Display(String tag) {
        this.tag = tag;
    }

    public abstract void show(String msg);

    public void log(String msg) {
        Log.d(tag, msg);
    }
}
