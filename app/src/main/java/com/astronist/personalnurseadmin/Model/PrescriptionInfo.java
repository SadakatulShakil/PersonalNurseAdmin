package com.astronist.personalnurseadmin.Model;

import java.io.Serializable;

public class PrescriptionInfo implements Serializable {
    private String userId;
    private String pushId;
    private String prescriptionUrl;
    private String date;
    private String time;
    private String type;

    public PrescriptionInfo() {
    }

    public PrescriptionInfo(String userId, String pushId, String prescriptionUrl, String date, String time, String type) {
        this.userId = userId;
        this.pushId = pushId;
        this.prescriptionUrl = prescriptionUrl;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getPrescriptionUrl() {
        return prescriptionUrl;
    }

    public void setPrescriptionUrl(String prescriptionUrl) {
        this.prescriptionUrl = prescriptionUrl;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PrescriptionInfo{" +
                "userId='" + userId + '\'' +
                ", pushId='" + pushId + '\'' +
                ", prescriptionUrl='" + prescriptionUrl + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
