package com.example.fermentationmonitor;

public class FermentationData {
    private String id;
    private double alcoholLevel;
    private double density;
    private double temperature;
    private String userid;

    public FermentationData() {

    }

    public FermentationData(String id, String userid, double alcoholLevel, double density, double temperature) {
        this.id = id;
        this.alcoholLevel = alcoholLevel;
        this.density = density;
        this.temperature = temperature;
        this.userid = userid;
    }

    public double getAlcoholLevel() {
        return alcoholLevel;
    }

    public void setAlcoholLevel(double alcoholLevel) {
        this.alcoholLevel = alcoholLevel;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
