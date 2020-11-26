package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.number.Precision;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Collections;
import java.util.List;

public class CurrentBrewActivity extends AppCompatActivity {
    protected TextView title;
    protected TextView yeastType;
    protected TextView alcoholLevel;
    protected TextView date;
    protected TextView time;
    protected TextView density;
    protected TextView temp;
    protected ListView listView;
    protected Button graphButton;
 	protected FloatingActionButton deleteButton;
 	private Toolbar toolbar;

    protected List<BrewData> brewDataList = new ArrayList<>();
    protected String userID;
    protected String batchID;
    protected String batchName;
    protected String yeast;

    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    protected SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_brew);
        sharedPreferenceHelper = new SharedPreferenceHelper(CurrentBrewActivity.this);

        setupUI();

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("SensorData/" + batchID + "/brewData");
    }

    private void setupUI() {
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Current Brew");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        batchID = sharedPreferenceHelper.getBatchId();
        batchName = sharedPreferenceHelper.getBatchName();
        yeast = sharedPreferenceHelper.getYeastType();

        title = findViewById(R.id.current_title);
        title.setText(batchName);
        yeastType = findViewById(R.id.current_yeastType);
        yeastType.setText("Yeast Type: " + yeast);
        alcoholLevel = findViewById(R.id.current_alcoholLevel);
        alcoholLevel.setText("Alcohol Level: - ");
        date = findViewById(R.id.current_date);
        time = findViewById(R.id.current_time);
        density = findViewById(R.id.current_density);
        temp = findViewById(R.id.current_temp);
        listView = findViewById(R.id.current_list);
        graphButton = findViewById(R.id.current_graphButton);
        graphButton.setOnClickListener(onClickGraphButton);
        deleteButton = findViewById(R.id.current_deleteButton);
        deleteButton.setOnClickListener(onClickDeleteButton);
    }

    private Button.OnClickListener onClickGraphButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CurrentBrewActivity.this, DensityGraphActivity.class);
            intent.putExtra("batchId", batchID);
            startActivity(intent);
		}
    };
    
    private Button.OnClickListener onClickDeleteButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder((CurrentBrewActivity.this));
            builder.setMessage("Are you sure want to delete batch: " + batchName + "?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference delRef = db.getReference("SensorData/" + batchID);
                            delRef.removeValue();

                            Toast.makeText(CurrentBrewActivity.this, "Batch Deleted" , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CurrentBrewActivity.this, PastBrewsActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
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
                    Collections.reverse(brewDataList);
                    calculateAlcoholLevel();
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

    private void calculateAlcoholLevel() {
        if(brewDataList.isEmpty()) {
            alcoholLevel.setText("Alcohol Level: - ");
        } else {
            float startSG = Float.valueOf(brewDataList.get(brewDataList.size() - 1).getSpecificGravity());
            float finalSG = Float.valueOf(brewDataList.get(0).getSpecificGravity());

            double scale = Math.pow(10, 2);
            double alcoholLvl = Math.round(((startSG - finalSG) * 131.25) * scale) / scale;
            alcoholLevel.setText("Alcohol Level: " + alcoholLvl + "%");
        }
    }
}