package b.bikes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Sync s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void findPath(float latA,float longA,float latB,float longB)
    {
        Station bestA=null,bestB=null;
        float distA=Float.MAX_VALUE,distB=Float.MAX_VALUE;
        for (Map.Entry<String, Station> e : s.stations.entrySet())
        {
            Station a=e.getValue();
            float tmp;
            if (a.availableBikes!=0 && Math.sqrt(Math.pow((latA-a.latitutde),2.0)+Math.pow((longA - a.longitude), 2.0))<distA)
            {
                distA=(float)Math.sqrt(Math.pow((latA-a.latitutde),2.0)+Math.pow((longA - a.longitude), 2.0));
                bestA=a;
            }
            if (a.availableDocks!=0 && Math.sqrt(Math.pow((latB-a.latitutde),2.0)+Math.pow((longB - a.longitude), 2.0))<distB)
            {
                distB=(float)Math.sqrt(Math.pow((latB-a.latitutde),2.0)+Math.pow((longB - a.longitude), 2.0));
                bestB=a;
            }
        }

        if (bestA==bestB)
        {
            
        }
        else
        {

        }

    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            Iterator it=s.stations.entrySet().iterator();
            for (Map.Entry<String, Station> e : s.stations.entrySet())
            {
               // if (e.getKey()==null) continue;

                Station a=e.getValue();
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
        }
    };





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng nyc = new LatLng(40.7127, -74.0059);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nyc, 10.0f));
        Timer sync = new Timer("sync", true);
        sync.scheduleAtFixedRate(s=new Sync(mHandler), (long) 0, (long) 60000);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }
}
