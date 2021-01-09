package com.example.shin.Model;

public class Category {
    private String CategoryName;
    private int  CategoryIcon;

    public Category(int categoryIcon, String categoryName) {
        CategoryIcon = categoryIcon;
        CategoryName = categoryName;
    }

    public int getCategoryIcon() {
        return CategoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        CategoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return CategoryName;
    }


    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
