package com.astronist.personalnurseadmin.Model;

public class ProductInfo {
    private String title;
    private String category;
    private String subCategory;
    private String imageUrl;
    private String description;
    private int stockAvailable;
    private double regularPrice;
    private double sellingPrice;
    private int discountPercentage;
    private double actualSellingPrice;
    private String createdDate;
    private String createTime;

    public ProductInfo() {
    }

    public ProductInfo(String title, String category, String subCategory,
                       String imageUrl, String description, int stockAvailable,
                       double regularPrice, double sellingPrice, int discountPercentage,
                       double actualSellingPrice, String createdDate, String createTime) {
        this.title = title;
        this.category = category;
        this.subCategory = subCategory;
        this.imageUrl = imageUrl;
        this.description = description;
        this.stockAvailable = stockAvailable;
        this.regularPrice = regularPrice;
        this.sellingPrice = sellingPrice;
        this.discountPercentage = discountPercentage;
        this.actualSellingPrice = actualSellingPrice;
        this.createdDate = createdDate;
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(int stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getActualSellingPrice() {
        return actualSellingPrice;
    }

    public void setActualSellingPrice(double actualSellingPrice) {
        this.actualSellingPrice = actualSellingPrice;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
