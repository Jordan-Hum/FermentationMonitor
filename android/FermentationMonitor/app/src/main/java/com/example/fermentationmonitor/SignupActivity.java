package com.example.fermentationmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "Signup";
    protected Button loginButton;
    protected TextView nameInput;
    protected TextView emailInput;
    protected TextView passwordInput;
    protected TextView confirmpasswordInput;
    protected Button continueButton;
    protected ProgressBar progressBar;

    protected FirebaseAuth fAuth;
    protected FirebaseFirestore fStore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
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
            Intent intent = new Intent(SignupActivity.this, PastBrewsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupUI(){
        getSupportActionBar().setTitle("");
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmpasswordInput = findViewById(R.id.confirmPasswordInput);
        continueButton = findViewById(R.id.continueButton);
        loginButton = findViewById(R.id.signinButton);
        progressBar = findViewById(R.id.progressBarSignup);

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

    private Button.OnClickListener continueButtonListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            registerUser();
        }
    };

    private void registerUser() {
        final String email = emailInput.getText().toString().trim();
        final String password = passwordInput.getText().toString().trim();
        final String name = nameInput.getText().toString().trim();

        //Validate User
        if(TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            return;
        }
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

        //Register User
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "User account created" , Toast.LENGTH_SHORT).show();

                    //Store User in the database
                    final String userID = fAuth.getCurrentUser().getUid();
                    DocumentReference docRef = fStore.collection("users").document(userID);
                    docRef.set(new User(userID, name, email)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User " + userID + " successfully created!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

                    //Send to Main Activity
                    Intent intent = new Intent(SignupActivity.this, PastBrewsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}