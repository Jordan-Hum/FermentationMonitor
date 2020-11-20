package com.example.fermentationmonitor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button registerButton;
    private EditText registerInput;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        setupUI();
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

    private Button.OnClickListener registerDevice = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            //TODO: Check If DeviceId is set in Firestore ? If exist -> link DeviceId to user : Else -> return Toast "Invalid Device Id"
        }
    };
}
