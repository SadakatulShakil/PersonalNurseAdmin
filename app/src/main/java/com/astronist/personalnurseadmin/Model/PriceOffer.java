package com.astronist.personalnurseadmin.Model;

public class PriceOffer {

    private String customerId;
    private String pushId;
    private String medicineList;
    private String oneDayPrice;
    private String oneWeakPrice;
    private String fifteenDaysPrice;
    private String oneMonthPrice;
    private String date;
    private String time;

    public PriceOffer() {

    }

    public PriceOffer(String customerId, String pushId, String medicineList, String oneDayPrice,
                      String oneWeakPrice, String fifteenDaysPrice, String oneMonthPrice, String date, String time) {
        this.customerId = customerId;
        this.pushId = pushId;
        this.medicineList = medicineList;
        this.oneDayPrice = oneDayPrice;
        this.oneWeakPrice = oneWeakPrice;
        this.fifteenDaysPrice = fifteenDaysPrice;
        this.oneMonthPrice = oneMonthPrice;
        this.date = date;
        this.time = time;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(String medicineList) {
        this.medicineList = medicineList;
    }

    public String getOneDayPrice() {
        return oneDayPrice;
    }

    public void setOneDayPrice(String oneDayPrice) {
        this.oneDayPrice = oneDayPrice;
    }

    public String getOneWeakPrice() {
        return oneWeakPrice;
    }

    public void setOneWeakPrice(String oneWeakPrice) {
        this.oneWeakPrice = oneWeakPrice;
    }

    public String getFifteenDaysPrice() {
        return fifteenDaysPrice;
    }

    public void setFifteenDaysPrice(String fifteenDaysPrice) {
        this.fifteenDaysPrice = fifteenDaysPrice;
    }

    public String getOneMonthPrice() {
        return oneMonthPrice;
    }

    public void setOneMonthPrice(String oneMonthPrice) {
        this.oneMonthPrice = oneMonthPrice;
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
}
