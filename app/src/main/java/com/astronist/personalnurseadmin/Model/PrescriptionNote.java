package com.astronist.personalnurseadmin.Model;

import java.io.Serializable;

public class PrescriptionNote implements Serializable {
    private String uploadId;
    private String salesId;
    private String prescriptionImage;
    private String dateTime;
    private String medicineList;
    private String oneDayAmount;

    public PrescriptionNote() {
    }

    public PrescriptionNote(String uploadId, String salesId, String prescriptionImage, String dateTime) {
        this.uploadId = uploadId;
        this.salesId = salesId;
        this.prescriptionImage = prescriptionImage;
        this.dateTime = dateTime;
    }

    public PrescriptionNote(String uploadId, String salesId, String prescriptionImage, String dateTime, String medicineList, String oneDayAmount) {
        this.uploadId = uploadId;
        this.salesId = salesId;
        this.prescriptionImage = prescriptionImage;
        this.dateTime = dateTime;
        this.medicineList = medicineList;
        this.oneDayAmount = oneDayAmount;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getPrescriptionImage() {
        return prescriptionImage;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(String medicineList) {
        this.medicineList = medicineList;
    }

    public String getOneDayAmount() {
        return oneDayAmount;
    }

    public void setOneDayAmount(String oneDayAmount) {
        this.oneDayAmount = oneDayAmount;
    }

    @Override
    public String toString() {
        return "PrescriptionNote{" +
                "uploadId='" + uploadId + '\'' +
                ", salesId='" + salesId + '\'' +
                ", prescriptionImage='" + prescriptionImage + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", medicineList='" + medicineList + '\'' +
                ", oneDayAmount='" + oneDayAmount + '\'' +
                '}';
    }
}
