package com.example.fermentationmonitor;

public class BrewData {
    private String date;
    private String time;
    private String density;
    private String temperature;

    public BrewData() {}

    public BrewData(String date, String time, String density, String temperature) {
        this.date = date;
        this.time = time;
        this.density = density;
        this.temperature = temperature;
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

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
