package com.astronist.personalnurseadmin.Model;

public class Category {

    private String categoryName;

    public Category() {
    }

    public Category(String categoryName) {

        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
