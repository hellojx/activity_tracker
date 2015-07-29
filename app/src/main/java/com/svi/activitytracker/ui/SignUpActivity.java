package com.svi.activitytracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.svi.activitytracker.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.signUpEmailText)
    EditText emailText;
    @Bind(R.id.signUpPasswordText)
    EditText passwordText;
    @Bind(R.id.signUpConfirmPasswordText)
    EditText confirmPasswordText;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        getSupportActionBar().hide();
    }


    @OnClick(R.id.startButton)
    public void startButton(){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        Log.d(TAG, "email: " + email + " password: " + password);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @OnClick(R.id.alreadyTrackerText)
    public void alreadyTracker(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}
