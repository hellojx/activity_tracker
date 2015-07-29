package com.svi.activitytracker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.svi.activitytracker.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.loginEmailText)
    EditText emailText;
    @Bind(R.id.loginPasswordText)
    EditText passwordText;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getSupportActionBar().hide();
    }

    @OnClick(R.id.loginButton)
    public void loginButton(){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        Log.d(TAG, "email: " + email + " password: " + password);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @OnClick(R.id.signUpText)
    public void newTracker(){
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }
}
