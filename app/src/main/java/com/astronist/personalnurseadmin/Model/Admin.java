package com.astronist.personalnurseadmin.Model;

import java.io.Serializable;

public class Admin implements Serializable {
    private String pushId;
    private String phoneNo;
    private String emailId;
    private String shopName;
    private String shopArea;
    private String password;

    public Admin() {
    }

    public Admin(String pushId, String phoneNo, String emailId, String shopName, String shopArea, String password) {
        this.pushId = pushId;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.shopName = shopName;
        this.shopArea = shopArea;
        this.password = password;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopArea() {
        return shopArea;
    }

    public void setShopArea(String shopArea) {
        this.shopArea = shopArea;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "pushId='" + pushId + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", emailId='" + emailId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopArea='" + shopArea + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
