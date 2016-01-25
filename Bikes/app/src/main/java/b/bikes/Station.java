package b.bikes;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by patryk on 25.01.2016.
 */
public class Station {
    public String name;
    public String status;
    public String address;
    public int availableBikes;
    public int availableDocks;
    public float longitude;
    public float latitutde;
    Marker marker;
}