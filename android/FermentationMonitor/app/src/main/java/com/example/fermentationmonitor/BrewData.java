package com.example.fermentationmonitor;

public class BrewData {
    private String date;
    private String time;
    private double specificGravity;
    private double tempOfLiquid;

    public BrewData() {}

    public BrewData(String date, String time, double specificGravity, double tempOfLiquid) {
        this.date = date;
        this.time = time;
        this.specificGravity = specificGravity;
        this.tempOfLiquid = tempOfLiquid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getSpecificGravity() {
        return specificGravity;
    }

    public void setSpecificGravity(double specificGravity) {
        this.specificGravity = specificGravity;
    }

    public double getTempOfLiquid() {
        return tempOfLiquid;
    }

    public void setTempOfLiquid(double tempOfLiquid) {
        this.tempOfLiquid = tempOfLiquid;
    }
}
