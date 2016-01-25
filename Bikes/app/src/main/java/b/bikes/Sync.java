package b.bikes;

import android.os.Handler;
import android.os.Message;

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
import java.util.TimerTask;


public class Sync extends TimerTask {

    public HashMap<String, Station> stations= new HashMap<String, Station>();

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
                    a.longitude=Float.parseFloat(subObj.getString("longitude"));
                    a.latitutde=Float.parseFloat(subObj.getString("latitude"));
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

