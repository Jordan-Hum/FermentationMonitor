package com.example.fermentationmonitor;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class notesDialogActivity extends AppCompatDialogFragment {

    String batchID;

    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    protected EditText notesText;
    String userID;

    public notesDialogActivity(String batchId){
        this.batchID = batchId;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){


        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("SensorData");

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        dbRef = db.getReference("SensorData/" + batchID + "/notes");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_notes_dialog, null);

        notesText = view.findViewById(R.id.notesTextID);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notesText.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Dialog", "broken");
            }
        });


        builder.setView(view).setTitle("Add Notes").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbRef.setValue(notesText.getText().toString().trim());
            }

        });
        return builder.create();
    }

}
