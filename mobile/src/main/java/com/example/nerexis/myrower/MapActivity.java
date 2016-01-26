package com.example.nerexis.myrower;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

import com.esotericsoftware.kryonet.Client;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

public class MapActivity extends AppCompatActivity {

    Button reserveButton;
    Button logoutButton;
    Button settingsButton;
    Button historyButton;

    MultiAutoCompleteTextView fromStationTextBox;
    MultiAutoCompleteTextView toStationTextBox;

    public static MapActivity currentInstance;

    Sync s;
    public GoogleMap mMap;
    ArrayAdapter<String> adapter;
    Polyline startStationRouteP,betweenStationsRouteP,endStationRouteP;
    PolylineOptions startStationRoute,betweenStationsRoute,endStationRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Timer sync = new Timer("sync", true);
        sync.scheduleAtFixedRate(s=new Sync(mHandler), (long) 0, (long) 60000);

        currentInstance = this;

        reserveButton = (Button) findViewById(R.id.reserveButton);

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserveButton_click();

            }
        });

        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutButton_click();
            }
        });

        settingsButton = (Button) findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settingsButton_click();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsButton_click();
            }
        });

        historyButton = (Button) findViewById(R.id.historyButton);

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyButton_click();
            }
        });

        fromStationTextBox = (MultiAutoCompleteTextView) findViewById(R.id.fromStationSelector);
        toStationTextBox = (MultiAutoCompleteTextView) findViewById(R.id.toStationSelector);
    }

    public Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {

            startStationRouteP=mMap.addPolyline(startStationRoute);
            betweenStationsRouteP=mMap.addPolyline(betweenStationsRoute);
            endStationRouteP=mMap.addPolyline(endStationRoute);
            System.out.println(startStationRouteP.toString());
        }
    };

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            String[] stationName=new String[s.stations.size()];
            int i=0;
            Iterator it=s.stations.entrySet().iterator();
            for (Map.Entry<String, Station> e : s.stations.entrySet())
            {

                // if (e.getKey()==null) continue;

                Station a=e.getValue();
                stationName[i]=a.name;
                LatLng pos=new LatLng(a.latitutde,a.longitude);
                if (a.marker==null)
                {

                    a.marker=mMap.addMarker(new MarkerOptions().position(pos).title(a.name).snippet("Available bikes:"+a.availableBikes+
                            "\nAvailable docks:"+a.availableDocks+"\n"+a.status));
                }
                else {
                    a.marker.setSnippet("Available bikes:" + a.availableBikes +
                            "\nAvailable docks:" + a.availableDocks + "\n" + a.status);
                }

            }

            //from to autocomplete
            if (adapter==null) {
                adapter = new ArrayAdapter<String>(currentInstance, android.R.layout.simple_dropdown_item_1line, stationName);
                fromStationTextBox.setAdapter(adapter);
                toStationTextBox.setAdapter(adapter);
                fromStationTextBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                toStationTextBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            }

            s.findPath(40.732314, -73.996343, 40.741029, -73.994251);


            if (s.bestA!=null && s.bestB!=null)
            {
                it=s.stations.entrySet().iterator();
                for (Map.Entry<String, Station> e : s.stations.entrySet())
                {
                    Station a=e.getValue();
                    if (a!=s.bestA && a!=s.bestB)
                    {
                        a.marker.setVisible(false);
                    }
                }
            }
            new Route(currentInstance,rHandler).execute();
        }


    };




    private void logoutButton_click()
    {
        finish();
    }

    private void reserveButton_click()
    {
        if(fromStationTextBox.getText().length()<=0 || toStationTextBox.length()<=0)
            return;

        Intent reserveIntent = new Intent(this,ReserveActivity.class);
        reserveIntent.putExtra("from", fromStationTextBox.getText().toString());
        reserveIntent.putExtra("to", toStationTextBox.getText().toString());


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        //System.out.println(dateFormat.format(cal.getTime())); //2014/08/06 16:00:22


        reserveIntent.putExtra("time", timeFormat.format(cal.getTime()));
        reserveIntent.putExtra("date", dateFormat.format(cal.getTime()));
        
        startActivity(reserveIntent);
    }
    private void settingsButton_click()
    {
        Intent settingsIntent = new Intent(this,SettingsActivity.class);




        startActivity(settingsIntent);
    }
    private void historyButton_click()
    {
        Intent historyIntent = new Intent(this,HistoryActivity.class);
        startActivity(historyIntent);
    }
    public void onBackPressed()
    {
        finish();
    }

}
