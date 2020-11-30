package com.example.fermentationmonitor;

public class Batch {
    private String id;
    private String batchName;
    private String startDate;
    private String endDate;
    private String userId;
    private String yeastType;
    private String idealSg;
    private String notes;
    private String deviceId;

    public Batch() {}

    public Batch(String batchName, String startDate, String endDate, String userId, String yeastType, String idealSg, String notes, String deviceId) {
        this.batchName = batchName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.yeastType = yeastType;
        this.idealSg = idealSg;
        this.notes = notes;
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYeastType() {
        return yeastType;
    }

    public void setYeastType(String yeastType) {
        this.yeastType = yeastType;
    }

    public String getIdealSg() {
        return idealSg;
    }

    public void setIdealSg(String idealSg) {
        this.idealSg = idealSg;
    }
}
