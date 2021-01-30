package com.astronist.personalnurseadmin.Model;

public class Address {

    private String userId;
    private String name;
    private String phone;
    private String addressLine1;
    private String getAddressLine2;
    private String roadNo;

    public Address() {
    }

    public Address(String userId, String name, String phone, String addressLine1, String getAddressLine2, String roadNo) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.getAddressLine2 = getAddressLine2;
        this.roadNo = roadNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getGetAddressLine2() {
        return getAddressLine2;
    }

    public void setGetAddressLine2(String getAddressLine2) {
        this.getAddressLine2 = getAddressLine2;
    }

    public String getRoadNo() {
        return roadNo;
    }

    public void setRoadNo(String roadNo) {
        this.roadNo = roadNo;
    }

    @Override
    public String toString() {
        return "Address{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", getAddressLine2='" + getAddressLine2 + '\'' +
                ", roadNo='" + roadNo + '\'' +
                '}';
    }
}
