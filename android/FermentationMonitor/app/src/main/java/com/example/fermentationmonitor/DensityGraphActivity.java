package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DensityGraphActivity extends AppCompatActivity {
    protected LineChart lineChart;
    protected LineData lineData;
    protected LineDataSet lineDataSet;

    protected List<Entry> lineEntries = new ArrayList<>();
    protected String batchID;

    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_density_graph);
        setupUI();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("SensorData/" + batchID + "/brewData");
    }

    private void setupUI() {
        getSupportActionBar().setTitle("Graph");

        Intent intent = getIntent();
        batchID = intent.getStringExtra("batchId");

        lineChart = findViewById(R.id.lineChart);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    float x = 0;
                    lineEntries.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        float sg = Float.valueOf(dataSnapshot.child("specificGravity").getValue().toString());
                        lineEntries.add(new Entry(x, sg));
                        x++;
                    }
                    generateGraph();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DensityGraphActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Everything related to the graph goes here
    // Creating the graph
    // Customizing the graph
    private void generateGraph() {
        lineDataSet = new LineDataSet(lineEntries, "Specific Gravity");
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        lineDataSet.setColors(ColorTemplate.rgb("800000"));
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(15f);

        lineChart.animateXY(2000,2000);

    }
}