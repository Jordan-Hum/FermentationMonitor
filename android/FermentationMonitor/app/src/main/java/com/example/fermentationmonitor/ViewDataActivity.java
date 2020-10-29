package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewDataActivity extends AppCompatActivity {
    //Variables
    protected TextView textView;
    protected ListView listView;
    protected List<FermentationData> dataList = new ArrayList<>();

    //Firebase
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("FermentationData");
        setupUI();
    }

    //Initializes the UI
    protected void setupUI() {
        getSupportActionBar().setTitle("Device Readings");
        textView = findViewById(R.id.title_data_view);
        textView.setText("Device Reading Output");
        listView = findViewById(R.id.data_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    dataList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FermentationData data = dataSnapshot.getValue(FermentationData.class);
                        dataList.add(data);
                    }
                    DataList adapter = new DataList(ViewDataActivity.this, R.layout.data_list_layout, dataList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewDataActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}