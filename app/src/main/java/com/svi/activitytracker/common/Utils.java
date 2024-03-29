package com.svi.activitytracker.common;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.TextView;

import com.svi.activitytracker.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static Drawable getActivityIconDrawable(Context context, int activityType) {
        return context.getResources().getDrawable(getActivityIcon(activityType));
    }

    public static int getActivityIcon(int activityType) {

        int iconId = -1;
        switch (activityType) {
            case Constants.ACTIVITY_TYPE_WALKING:
                iconId = R.drawable.walking;
                break;
            case Constants.ACTIVITY_TYPE_IN_VEHICLE:
                iconId = R.drawable.driving_a_car;
                break;
            case Constants.ACTIVITY_TYPE_BIKING:
                iconId = R.drawable.cycling;
                break;
            case Constants.ACTIVITY_TYPE_RUNNING:
                iconId = R.drawable.running;
                break;
            case Constants.ACTIVITY_TYPE_SWIMMING:
                iconId = R.drawable.swimming;
                break;
        }
        return iconId;
    }

    public static String getActivityNameString(Context context, int activityType) {
        return context.getResources().getString(getActivityName(activityType));
    }

    public static int getActivityName(int activityType) {
        int activityName = R.string.activity_unknown;
        switch (activityType) {
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
            case Constants.ACTIVITY_TYPE_SWIMMING:
                activityName = R.string.activity_swimming;
                break;
        }
        return activityName;
    }

    public static void getPlaceDescription(final Context context, final float lat, final float lng, final TextView place) {

            place.setText("...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    try {
                        final List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
                        if (addresses != null && addresses.size() > 0) {
                            final String locationDescription = addresses.get(0).getLocality();
                            place.post(new Runnable() {
                                @Override
                                public void run() {
                                    place.setText(locationDescription);
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }

    public static String locationStringFromLocation(final Location location) {
        return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + " "
                + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
    }
}
