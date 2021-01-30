package com.astronist.personalnurseadmin.Model;

import java.io.Serializable;

public class MedicineOrder implements Serializable {
    private String userId;
    private String pushId;
    private String customerName;
    private String customerPhone;
    private String addressLine1;
    private String addressLine2;
    private String roadNo;
    private String medicineList;
    private String singlePrice;
    private String totalPrice;
    private String dayCount;
    private String status;
    private String paymentMethod;
    private String orderDate;
    private String orderTime;

    public MedicineOrder() {
    }

    public MedicineOrder(String userId, String pushId, String customerName, String customerPhone,
                         String addressLine1, String addressLine2, String roadNo, String medicineList,
                         String singlePrice, String totalPrice, String dayCount, String status,
                         String paymentMethod, String orderDate, String orderTime) {
        this.userId = userId;
        this.pushId = pushId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.roadNo = roadNo;
        this.medicineList = medicineList;
        this.singlePrice = singlePrice;
        this.totalPrice = totalPrice;
        this.dayCount = dayCount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getRoadNo() {
        return roadNo;
    }

    public void setRoadNo(String roadNo) {
        this.roadNo = roadNo;
    }

    public String getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(String medicineList) {
        this.medicineList = medicineList;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "MedicineOrder{" +
                "userId='" + userId + '\'' +
                ", pushId='" + pushId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", roadNo='" + roadNo + '\'' +
                ", medicineList='" + medicineList + '\'' +
                ", singlePrice='" + singlePrice + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", dayCount='" + dayCount + '\'' +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderTime='" + orderTime + '\'' +
                '}';
    }
}
