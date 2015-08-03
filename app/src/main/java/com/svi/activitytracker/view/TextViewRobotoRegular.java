package com.svi.activitytracker.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TextViewRobotoRegular extends TextView {

    public static String TAG = TextViewRobotoRegular.class.getSimpleName();

    private static Typeface sTypefaceRobotoRegular;

    public TextViewRobotoRegular(Context context) {
        super(context);
        setStateListAnimator(this);
    }

    public TextViewRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStateListAnimator(this);
    }

    public TextViewRobotoRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setStateListAnimator(this);
    }

    public static void setStateListAnimator(View v) {
        if (android.os.Build.VERSION.SDK_INT >= 21){
            v.setStateListAnimator(null);
        }
    }

    public void setTypeface(Typeface tf, int style) {
        if (isInEditMode()) {
            super.setTypeface(tf, style);
            return;
        }
        Typeface font = getCustomTypeface();
        super.setTypeface(font);
    }

    private Typeface getCustomTypeface() {
        if (sTypefaceRobotoRegular == null) {
            sTypefaceRobotoRegular = getTypeface(getContext(), "fonts/Roboto-Regular.ttf");
            if(sTypefaceRobotoRegular == null){
                return Typeface.DEFAULT;
            }
        }
        return sTypefaceRobotoRegular;
    }

    private static Typeface getTypeface(Context aContext,String aPath){
        try {
            return  Typeface.createFromAsset(aContext.getAssets(), aPath);
        } catch (Exception ex) {
            Log.e(TAG, "createFromAsset could not be established for path: " + aPath, ex);
        }

        return null;
    }
}
