package me.beartransit.apollojain.myapplication;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by apollojain on 8/17/15.
 */
public class BusListAdapter extends ArrayAdapter<String> {


    private final Activity context;
    private final String[] location_names;
    private final double[] distances;
    int cur_id;

    public BusListAdapter(Activity context, int layout, String[] location_ids, String[] location_names, double[] distances) {
        super(context, layout, location_ids);
        cur_id = layout;
        // TODO Auto-generated constructor stub

        this.context=context;
        this.location_names = location_names;
        this.distances=distances;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(cur_id, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        txtTitle.setText(location_names[position]);
        imageView.setImageResource(R.mipmap.ic_right);
        extratxt.setText(String.valueOf(round(distances[position], 2)) + " mi");
        return rowView;

    };

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
