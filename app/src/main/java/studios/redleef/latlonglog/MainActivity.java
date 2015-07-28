package studios.redleef.latlonglog;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends Activity implements LocationListener {

    private TextView gpsStatus2, gpsAccuracy2, gpsLatitude2, gpsLongitude2, gpsSpeed2, gpsTime2, gpsContact2, gpsLastUpdate2;
    private TextView gpsCount2;

    //GPS
    Location currentLocation;
    LocationManager locationManager;
    String locationProvider;

    //Timers and Counters
    int numGps;
    Date start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gpsStatus2 = (TextView)findViewById(R.id.gpsStatus2);
        gpsAccuracy2 = (TextView)findViewById(R.id.gpsAccuracy2);
        gpsLatitude2 = (TextView)findViewById(R.id.gpsLatitude2);
        gpsLongitude2 = (TextView)findViewById(R.id.gpsLongitude2);
        gpsSpeed2 = (TextView)findViewById(R.id.gpsSpeed2);
        gpsTime2 = (TextView)findViewById(R.id.gpsTime2);
        gpsContact2 = (TextView)findViewById(R.id.gpsContact2);
        gpsLastUpdate2 = (TextView)findViewById(R.id.gpsLastUpdate2);
        gpsCount2 = (TextView)findViewById(R.id.gpsCount2);

        numGps = 0;
        gpsCount2.setText(String.valueOf(numGps));


        InitializeLocationManager();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //GPS
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //GPS
        locationManager.removeUpdates(this);
    }

    //==================================GPS INTERFACE METHODS==============================
    private void InitializeLocationManager()
    {
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria criteriaForLocation = new Criteria();
        //criteriaForLocation.setPowerRequirement(Criteria.POWER_HIGH);
        criteriaForLocation.setAccuracy(Criteria.ACCURACY_FINE);

        locationProvider = locationManager.getBestProvider(criteriaForLocation, true);
    }

    public void onLocationChanged(Location location)
    {
        currentLocation = location;
        if(currentLocation != null)
        {
            /*
            if(currentLocation.getProvider() == LocationManager.GPS_PROVIDER)
            {
                gpsLatitude2.setText(String.valueOf(currentLocation.getLatitude()));
                gpsLongitude2.setText(String.valueOf(currentLocation.getLongitude()));
                gpsSpeed2.setText(String.valueOf(currentLocation.getSpeed()));
                gpsAccuracy2.setText(String.valueOf(currentLocation.getAccuracy() + " Meters"));
            }
            */
            gpsLatitude2.setText(String.valueOf(currentLocation.getLatitude()));
            gpsLongitude2.setText(String.valueOf(currentLocation.getLongitude()));
            gpsSpeed2.setText(String.valueOf(currentLocation.getSpeed()));
            gpsAccuracy2.setText(String.valueOf(currentLocation.getAccuracy() + " Meters"));

            if(numGps == 0)
            {
                //First Contact
            }
            else
            {
                //Last Update
            }

            ++numGps;
            gpsCount2.setText(String.valueOf(numGps));
        }
    }
    public void onProviderDisabled(String provider)
    {

    }
    public void onProviderEnabled(String provider)
    {

    }
    public void onStatusChanged(String provider, int status, Bundle extras )
    {
        if(provider == LocationManager.GPS_PROVIDER)
        {
            gpsStatus2.setText(String.valueOf(status));
        }
    }
    //=================================TIMER ===============================


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
