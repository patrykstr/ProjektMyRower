package com.example.nerexis.myrower;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by patryk on 26.01.2016.
 */
public class Route extends AsyncTask<Void, Void, Void> {

    MapActivity m;
    Handler h;
    @Override
    protected Void doInBackground(Void... result) {

        //https://maps.googleapis.com/maps/api/directions/json?origin=40.732315,-73.996346&destination=40.73332,-73.9951&mode=bicycling&key=AIzaSyCbTg7OVSdAJotBU2SlrZXFIR4cn3qrwj8&units=metric
        //https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling&key=YOUR_API_KEY

        try {

            if (m.startStationRouteP!=null){m.startStationRouteP.remove();}
            if (m.betweenStationsRouteP!=null){m.betweenStationsRouteP.remove();}
            if (m.endStationRouteP!=null){m.endStationRouteP.remove();}

            if (m.startStationRoute == null) {
                m.startStationRoute = new PolylineOptions();
            }
            if (m.endStationRoute == null) {
                m.endStationRoute = new PolylineOptions();
            }
            if (m.betweenStationsRoute == null) {
                m.betweenStationsRoute = new PolylineOptions();
            }

            for (int z = 0; z < 3; z++)
            {
                String A,B,mode;
                PolylineOptions currentRoute;
                if (z==0)
                {
                    mode="walking";
                    A = m.s.Src.latitude + "," + m.s.Src.longitude;
                    B = m.s.bestA.latitutde + "," + m.s.bestA.longitude;
                    currentRoute=m.startStationRoute;
                    currentRoute.color(Color.parseColor("#FF0000"));
                    currentRoute.width((float) 5.0);
                    currentRoute.visible(true);
                }
                else if (z==1)
                {
                    mode="bicycling";
                    A = m.s.bestA.latitutde + "," + m.s.bestA.longitude;
                    B = m.s.bestB.latitutde + "," + m.s.bestB.longitude;
                    currentRoute=m.betweenStationsRoute;
                    currentRoute.color(Color.parseColor("#00FF00"));
                    currentRoute.width((float) 5.0);
                    currentRoute.visible(true);
                }
                else
                {
                    mode="walking";
                    A = m.s.bestB.latitutde + "," + m.s.bestB.longitude;
                    B = m.s.Dst.latitude + "," + m.s.Dst.longitude;
                    currentRoute=m.endStationRoute;
                    currentRoute.color(Color.parseColor("#FF0000"));
                    currentRoute.width((float) 5.0);
                    currentRoute.visible(true);

                }

                if (m.s.bestB == null || m.s.bestA == null) {
                    return null;
                }



                HttpGet httppost = new HttpGet("https://maps.googleapis.com/maps/api/directions/json?origin=" + A + "&destination=" + B + "&mode="+mode+"&key=AIzaSyCbTg7OVSdAJotBU2SlrZXFIR4cn3qrwj8&units=metric");
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    JSONObject subObj;

                    JSONObject jsono = new JSONObject(data);

                    //System.out.println(jsono.toString());

                    JSONArray path = jsono.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");



                    for (int i = 0; i < path.length(); i++) {
                        JSONArray path_a = path.getJSONObject(i).getJSONArray("steps");


                        for (int j = 0; j < path_a.length(); j++) {
                            //path_a.getJSONObject(j).getJSONObject("start_location").getDouble("lat"),path_a.getJSONObject(j).getJSONObject("start_location").getDouble("lng")

                            LatLng posA = new LatLng(path_a.getJSONObject(j).getJSONObject("start_location").getDouble("lat"), path_a.getJSONObject(j).getJSONObject("start_location").getDouble("lng"));
                            LatLng posB = new LatLng(path_a.getJSONObject(j).getJSONObject("end_location").getDouble("lat"), path_a.getJSONObject(j).getJSONObject("end_location").getDouble("lng"));
                            currentRoute.add(posA);
                            currentRoute.add(posB);

                            System.out.println(posA.toString());
                        }
                    }

                    //m.mMap.addPolyline(m.startStationRoute);
                    //System.out.println(path.toString());


                }

            }
            Message msg = Message.obtain();
            h.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void result) {


    }

    public Route(MapActivity m,Handler h)
    {
        this.m=m;
        this.h=h;
    }


}