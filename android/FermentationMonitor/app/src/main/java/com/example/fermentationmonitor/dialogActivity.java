package com.example.fermentationmonitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dialogActivity extends AppCompatDialogFragment {

    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    protected TextView brewTitleInput;
    protected TextView yeastTypeInput;
    String userID;

    public Dialog onCreateDialog(Bundle savedInstanceState){


        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("SensorData");

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        brewTitleInput = view.findViewById(R.id.brewTitleInput);
        yeastTypeInput = view.findViewById(R.id.yeastTypeInput);

        builder.setView(view).setTitle("Add New Brew").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String brewTitle = brewTitleInput.getText().toString().trim();
                String yeastType = yeastTypeInput.getText().toString().trim();

                Batch batch = new Batch(brewTitle, "13/11/2020", "", userID, yeastType);


                dbRef.push().setValue(batch);

            }

        });


        return builder.create();
    }

}
