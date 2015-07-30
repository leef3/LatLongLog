package studios.redleef.latlonglog;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class FullList extends Activity {

    //Adapter
    LatLongListAdapter mAdapter;

    //List
    ArrayList<LatLongObject> latLongList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lat_long_list_full);

        //List Initiate
        latLongList = new ArrayList<LatLongObject>();
        //Data Populate
        loadData();

        ListView fullList = (ListView)findViewById(R.id.fullListView);
        mAdapter = new LatLongListAdapter(this, latLongList);
        fullList.setAdapter(mAdapter);

    }

    public void loadData()
    {
        for(int x = 0; x < 20; x++)
        {
            latLongList.add(new LatLongObject(93.4905, 9384.029));
        }
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
