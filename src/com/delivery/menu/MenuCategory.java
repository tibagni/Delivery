package com.delivery.menu;

import java.util.List;

import com.delivery.util.SQLUtils;

public class MenuCategory {
    public static final int INVALID_ID = (int) SQLUtils.INVALID_ID;

    private int mCategoryId;
    private String mName;
    private int mParentId = INVALID_ID;
    private List<MenuCategory> mSubCategories;
    private List<Product> mProducts;

    public MenuCategory() {
        mCategoryId = INVALID_ID;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public void setSubCategories(List<MenuCategory> subCategories) {
        mSubCategories = subCategories;
    }

    public List<MenuCategory> getSubCategories() {
        return mSubCategories;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int parentId) {
        mParentId = parentId;
    }
}
