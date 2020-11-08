package com.example.fermentationmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class loginActivity extends AppCompatActivity {

    protected TextView emailloginInput;
    protected TextView passwordloginInput;
    protected Button loginButton;
    protected ProgressBar progressBar;

    protected FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        setupUI();
    }

    protected void setupUI() {
        getSupportActionBar().setTitle("");
        emailloginInput = findViewById(R.id.emailloginInput);
        passwordloginInput = findViewById(R.id.passwordloginInput);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBarSignup);
        progressBar.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(loginButtonListener);
    }

    private View.OnClickListener loginButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

            if(profileExists(emailloginInput.getText().toString(), passwordloginInput.getText().toString())){
                Intent intent = new Intent(loginActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(loginActivity.this,"Account does not exist, try again", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean profileExists(String name, String password){
        //check if the username and password match
        if(/*profile exists*/ true){  //DELETE change this to have it working and not always true
            return true;
        }
        else{
            return false;
        }
    }

}