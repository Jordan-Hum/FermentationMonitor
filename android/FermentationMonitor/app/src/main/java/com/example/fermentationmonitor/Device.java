package com.example.fermentationmonitor;

public class Device {
    private String id;
    private String currentBatchId;
    private String userId;

    public Device() {
    }

    public Device(String currentBatchId, String userId) {
        this.currentBatchId = currentBatchId;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrentBatchId() {
        return currentBatchId;
    }

    public void setCurrentBatchId(String currentBatchId) {
        this.currentBatchId = currentBatchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
