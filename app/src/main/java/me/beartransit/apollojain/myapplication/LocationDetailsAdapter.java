package me.beartransit.apollojain.myapplication;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by apollojain on 8/18/15.
 */
public class LocationDetailsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final JSONArray arr;
    int cur_id;

    public LocationDetailsAdapter(Activity context, int layout, String[] location_ids, JSONArray arr) {
        super(context, layout, location_ids);
        cur_id = layout;
        // TODO Auto-generated constructor stub

        this.context=context;
        this.arr = arr;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(cur_id, null, true);

        TextView lineName = (TextView) rowView.findViewById(R.id.line_name);
        TextView finalDestination = (TextView) rowView.findViewById(R.id.final_destination);
        TextView firstStop = (TextView) rowView.findViewById(R.id.first_time);
        TextView secondStop = (TextView) rowView.findViewById(R.id.second_time);
        try{
            lineName.setText((String) arr.getJSONObject(position).getString("line"));
            finalDestination.setText((String) arr.getJSONObject(position).getString("line_note"));
            JSONArray cur = arr.getJSONObject(position).getJSONArray("times");
            if(cur.length() >= 1){
              firstStop.setText(formatTime(cur.getInt(0)));
            }
            if(cur.length() >= 2){
                secondStop.setText(formatTime(cur.getInt(1)));
            }
        }catch(Exception e){
            Log.e("ERROR", e.toString());
        }

//        extratxt.setText(String.valueOf(round(distances[position], 2)) + " mi");
        return rowView;

    }

    public String formatTime(int time){
        int min = time % 100;
        int hour = (time - min)/100;
        int totalMins = hour*60 + min;
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int curHour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int curMin = calendar.get(Calendar.MINUTE);
        int curTotalMins = curHour*60 + curMin;
        int diff = totalMins - curTotalMins;
        if(diff < 0){
            diff = 24*60 + diff;
        }
        String s = "";
        int finalMin = diff % 60;
        int finalHour = (diff - finalMin)/60;
        if(finalHour > 0){
            s = Integer.toString(finalHour) + "h " + Integer.toString(finalMin) + "m";
        }else{
            s = Integer.toString(finalHour) + " min";
        }
        return s;
    }

}
