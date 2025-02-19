package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PastBrewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static String TAG = "PastBrewActivity";

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
    protected SharedPreferenceHelper sharedPreferenceHelper;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_brews);

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("SensorData");
        sharedPreferenceHelper = new SharedPreferenceHelper(PastBrewsActivity.this);

        setupUI();
        setupCloudMessaging();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Initialize the UI
    private void setupUI() {
        batchName = findViewById(R.id.past_batch_name);
        startDate = findViewById(R.id.past_start_date);
        endDate = findViewById(R.id.past_end_date);
        yeastType = findViewById(R.id.past_yeast_type);
        listView = findViewById(R.id.past_list);
        addButton = findViewById(R.id.past_addButton);
        addButton.setOnClickListener(onClickaddButton);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_drawer);
        toolbar = findViewById(R.id.myToolbar);

        navigationView.setNavigationItemSelectedListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PastBrewsActivity.this, CurrentBrewActivity.class);
                Batch batch = batchList.get(position);
                sharedPreferenceHelper.saveBatchId(batch);
                sharedPreferenceHelper.saveBatchName(batch);
                sharedPreferenceHelper.saveYeastType(batch);
                sharedPreferenceHelper.saveEndDate(batch);
                sharedPreferenceHelper.saveDeviceId(batch);
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
        dbRef.orderByChild("startDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    batchList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Batch data = dataSnapshot.getValue(Batch.class);
                        if(data.getUserId().equals(userID)) {
                            data.setId(dataSnapshot.getKey());
                            batchList.add(data);
                        }
                    }
                    Collections.reverse(batchList);
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
            dialogActivity dialog = new dialogActivity();
            dialog.show(getSupportFragmentManager(), "addBrewDialog");
        }
    };

    //Logout User
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(PastBrewsActivity.this, loginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_settings:
                Intent profileActivity = new Intent(this, ProfileSettingsActivity.class);
                startActivity(profileActivity);
                return true;
            case R.id.past_brews_activity_logout:
                logout();
                return true;
            case R.id.basics_brewing:
                Intent basicinfoActivity = new Intent(this, BasicInfoActivity.class);
                startActivity(basicinfoActivity);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupCloudMessaging(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(PastBrewsActivity.this, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Log.d(TAG, token);
                    }
                });
    }
}