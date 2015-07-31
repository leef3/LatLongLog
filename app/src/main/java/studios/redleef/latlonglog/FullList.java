package studios.redleef.latlonglog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class FullList extends Activity {

    //Adapter
    LatLongListAdapter mAdapter;

    //Ciontext
    Context context;

    //Save Name
    public static final String MASTER_SAVE_NAME = "MASTER_SAVE_DATA_LATLONG";

    //List
    ArrayList<LatLongObject> latLongList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lat_long_list_full);

        context = this;

        //List Initiate
        latLongList = new ArrayList<LatLongObject>();
        latLongList.clear();
        //Data Populate
        loadData();

        ListView fullList = (ListView)findViewById(R.id.fullListView);
        mAdapter = new LatLongListAdapter(this, latLongList);
        fullList.setAdapter(mAdapter);
        fullList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                latLongList.remove(position);
                mAdapter.notifyDataSetChanged();
                saveData();
                return true;
            }
        });

    }

    private void loadData()
    {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_list, menu);
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
