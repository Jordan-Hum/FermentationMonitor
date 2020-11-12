package com.example.fermentationmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BrewDataListAdapter extends ArrayAdapter<BrewData> {
    //Variables
    private Context context;
    private int resource;

    //Constructor
    public BrewDataListAdapter(@NonNull Context context, int resource, @NonNull List<BrewData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        BrewData data = getItem(position);

        TextView date = convertView.findViewById(R.id.currentList_date);
        TextView time = convertView.findViewById(R.id.currentList_time);
        TextView density = convertView.findViewById(R.id.currentList_density);
        TextView temperature = convertView.findViewById(R.id.currentList_temp);

        date.setText(data.getDate());
        time.setText(data.getTime());
        density.setText(String.valueOf(data.getSpecificGravity()));
        temperature.setText(data.getTempOfLiquid()+"°C");

        return convertView;
    }
}
