package com.svi.activitytracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.svi.activitytracker.R;
import com.svi.activitytracker.view.CustomImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.manage_toolbar)
    Toolbar toolbar;
    @Bind(R.id.activityWalking)
    CustomImageView activityWalking;
    @Bind(R.id.activityDriving)
    CustomImageView activityDriving;
    @Bind(R.id.activityRidingBus)
    CustomImageView activityRidingBus;
    @Bind(R.id.activityCycling)
    CustomImageView activityCycling;
    @Bind(R.id.activityRunning)
    CustomImageView activityRunning;
    @Bind(R.id.activitySwimming)
    CustomImageView activitySwimming;
    @Bind(R.id.activityBasketball)
    CustomImageView activityBasketball;
    @Bind(R.id.activityRidingTrain)
    CustomImageView activityRidingTrain;

    private static final String TAG = "ManageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        setImageResources();
    }

    @OnClick(R.id.btnStartTracking)
    public void startTracking() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void setImageResources(){
        activityWalking.setImageResource(R.drawable.walking);
        activityDriving.setImageResource(R.drawable.driving_a_car_not_selected);
        activityRidingBus.setImageResource(R.drawable.riding_a_bus_not_selected);
        activityCycling.setImageResource(R.drawable.cycling_not_selected);
        activityRunning.setImageResource(R.drawable.running_not_selected);
        activitySwimming.setImageResource(R.drawable.swimming_not_selected);
        activityBasketball.setImageResource(R.drawable.basketball_not_selected);
        activityRidingTrain.setImageResource(R.drawable.riding_a_train_not_selected);
    }


    @Override
    public void onClick(View v){
        int drawable = 0;
        switch (v.getId()){
            case R.id.activityWalking:
                drawable = R.drawable.walking;
                if(activityWalking.getResourceId() == drawable){ // deactivate walking
                    activityWalking.setImageResource(R.drawable.walking_not_selected);
                } else { // activate walking
                    activityWalking.setImageResource(R.drawable.walking);
                }
                break;
            case R.id.activityDriving:
                drawable = R.drawable.driving_a_car;
                if(activityDriving.getResourceId() == drawable){ // deactivate driving
                    activityDriving.setImageResource(R.drawable.driving_a_car_not_selected);
                } else { // activate driving
                    activityDriving.setImageResource(R.drawable.driving_a_car);
                }
                break;
            case R.id.activityRidingBus:
                drawable = R.drawable.riding_a_bus;
                if(activityRidingBus.getResourceId() == drawable){ // deactivate riding bus
                    activityRidingBus.setImageResource(R.drawable.riding_a_bus_not_selected);
                } else { // activate riding bus
                    activityRidingBus.setImageResource(R.drawable.riding_a_bus);
                }
                break;
            case R.id.activityCycling:
                drawable = R.drawable.cycling;
                if(activityCycling.getResourceId() == drawable){ // deactivate cycling
                    activityCycling.setImageResource(R.drawable.cycling_not_selected);
                } else { // activate cycling
                    activityCycling.setImageResource(R.drawable.cycling);
                }
                break;
            case R.id.activityRunning:
                drawable = R.drawable.running;
                if(activityRunning.getResourceId() == drawable){ // deactivate running
                    activityRunning.setImageResource(R.drawable.running_not_selected);
                } else { // activate running
                    activityRunning.setImageResource(R.drawable.running);
                }
                break;
            case R.id.activitySwimming:
                drawable = R.drawable.swimming;
                if(activitySwimming.getResourceId() == drawable){ // deactivate swimming
                    activitySwimming.setImageResource(R.drawable.swimming_not_selected);
                } else { // activate swimming
                    activitySwimming.setImageResource(R.drawable.swimming);
                }
                break;
            case R.id.activityBasketball:
                drawable = R.drawable.basketball;
                if(activityBasketball.getResourceId() == drawable){ // deactivate basketball
                    activityBasketball.setImageResource(R.drawable.basketball_not_selected);
                } else { // activate basketball
                    activityBasketball.setImageResource(R.drawable.basketball);
                }
                break;
            case R.id.activityRidingTrain:
                drawable = R.drawable.riding_a_train;
                if(activityRidingTrain.getResourceId() == drawable){ // deactivate riding train
                    activityRidingTrain.setImageResource(R.drawable.riding_a_train_not_selected);
                } else { // activate riding train
                    activityRidingTrain.setImageResource(R.drawable.riding_a_train);
                }
                break;
        }
    }
}
