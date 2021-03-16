package com.astronist.personalnurseadmin.Model;

public class SalesShop {
    private String salesPhone;
    private String salesEmail;
    private String salesArea;
    private String selected;

    public SalesShop() {
    }

    public SalesShop(String salesPhone, String salesEmail, String salesArea, String selected) {
        this.salesPhone = salesPhone;
        this.salesEmail = salesEmail;
        this.salesArea = salesArea;
        this.selected = selected;
    }

    public String getSalesPhone() {
        return salesPhone;
    }

    public void setSalesPhone(String salesPhone) {
        this.salesPhone = salesPhone;
    }

    public String getSalesEmail() {
        return salesEmail;
    }

    public void setSalesEmail(String salesEmail) {
        this.salesEmail = salesEmail;
    }

    public String getSalesArea() {
        return salesArea;
    }

    public void setSalesArea(String salesArea) {
        this.salesArea = salesArea;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
