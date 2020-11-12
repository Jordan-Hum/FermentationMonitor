package com.example.fermentationmonitor;

public class Batch {
    private String id;
    private String batchName;
    private String startDate;
    private String endDate;
    private String userId;
    private String yeastType;

    public Batch() {}

    public Batch(String batchName, String startDate, String endDate, String userId, String yeastType) {
        this.batchName = batchName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.yeastType = yeastType;
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
}
