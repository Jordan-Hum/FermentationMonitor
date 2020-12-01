package com.example.fermentationmonitor;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button registerButton;
    private EditText registerInput;

    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    protected FirebaseFirestore fStore;
    private FirebaseAuth fAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        setupUI();
        setupFirebase();
    }

    private void setupUI(){
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerButton = findViewById(R.id.registerButton);
        registerInput = findViewById(R.id.deviceIdInput);

        registerButton.setOnClickListener(registerDevice);
    }

    private void setupFirebase(){
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    private Button.OnClickListener registerDevice = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(!registerInput.getText().equals("")) {
                registerDevice();
            }
        }
    };

    private void registerDevice(){
        dbRef = db.getReference("Devices/" + registerInput.getText() + "/userId");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if( ( (String) snapshot.getValue() ).equals("-1") ){
                        dbRef.setValue(fAuth.getUid());
                        Toast.makeText(ProfileSettingsActivity.this, "Device has been registered", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ProfileSettingsActivity.this, "Error: Device has already been registered", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ProfileSettingsActivity.this, "Error: Device Id does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileSettingsActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
