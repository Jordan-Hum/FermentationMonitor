package com.example.fermentationmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataList extends ArrayAdapter<FermentationData> {
    //Variables
    private Context context;
    private int resource;

    //Constructor
    public DataList(@NonNull Context context, int resource, @NonNull List<FermentationData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        FermentationData data = getItem(position);

        TextView alcoholLevel = convertView.findViewById(R.id.alcohol_level);
        TextView density = convertView.findViewById(R.id.density);
        TextView temperature = convertView.findViewById(R.id.temperature);

        alcoholLevel.setText("Alcohol Level: " + data.getAlcoholLevel() + "%");
        density.setText("Density: " + data.getDensity());
        temperature.setText("Temperature: " + data.getTemperature());

        return convertView;
    }
}
