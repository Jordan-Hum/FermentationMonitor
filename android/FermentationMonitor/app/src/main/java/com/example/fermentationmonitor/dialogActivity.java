package com.example.fermentationmonitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class dialogActivity extends AppCompatDialogFragment {

    static String TAG = "dialogActivity";

    protected TextView brewTitleInput;
    protected TextView yeastTypeInput;
    protected TextView idealSgInput;

    private String userID;
    private String deviceID;
    private List<String> devices = new ArrayList<>();

    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

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

        getDevice();
        
        builder.setTitle("Add New Brew");
        builder.setView(view);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
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
                if(deviceID == null) {
                    Toast.makeText(getActivity(), "Device needs to be registered before creating a batch", Toast.LENGTH_LONG).show();
                    return;
                }

                Batch batch = new Batch(brewTitle, date, "-", userID, yeastType, idealSg, "", deviceID);
                String batchId = dbRef.push().getKey();
                dbRef.child(batchId).setValue(batch);

                db.getReference("Devices/").child(deviceID).child("currentBatchId").setValue(batchId);

                registerBrewToMessageTopic(batchId);
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void getDevice() {
        db.getReference("Devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Device device = dataSnapshot.getValue(Device.class);
                        if(device.getUserId().equals(userID)) {
                            device.setId(dataSnapshot.getKey());
                            deviceID = device.getId();
                            Log.d("ABC", deviceID + "");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void registerBrewToMessageTopic(final String brewId) {
        Log.d(TAG,"subscribing....");
        FirebaseMessaging.getInstance().subscribeToTopic(brewId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfully Subscribed to "+brewId;
                        if (!task.isSuccessful()) {
                            msg = "Failed to Subscribe to "+ brewId;
                        }
                        Log.d(TAG, msg);
                        //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
