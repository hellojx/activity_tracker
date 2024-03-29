package com.svi.activitytracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.svi.activitytracker.R;
import com.svi.activitytracker.utils.ActivityUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if(ActivityUtils.getIsLoggedIn(getApplicationContext())){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            ActivityUtils.setIsFirstTimeSeeManageActivities(getApplicationContext(), true);
        }

    }

    @OnClick(R.id.btnContinue)
    public void continueWith() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}
