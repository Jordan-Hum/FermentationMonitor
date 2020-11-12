package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DensityGraphActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList  barEntries = new ArrayList <>(2);
    String batchID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_density_graph);

        Intent intent = getIntent();
        batchID = intent.getStringExtra("batchId");
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("SensorData/" + batchID + "/brewData");

        barChart = findViewById(R.id.BarChart);
        loadData();
        Log.d("console", String.valueOf(barEntries));

        barDataSet = new BarDataSet(barEntries, "Density");
        barData = new BarData(barDataSet);
        barChart.setData(barData);

        barDataSet.setColors(ColorTemplate.rgb("800000"));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(15f);

        barChart.animateXY(2000,2000);

    }


    private void getEntries() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, 1));
        barEntries.add(new BarEntry(2f, 2));
        barEntries.add(new BarEntry(3f, 3));
        barEntries.add(new BarEntry(4f, 4));
        barEntries.add(new BarEntry(5f, 5));
        barEntries.add(new BarEntry(6f, 6));
    }


    private void loadData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    int x = 1;
                    barEntries.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        float sg = Float.valueOf(dataSnapshot.child("specificGravity").getValue().toString());
                        barEntries.add(new BarEntry(Float.valueOf(x), sg));
                        x++;
                    }
                    Log.d("abc", String.valueOf(barEntries));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DensityGraphActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}