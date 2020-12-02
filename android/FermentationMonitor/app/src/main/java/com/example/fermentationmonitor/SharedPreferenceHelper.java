package com.example.fermentationmonitor;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    private SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("BatchPreference", Context.MODE_PRIVATE );
    }

    public void saveBatchName(Batch batch) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("batchName", batch.getBatchName() );
        editor.commit();
    }

    public void saveBatchId(Batch batch) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("batchId", batch.getId() );
        editor.commit();
    }

    public void saveYeastType(Batch batch) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("yeastType", batch.getYeastType() );
        editor.commit();
    }

    public void saveEndDate(Batch batch) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("endDate", batch.getEndDate());
        editor.commit();
    }

    public void saveDeviceId(Batch batch) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deviceId", batch.getDeviceId());
        editor.commit();
    }

    public String getBatchName() {
        return sharedPreferences.getString("batchName", null);
    }

    public String getBatchId() {
        return sharedPreferences.getString("batchId", null);
    }

    public String getYeastType() {
        return sharedPreferences.getString("yeastType", null);
    }

    public String getEndDate() {
        return sharedPreferences.getString("endDate", null);
    }

    public String getDeviceId() {
        return sharedPreferences.getString("deviceId", null);
    }
}
