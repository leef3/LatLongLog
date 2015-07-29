package studios.redleef.latlonglog;

/**
 * Created by Fred Lee on 7/29/2015.
 */
public class LatLongObject {

    private double Latitude, Longitude;
    private long timeStamp;

    LatLongObject(double latitude, double longitude)
    {
        timeStamp = System.currentTimeMillis();
        Latitude = latitude;
        Longitude = longitude;
    }

    public double getLatitude() {return Latitude;}
    public double getLongitude() {return Longitude;}
    public long getTimeStamp() {return timeStamp;}
}
