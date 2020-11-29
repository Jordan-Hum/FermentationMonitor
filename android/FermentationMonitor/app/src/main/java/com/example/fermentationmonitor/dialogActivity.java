package com.example.fermentationmonitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class dialogActivity extends AppCompatDialogFragment {

    protected TextView brewTitleInput;
    protected TextView yeastTypeInput;
    protected TextView idealSgInput;
    protected Spinner spinner;

    private Context context;
    private String userID;
    private List<String> devices = new ArrayList<>();

    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public dialogActivity(Context context){
        this.context = context;
    }

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
        idealSgInput = view.findViewById(R.id.idealSgInput);
        spinner = view.findViewById(R.id.add_dialog_spinner);

        getDevices();

        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, devices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        builder.setView(view).setTitle("Add New Brew").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String brewTitle = brewTitleInput.getText().toString().trim();
                String yeastType = yeastTypeInput.getText().toString().trim();
                String idealSg = idealSgInput.getText().toString().trim();
                String date = getDate();

                //***Needs to be fixed***
                if(TextUtils.isEmpty(brewTitle)) {
                    brewTitleInput.setError("Batch name is required");
                    return;
                }
                if(TextUtils.isEmpty(yeastType)) {
                    yeastTypeInput.setError("Yeast Type is required");
                    return;
                }
                if(TextUtils.isEmpty(idealSg)) {
                    yeastTypeInput.setError("Ideal specific gravity is required");
                    return;
                }

                Batch batch = new Batch(brewTitle, date, "-", userID, yeastType, idealSg, "");
                dbRef.push().setValue(batch);
            }

        });
        return builder.create();
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void getDevices() {
        db.getReference("Devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    devices.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Device device = dataSnapshot.getValue(Device.class);
                        if(device.getUserId().equals(userID)) {
                            device.setId(dataSnapshot.getKey());
                            devices.add(device.getId());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
