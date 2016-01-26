package com.example.nerexis.myrower;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    Intent loginActivityIntent;

    NetworkClient networkClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkClient = new NetworkClient();
        Timer sync = new Timer("sync", true);
        //sync.scheduleAtFixedRate(s=new Sync(mHandler), (long) 0, (long) 60000);
        loginActivityIntent = new Intent(this, LoginActivity.class);

        startActivityForResult(loginActivityIntent, 1);

        Log.d("abc", "app started");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode,
                                   int resultCode, Intent data)
    {
        Log.i("INFO","Request: " + requestCode + ", result: " + resultCode);
        switch(requestCode)
        {
            case 1: {
                if(resultCode==1) {
                    Intent mapIntent = new Intent(this, MapActivity.class);
                    startActivityForResult(mapIntent, 2);
                } else
                {
                    startActivityForResult(loginActivityIntent,1);
                }
            }
            break;
            case 2:
            {
                startActivityForResult(loginActivityIntent,1);
            }
            break;
        }
    }
    @Override
    public void onResume()
    {
        //System.exit(0);
        //finish();
        //startActivityForResult(loginActivityIntent,1);
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
