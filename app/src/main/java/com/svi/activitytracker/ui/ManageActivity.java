package com.svi.activitytracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.svi.activitytracker.R;
import com.svi.activitytracker.utils.ActivityUtils;
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
        if(ActivityUtils.getIsWalking(getApplicationContext())){
            activityWalking.setImageResource(R.drawable.walking);
        } else{
            activityWalking.setImageResource(R.drawable.walking_not_selected);
        }

        if(ActivityUtils.getIsDriving(getApplicationContext())){
            activityDriving.setImageResource(R.drawable.driving_a_car);
        } else {
            activityDriving.setImageResource(R.drawable.driving_a_car_not_selected);
        }

        if(ActivityUtils.getIsRidingBus(getApplicationContext())){
            activityRidingBus.setImageResource(R.drawable.riding_a_bus);
        } else {
            activityRidingBus.setImageResource(R.drawable.riding_a_bus_not_selected);
        }

        if(ActivityUtils.getIsCycling(getApplicationContext())){
            activityCycling.setImageResource(R.drawable.cycling);
        } else {
            activityCycling.setImageResource(R.drawable.cycling_not_selected);
        }

        if(ActivityUtils.getIsRunning(getApplicationContext())) {
            activityRunning.setImageResource(R.drawable.running);
        } else {
            activityRunning.setImageResource(R.drawable.running_not_selected);
        }

        if(ActivityUtils.getIsSwimming(getApplicationContext())){
            activitySwimming.setImageResource(R.drawable.swimming);
        } else {
            activitySwimming.setImageResource(R.drawable.swimming_not_selected);
        }

        if(ActivityUtils.getIsBasketball(getApplicationContext())){
            activityBasketball.setImageResource(R.drawable.basketball);
        } else {
            activityBasketball.setImageResource(R.drawable.basketball_not_selected);
        }

        if(ActivityUtils.getIsRidingTrain(getApplicationContext())) {
            activityRidingTrain.setImageResource(R.drawable.riding_a_train);
        } else {
            activityRidingTrain.setImageResource(R.drawable.riding_a_train_not_selected);
        }
    }


    @Override
    public void onClick(View v){
        int drawable = 0;
        switch (v.getId()){
            case R.id.activityWalking:
                drawable = R.drawable.walking;
                if(activityWalking.getResourceId() == drawable){ // deactivate walking
                    activityWalking.setImageResource(R.drawable.walking_not_selected);
                    ActivityUtils.setIsWalking(getApplicationContext(), false);
                } else { // activate walking
                    activityWalking.setImageResource(R.drawable.walking);
                    ActivityUtils.setIsWalking(getApplicationContext(), true);
                }
                break;
            case R.id.activityDriving:
                drawable = R.drawable.driving_a_car;
                if(activityDriving.getResourceId() == drawable){ // deactivate driving
                    activityDriving.setImageResource(R.drawable.driving_a_car_not_selected);
                    ActivityUtils.setIsDriving(getApplicationContext(), false);
                } else { // activate driving
                    activityDriving.setImageResource(R.drawable.driving_a_car);
                    ActivityUtils.setIsDriving(getApplicationContext(), true);
                }
                break;
            case R.id.activityRidingBus:
                drawable = R.drawable.riding_a_bus;
                if(activityRidingBus.getResourceId() == drawable){ // deactivate riding bus
                    activityRidingBus.setImageResource(R.drawable.riding_a_bus_not_selected);
                    ActivityUtils.setIsRidingBus(getApplicationContext(), false);
                } else { // activate riding bus
                    activityRidingBus.setImageResource(R.drawable.riding_a_bus);
                    ActivityUtils.setIsRidingBus(getApplicationContext(), true);
                }
                break;
            case R.id.activityCycling:
                drawable = R.drawable.cycling;
                if(activityCycling.getResourceId() == drawable){ // deactivate cycling
                    activityCycling.setImageResource(R.drawable.cycling_not_selected);
                    ActivityUtils.setIsCycling(getApplicationContext(), false);
                } else { // activate cycling
                    activityCycling.setImageResource(R.drawable.cycling);
                    ActivityUtils.setIsCycling(getApplicationContext(), true);
                }
                break;
            case R.id.activityRunning:
                drawable = R.drawable.running;
                if(activityRunning.getResourceId() == drawable){ // deactivate running
                    activityRunning.setImageResource(R.drawable.running_not_selected);
                    ActivityUtils.setIsRunning(getApplicationContext(), false);
                } else { // activate running
                    activityRunning.setImageResource(R.drawable.running);
                    ActivityUtils.setIsRunning(getApplicationContext(), true);
                }
                break;
            case R.id.activitySwimming:
                drawable = R.drawable.swimming;
                if(activitySwimming.getResourceId() == drawable){ // deactivate swimming
                    activitySwimming.setImageResource(R.drawable.swimming_not_selected);
                    ActivityUtils.setIsSwimming(getApplicationContext(), false);
                } else { // activate swimming
                    activitySwimming.setImageResource(R.drawable.swimming);
                    ActivityUtils.setIsSwimming(getApplicationContext(), true);
                }
                break;
            case R.id.activityBasketball:
                drawable = R.drawable.basketball;
                if(activityBasketball.getResourceId() == drawable){ // deactivate basketball
                    activityBasketball.setImageResource(R.drawable.basketball_not_selected);
                    ActivityUtils.setIsBasketball(getApplicationContext(), false);
                } else { // activate basketball
                    activityBasketball.setImageResource(R.drawable.basketball);
                    ActivityUtils.setIsBasketball(getApplicationContext(), true);
                }
                break;
            case R.id.activityRidingTrain:
                drawable = R.drawable.riding_a_train;
                if(activityRidingTrain.getResourceId() == drawable){ // deactivate riding train
                    activityRidingTrain.setImageResource(R.drawable.riding_a_train_not_selected);
                    ActivityUtils.setIsRidingTrain(getApplicationContext(), false);
                } else { // activate riding train
                    activityRidingTrain.setImageResource(R.drawable.riding_a_train);
                    ActivityUtils.setIsRidingTrain(getApplicationContext(), true);
                }
                break;
        }
    }
}
