package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    protected TextView emailInput;
    protected TextView passwordInput;
    protected Button loginButton;
    protected ProgressBar progressBar;

    protected FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        isLogin();
        setupUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.INVISIBLE);
    }

    //Check if the user is already logged into an account
    private void isLogin() {
        if(fAuth.getCurrentUser() != null) {
            Intent intent = new Intent(loginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void setupUI() {
        getSupportActionBar().setTitle("");
        emailInput = findViewById(R.id.emailloginInput);
        passwordInput = findViewById(R.id.passwordloginInput);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBarLogin);

        loginButton.setOnClickListener(loginButtonListener);
    }

    private View.OnClickListener loginButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            loginUser();
        }
    };

    private void loginUser(){
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        //Validate User
        if(TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return;
        }
        if(TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            return;
        }
        if(password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Authenticate User
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(loginActivity.this, "Logged in Successfully" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(loginActivity.this, "Error: " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}