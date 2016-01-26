package com.example.nerexis.myrower;

import android.os.Handler;
import android.os.Message;

import com.google.android.gms.maps.model.LatLng;

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
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by patryk on 26.01.2016.
 */
public class Sync extends TimerTask {

    public HashMap<String, Station> stations= new HashMap<String, Station>();
    public Station bestA,bestB;
    public LatLng Src,Dst;



    public void findPath(double latA,double longA,double latB,double longB)
    {
        Src=new LatLng(latA,longA);
        Dst=new LatLng(latB,longB);
        bestA=null;
        bestB=null;
        double distA=Double.MAX_VALUE,distB=Double.MAX_VALUE;
        for (Map.Entry<String, Station> e : stations.entrySet())
        {
            Station a=e.getValue();
            Double tmp;
            if ((a.availableBikes!=0 || true) && Math.sqrt(Math.pow((latA-a.latitutde),2.0)+Math.pow((longA - a.longitude), 2.0))<distA)
            {
                distA=Math.sqrt(Math.pow((latA-a.latitutde),2.0)+Math.pow((longA - a.longitude), 2.0));
                bestA=a;
            }
            if ((a.availableDocks!=0 || true) && Math.sqrt(Math.pow((latB-a.latitutde),2.0)+Math.pow((longB - a.longitude), 2.0))<distB)
            {
                distB=Math.sqrt(Math.pow((latB-a.latitutde),2.0)+Math.pow((longB - a.longitude), 2.0));
                bestB=a;
            }
        }
        System.out.println(latA+" "+longA+"\n"+bestA.latitutde+" "+bestA.longitude+"\n"+bestB.latitutde+" "+bestB.longitude+"\n"+latB+" "+longB);


    }




    Handler h;
    public Sync(Handler h)
    {
        this.h=h;
    }

    public void run(){
        try {

            //------------------>>
            HttpGet httppost = new HttpGet("https://www.citibikenyc.com/stations/json");
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);

            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                JSONObject subObj;

                JSONObject jsono = new JSONObject(data);

                JSONArray subArray=jsono.getJSONArray("stationBeanList");
                for (int j = 0; j < subArray.length(); j++)
                {
                    subObj = subArray.getJSONObject(j);
                    Station a=new Station();
                    a.name=subObj.getString("stationName");
                    a.address=subObj.getString("stAddress1");
                    a.availableDocks=Integer.parseInt(subObj.getString("availableDocks"));
                    a.availableBikes=Integer.parseInt(subObj.getString("availableBikes"));
                    a.status=subObj.getString("statusValue");
                    a.longitude=Double.parseDouble(subObj.getString("longitude"));
                    a.latitutde=Double.parseDouble(subObj.getString("latitude"));
                    stations.put(a.name,a);

                }

                Message msg = Message.obtain();
                h.sendMessage(msg);

                return ;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return ;

    }
}
