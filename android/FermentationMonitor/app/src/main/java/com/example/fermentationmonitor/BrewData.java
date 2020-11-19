package com.example.fermentationmonitor;

public class BrewData {
    private String date;
    private String time;
    private String specificGravity;
    private String tempOfLiquid;

    public BrewData() {}

    public BrewData(String date, String time, String specificGravity, String tempOfLiquid) {
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

    public String getSpecificGravity() {
        return specificGravity;
    }

    public void setSpecificGravity(String specificGravity) {
        this.specificGravity = specificGravity;
    }

    public String getTempOfLiquid() {
        return tempOfLiquid;
    }

    public void setTempOfLiquid(String tempOfLiquid) {
        this.tempOfLiquid = tempOfLiquid;
    }
}
