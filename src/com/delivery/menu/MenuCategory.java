package com.delivery.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuCategory {
    public static final int INVALID_ID = 0;

    private int mCategoryId;
    private String mName;
    private int mParentId = INVALID_ID;
    private ArrayList<MenuCategory> mSubCategories;
    // TODO lista de produtos

    public MenuCategory() {
        mCategoryId = INVALID_ID;
        mSubCategories = new ArrayList<MenuCategory>();
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public void addSubCategory(MenuCategory subCategory) {
        mSubCategories.add(subCategory);
    }

    public void addAllSubCategories(List<MenuCategory> subCategories) {
        mSubCategories.addAll(subCategories);
    }

    public List<MenuCategory> getSubCategories() {
        return mSubCategories;
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
