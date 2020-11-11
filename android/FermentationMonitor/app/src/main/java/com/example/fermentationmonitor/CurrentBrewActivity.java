package com.example.fermentationmonitor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;
    private String brewID = "3LrYqN2I48npWjbQ9Jqh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_brew);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        setupUI();
    }

    private void setupUI() {
        getSupportActionBar().setTitle("Current Brew");
        title = findViewById(R.id.current_title);
        yeastType = findViewById(R.id.current_yeastType);
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

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        CollectionReference colRef = fStore.collection("users").document(userID).collection("brews").document(brewID).collection("brew_data");
        colRef.orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        brewDataList.add(doc.toObject(BrewData.class));
                    }
                }
                BrewDataListAdapter adapter = new BrewDataListAdapter(CurrentBrewActivity.this, R.layout.current_brew_list_layout, brewDataList);
                listView.setAdapter(adapter);
            }
        });
    }
}