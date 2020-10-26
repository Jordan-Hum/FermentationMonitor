package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    protected TextView textView;
    protected EditText editText;
    protected Button button;
    private long id = 0;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Firebase Connected Successfully", Toast.LENGTH_LONG).show();
        setupUI();
    }

    //Initialize the UI
    private void setupUI() {
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.textEdit1);
        button = findViewById(R.id.button);
        button.setOnClickListener(onClickSaveButton);
        loadData();
    }

    private Button.OnClickListener onClickSaveButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendData();
        }
    };

    //Send data to Firebase
    //**IMPORTANT** Firebase can store Objects .setValue(Object)
    //Use .push() for auto generated Row Name
    private void sendData() {
        String data = editText.getText().toString().trim();
        DatabaseReference dbRef = db.getReference();

        dbRef.child("TableName").child("RowName"+id).child("ColumnName").setValue(data);
        loadData();
        id++;
    }

    //Get data from Firebase
    /*
    TODO
    Learn how ValueEventListener works
    Learn when ValueEventListener gets called
     */
    private void loadData() {
        DatabaseReference dbRef = db.getReference();

        dbRef.child("TableName").child("RowName"+id).addValueEventListener(dataListener);
    }

    ValueEventListener dataListener =  new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                String data = snapshot.child("ColumnName").getValue().toString();
                textView.setText("Previous data input: " + data);
            } else {
                textView.setText("No Data Stored");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
        }
    };
}