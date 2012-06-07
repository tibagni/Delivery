package com.delivery.menu;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int mId;
    private int mCategory;
    private String mName;
    private String mDecription;
    private String mPicturePath;

    private int mOptionalsPerOrder;
    private int mFlavoursPerOrder;

    private List<ProductSize> mSizes;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int category) {
        mCategory = category;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDecription;
    }

    public void setDecription(String decription) {
        mDecription = decription;
    }

    public String getPicturePath() {
        return mPicturePath;
    }

    public void setPicturePath(String picturePath) {
        mPicturePath = picturePath;
    }

    public int getOptionalsPerOrder() {
        return mOptionalsPerOrder;
    }

    public void setOptionalsPerOrder(int optionalsInOrder) {
        mOptionalsPerOrder = optionalsInOrder;
    }

    public int getFlavoursPerOrder() {
        return mFlavoursPerOrder;
    }

    public void setFlavoursPerOrder(int flavoursInOrder) {
        mFlavoursPerOrder = flavoursInOrder;
    }

    public List<ProductSize> getSizesAvailable() {
        return mSizes;
    }

    public void setSizesAvailable(List<ProductSize> sizes) {
        mSizes = sizes;
    }

    public void addSize(ProductSize newSize) {
        if (mSizes == null) {
            mSizes = new ArrayList<ProductSize>();
        }
        mSizes.add(newSize);
    }
}
