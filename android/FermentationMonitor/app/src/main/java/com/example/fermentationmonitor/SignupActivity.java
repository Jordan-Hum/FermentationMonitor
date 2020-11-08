package com.example.fermentationmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    protected Button loginButton;
    protected TextView nameInput;
    protected TextView emailInput;
    protected TextView passwordInput;
    protected TextView confirmpasswordInput;
    protected Button continueButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupUI();

    }


    private void setupUI(){
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmpasswordInput = findViewById(R.id.confirmPasswordInput);
        continueButton = findViewById(R.id.continueButton);
        loginButton = findViewById(R.id.signinButton);

        loginButton.setOnClickListener(signinButtonListener);
        continueButton.setOnClickListener(continueButtonListener);
    }

    private Button.OnClickListener signinButtonListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view){
            Intent intent = new Intent(SignupActivity.this, loginActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener continueButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            //create account
            Toast.makeText(SignupActivity.this, "Delete when done, add signup function" , Toast.LENGTH_SHORT).show(); //DELETE
        }
    };

}