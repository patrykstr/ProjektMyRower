package com.example.nerexis.myrower;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginButton = (Button) findViewById(R.id.loginButton);

        Log.d("abc", "Login activity started");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.activity_map);
                loginButton_onClick();
            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loginButton_onClick()
    {
        //setContentView(R.layout.activity_map);
        //Log.d("abc","clicked");

        setResult(1);
        finish();
    }


}
