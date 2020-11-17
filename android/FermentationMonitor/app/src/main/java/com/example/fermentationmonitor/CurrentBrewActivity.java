package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CurrentBrewActivity extends AppCompatActivity {
    protected TextView title;
    protected TextView yeastType;
    protected TextView date;
    protected TextView time;
    protected TextView density;
    protected TextView temp;
    protected ListView listView;
    protected Button graphButton;

    protected List<BrewData> brewDataList = new ArrayList<>();
    protected String userID;
    protected String batchID;
    protected String batchName;
    protected String yeast;

    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_brew);
        setupUI();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("SensorData/" + batchID + "/brewData");
    }

    private void setupUI() {
        getSupportActionBar().setTitle("Current Brew");

        Intent intent = getIntent();
        batchID = intent.getStringExtra("batchId");
        batchName = intent.getStringExtra("batchName");
        yeast = intent.getStringExtra("yeastType");

        title = findViewById(R.id.current_title);
        title.setText(batchName);
        yeastType = findViewById(R.id.current_yeastType);
        yeastType.setText("Yeast Type: " + yeast);
        date = findViewById(R.id.current_date);
        time = findViewById(R.id.current_time);
        density = findViewById(R.id.current_density);
        temp = findViewById(R.id.current_temp);
        listView = findViewById(R.id.current_list);
        graphButton = findViewById(R.id.current_graphButton);
        graphButton.setOnClickListener(onClickGraphButton);
    }

    private Button.OnClickListener onClickGraphButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CurrentBrewActivity.this, DensityGraphActivity.class);
            intent.putExtra("batchId", batchID);
            startActivity(intent);
        }
    };

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
                    brewDataList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BrewData data = dataSnapshot.getValue(BrewData.class);
                        brewDataList.add(data);
                    }
                    BrewDataListAdapter adapter = new BrewDataListAdapter(CurrentBrewActivity.this, R.layout.current_brew_list_layout, brewDataList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CurrentBrewActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}