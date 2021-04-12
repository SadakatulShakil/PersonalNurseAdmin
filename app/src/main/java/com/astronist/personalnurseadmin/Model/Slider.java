package com.astronist.personalnurseadmin.Model;

import java.io.Serializable;

public class Slider implements Serializable {
    private String sliderImageUrl;
    private String uploadTime;
    private String uploadKey;

    public Slider() {
    }

    public Slider(String sliderImageUrl, String uploadTime, String uploadKey) {
        this.sliderImageUrl = sliderImageUrl;
        this.uploadTime = uploadTime;
        this.uploadKey = uploadKey;
    }

    public String getSliderImageUrl() {
        return sliderImageUrl;
    }

    public void setSliderImageUrl(String sliderImageUrl) {
        this.sliderImageUrl = sliderImageUrl;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadKey() {
        return uploadKey;
    }

    public void setUploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
    }
}
