package me.beartransit.apollojain.myapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class BusListActivity extends AppCompatActivity {
    String base_url = "http://beartransit.daylen.com";
    private TextView t;
    private TextView title;
    private TextView description;
    private String[] s;
    BusListActivity b = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        setTitle("All stops");
        getHome();
    }


    public void getHome(){
        setContentView(R.layout.activity_bus_list);
        t=(TextView)findViewById(R.id.all_stops_title);
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String longitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());
        new GetJsonTask().execute(base_url + "/api/v1/stops?lat=" + latitude + "&lon=" + longitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_map:
                Intent intent1 = new Intent(this, MapsActivity.class);
                this.startActivity(intent1);
                break;
            case R.id.menu_list:
                Intent intent2 = new Intent(this, BusListActivity.class);
                this.startActivity(intent2);
                break;
            case R.id.menu_current:
                Intent intent3 = new Intent(this, CurrentMapsActivity.class);
                this.startActivity(intent3);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }



    private class GetJsonTask extends AsyncTask {
        protected Object doInBackground(Object[] urls) {
            try{
                JSONArray page = new Communicator().getJsonArrayFromUrl((String) urls[0]);
                return page;
            }catch(Exception e){
                return null;
            }

        }

        protected void onPostExecute(Object result) {
            final HashMap hm = new HashMap();
            JSONArray jobject = (JSONArray) result;
            String[] location_names = new String[jobject.length()];
            String[] location_ids = new String[jobject.length()];
            double[] location_dist = new double[jobject.length()];
            for(int i = 0; i<jobject.length(); i++){
                try {
                    location_names[i] = jobject.getJSONObject(i).getString("name");
                    location_ids[i] = jobject.getJSONObject(i).getString("id");
                    location_dist[i] = jobject.getJSONObject(i).getDouble("dist");
                    hm.put(location_ids[i], location_names[i]);
                } catch (JSONException e) {
                    location_names[i] = e.toString();
                    location_ids[i] = e.toString();
                    location_dist[i] = 0;
                }
                                            }
            BusListAdapter adapter = new BusListAdapter(b, R.layout.bus_list, location_ids, location_names,  location_dist);
            final ListView listView = (ListView) findViewById(R.id.all_stops_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String location = listView.getItemAtPosition(position).toString();
                    String name = (String) hm.get(location);
                    String ms = Long.toString(System.currentTimeMillis());
                    String url = (new Communicator()).convert_id_to_url(location);
                    Intent intent = new Intent(b, LocationDetailsActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("name", name);
                    b.startActivity(intent);

                }
            });
        }

    }
}
