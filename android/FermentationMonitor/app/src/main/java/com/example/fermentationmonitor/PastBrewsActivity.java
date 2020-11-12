package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

public class PastBrewsActivity extends AppCompatActivity {
    //Variables
    protected TextView title;
    protected TextView batchName;
    protected TextView startDate;
    protected TextView endDate;
    protected TextView yeastType;
    protected ListView listView;
    protected FloatingActionButton addButton;

    protected List<Batch> batchList = new ArrayList<>();
    protected String userID;

    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_brews);
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("SensorData");
        setupUI();
    }

    //Initialize the UI
    private void setupUI() {
        getSupportActionBar().setTitle("Past Brews");
        title = findViewById(R.id.past_title);
        title.setText("Past Brews");
        batchName = findViewById(R.id.past_batch_name);
        startDate = findViewById(R.id.past_start_date);
        endDate = findViewById(R.id.past_end_date);
        yeastType = findViewById(R.id.past_yeast_type);
        listView = findViewById(R.id.past_list);
        addButton = findViewById(R.id.past_addButton);
        addButton.setOnClickListener(onClickaddButton);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PastBrewsActivity.this, CurrentBrewActivity.class);
                intent.putExtra("batchId", batchList.get(position).getId());
                intent.putExtra("yeastType", batchList.get(position).getYeastType());
                intent.putExtra("batchName", batchList.get(position).getBatchName());
                startActivity(intent);
            }
        });
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
                    batchList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Batch data = dataSnapshot.getValue(Batch.class);
                        data.setId(dataSnapshot.getKey());
                        batchList.add(data);
                    }
                    BatchListAdapter adapter = new BatchListAdapter(PastBrewsActivity.this, R.layout.past_brews_list_layout, batchList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PastBrewsActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private FloatingActionButton.OnClickListener onClickaddButton = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(PastBrewsActivity.this, "Click!" , Toast.LENGTH_SHORT).show();
        }
    };

    //Creates the menu at the top right of the screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.past_brews_activity_menu, menu);
        return true;
    }

    //Listener to select menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.past_brews_activity_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Logout User
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(PastBrewsActivity.this, loginActivity.class);
        startActivity(intent);
        finish();
    }
}