package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
    private Toolbar toolbar;


    protected ArrayList<String> xLabel = new ArrayList<>();
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
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Graph");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        String date = String.valueOf(dataSnapshot.child("date").getValue().toString());
                        xLabel.add(new String(date));
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
        lineDataSet.setValueTextSize(12f);

        lineChart.setExtraTopOffset(65f);
        lineChart.setVisibleXRangeMaximum(5f);
        lineChart.setScaleEnabled(false);


        XAxis xaxis = lineChart.getXAxis();
        xaxis.setSpaceMin(1.5f);
        xaxis.setCenterAxisLabels(false);
        xaxis.isDrawGridLinesBehindDataEnabled();
        xaxis.setLabelCount(6, true); //forces xaxis to freeze with 6 values and only move the line graph


        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawGridLines(true);
        xaxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int)value);
            }
        });

    }









}