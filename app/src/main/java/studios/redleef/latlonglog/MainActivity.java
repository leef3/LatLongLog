package studios.redleef.latlonglog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

//For Data Save
//GSON Serializable Data
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;


public class MainActivity extends Activity implements LocationListener {

    private TextView gpsStatus2, gpsAccuracy2, gpsLatitude2, gpsLongitude2, gpsSpeed2, gpsTime2, gpsContact2, gpsLastUpdate2;
    private TextView gpsCount2;

    //GPS
    Location currentLocation;
    LocationManager locationManager;
    String locationProvider;

    //Timers and Counters
    int numGps;
    long lastUpdate;

    //Temp Storage
    double lastLatitude;
    double lastLongitude;

    //ListView ArrayAdapters and ArrayList
    LatLongListAdapter mAdapter;
    ArrayList<LatLongObject> latLongList;
    ArrayList<LatLongObject> tempLatLongList;

    //Context
    Context context;

    //Data Storage
    public static final String MASTER_SAVE_NAME = "MASTER_SAVE_DATA_LATLONG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        //UI Element Initializations
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

        //Initialize ArrayList
        latLongList = new ArrayList<LatLongObject>();
        tempLatLongList = new ArrayList<LatLongObject>();
        //Populate Data
        loadData();


        //TODO Cut data short to 3 for main page list
        //LatLong List Initialization
        ListView latLongListView = (ListView)findViewById(R.id.latLongListView);
        mAdapter = new LatLongListAdapter(this, tempLatLongList);
        latLongListView.setAdapter(mAdapter);

        //Add LatLong button intialization
        ImageView addButton = (ImageView)findViewById(R.id.addLog);
        addButton.setOnClickListener(new View.OnClickListener() {
            // Start new list activity
            public void onClick(View v) {
                addLogItem();
            }
        });

        TextView seeAll = (TextView)findViewById(R.id.seeAllTextView);
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullList.class);
                startActivity(intent);
            }
        });

        //TODO Add Switch Listener for Switch GPS on off

        //GPS Initializations
        InitializeLocationManager();
    }

    public void addLogItem()
    {
        //TODO Add Checks for last update is recent if so, then add
        latLongList.add(new LatLongObject(lastLatitude, lastLongitude));
        mAdapter.notifyDataSetChanged();
        onlyThree();
        saveData();
    }

    public void onlyThree() {
        tempLatLongList.clear();
        tempLatLongList.add(latLongList.get(latLongList.size() - 1));
        tempLatLongList.add(latLongList.get(latLongList.size() - 2));
        tempLatLongList.add(latLongList.get(latLongList.size() - 3));

    }

    private void loadData()
    {
        latLongList.clear();
        SharedPreferences settings = context.getSharedPreferences("pref", 0);
        settings = context.getSharedPreferences("pref", 0);
        String objectData = settings.getString(MASTER_SAVE_NAME, "");
        if (!objectData.equals("")) {
            System.out.println("Object Data: " + objectData);
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<LatLongObject>>() {
            }.getType();
            JsonArray jArray = new JsonParser().parse(objectData).getAsJsonArray();
            for (JsonElement e : jArray) {
                LatLongObject c = gson.fromJson(e, LatLongObject.class);
                latLongList.add(c);
            }
        }
        onlyThree();
    }

    private void saveData()
    {
        SharedPreferences.Editor settings = context.getSharedPreferences("pref",0).edit();
        String data = new Gson().toJson(latLongList);
        System.out.println("Data!: " + data);
        settings.putString(MASTER_SAVE_NAME, data);
        settings.commit();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //GPS
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        loadData();
        mAdapter.notifyDataSetChanged();
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
            gpsLatitude2.setText(String.valueOf(currentLocation.getLatitude()));
            lastLatitude = currentLocation.getLatitude();
            gpsLongitude2.setText(String.valueOf(currentLocation.getLongitude()));
            lastLongitude = currentLocation.getLongitude();
            gpsSpeed2.setText(String.valueOf(currentLocation.getSpeed()));
            gpsAccuracy2.setText(String.valueOf(currentLocation.getAccuracy() + " Meters"));

            if(numGps == 0)
            {
                //First Contact
                gpsContact2.setText(String.valueOf(System.currentTimeMillis()));
                gpsLastUpdate2.setText(String.valueOf(System.currentTimeMillis()));
                lastUpdate = System.currentTimeMillis();
            }
            else
            {
                gpsLastUpdate2.setText(String.valueOf(System.currentTimeMillis()));
                lastUpdate = System.currentTimeMillis();
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
