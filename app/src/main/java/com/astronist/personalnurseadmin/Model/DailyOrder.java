package com.astronist.personalnurseadmin.Model;

import java.io.Serializable;

public class DailyOrder implements Serializable {
    private String userId;
    private String pushId;
    private String userName;
    private String userPhone;
    private String userAddress1;
    private String userAddress2;
    private String userRoadNo;
    private String productTitle;
    private String productQuantity;
    private double totalPrice;
    private String productCategory;
    private String upTime;
    private String update;
    private String paymentStatus;
    private String status;

    public DailyOrder() {
    }

    public DailyOrder(String userId, String pushId, String userName, String userPhone,
                      String userAddress1, String userAddress2, String userRoadNo,
                      String productTitle, String productQuantity, double totalPrice,
                      String productCategory, String upTime, String update, String paymentStatus, String status) {
        this.userId = userId;
        this.pushId = pushId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAddress1 = userAddress1;
        this.userAddress2 = userAddress2;
        this.userRoadNo = userRoadNo;
        this.productTitle = productTitle;
        this.productQuantity = productQuantity;
        this.totalPrice = totalPrice;
        this.productCategory = productCategory;
        this.upTime = upTime;
        this.update = update;
        this.paymentStatus = paymentStatus;
        this.status = status;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress1() {
        return userAddress1;
    }

    public void setUserAddress1(String userAddress1) {
        this.userAddress1 = userAddress1;
    }

    public String getUserAddress2() {
        return userAddress2;
    }

    public void setUserAddress2(String userAddress2) {
        this.userAddress2 = userAddress2;
    }

    public String getUserRoadNo() {
        return userRoadNo;
    }

    public void setUserRoadNo(String userRoadNo) {
        this.userRoadNo = userRoadNo;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DailyOrder{" +
                "userId='" + userId + '\'' +
                ", pushId='" + pushId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userAddress1='" + userAddress1 + '\'' +
                ", userAddress2='" + userAddress2 + '\'' +
                ", userRoadNo='" + userRoadNo + '\'' +
                ", productTitle='" + productTitle + '\'' +
                ", productQuantity='" + productQuantity + '\'' +
                ", totalPrice=" + totalPrice +
                ", productCategory='" + productCategory + '\'' +
                ", upTime='" + upTime + '\'' +
                ", update='" + update + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
