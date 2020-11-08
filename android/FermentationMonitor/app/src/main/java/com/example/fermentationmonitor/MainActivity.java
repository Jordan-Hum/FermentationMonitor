package com.example.fermentationmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //Variables
    protected TextView textView;
    protected Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
    }

    //Initialize the UI
    private void setupUI() {
        getSupportActionBar().setTitle("Welcome");
        textView = findViewById(R.id.title_main_page);
        textView.setText("Fermentation Monitor App");
        button = findViewById(R.id.view_data_button);
        button.setOnClickListener(onClickSaveButton);
    }

    private Button.OnClickListener onClickSaveButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ViewDataActivity.class);
            startActivity(intent);
        }
    };

    //**TO LOGOUT**
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, loginActivity.class);
        startActivity(intent);
        finish();
    }
}