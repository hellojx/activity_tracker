package com.svi.activitytracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.svi.activitytracker.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        getSupportActionBar().hide();
    }

    @OnClick(R.id.btnSignUp)
    public void signUp() {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }

    @OnClick(R.id.btnLogin)
    public void login() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
