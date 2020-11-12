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

public class BatchListAdapter extends ArrayAdapter<Batch> {
    //Variables
    private Context context;
    private int resource;

    //Constructor
    public BatchListAdapter(@NonNull Context context, int resource, @NonNull List<Batch> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        Batch data = getItem(position);

        TextView name = convertView.findViewById(R.id.pastList_batch_name);
        TextView startDate = convertView.findViewById(R.id.pastList_start_date);
        TextView endDate = convertView.findViewById(R.id.pastList_end_date);
        TextView yeastType = convertView.findViewById(R.id.pastList_yeast_type);

        name.setText(data.getBatchName());
        startDate.setText(data.getStartDate());
        endDate.setText(data.getEndDate());
        yeastType.setText(data.getYeastType());

        return convertView;
    }
}
