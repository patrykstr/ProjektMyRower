package com.example.nerexis.myrower;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReserveActivity extends AppCompatActivity {

    TextView fromData, toData, timeData, dateData;

    Button cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cancelButton = (Button) findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fromData = (TextView) findViewById(R.id.fromData);
        toData = (TextView) findViewById(R.id.toData);
        timeData = (TextView) findViewById(R.id.timeData);
        dateData = (TextView) findViewById(R.id.dateData);


        fromData.setText(getIntent().getStringExtra("from"));
        toData.setText(getIntent().getStringExtra("to"));
        timeData.setText(getIntent().getStringExtra("time"));
        dateData.setText(getIntent().getStringExtra("date"));

    }


}
