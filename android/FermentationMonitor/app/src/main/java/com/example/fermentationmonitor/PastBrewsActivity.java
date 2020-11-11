package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class PastBrewsActivity extends AppCompatActivity {
    //Variables
    protected TextView textView;
    protected Button button;
    protected FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_brews);
        setupUI();
    }

    //Initialize the UI
    private void setupUI() {
        getSupportActionBar().setTitle("Past Brews");
        textView = findViewById(R.id.title_main_page);
        textView.setText("Fermentation Monitor App");
        button = findViewById(R.id.view_data_button);
        button.setOnClickListener(onClickSaveButton);
        addButton = findViewById(R.id.past_addButton);
        addButton.setOnClickListener(onClickaddButton);
    }

    private Button.OnClickListener onClickSaveButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PastBrewsActivity.this, CurrentBrewActivity.class);
            startActivity(intent);
        }
    };

    private FloatingActionButton.OnClickListener onClickaddButton = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {

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