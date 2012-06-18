package com.delivery.menu;

import java.util.List;

public class Product {
    private int mId;
    private int mCategoryId;
    private String mName;
    private String mDecription;
    private String mPicturePath;

    private int mOptionalsPerOrder;
    private int mFlavoursPerOrder;

    // Atributos relacionais (nao estao na mesma entidade no banco de dados)
    private List<ProductSize> mSizes;
    private List<Flavour> mFlavours;
    private List<Optional> mOptionals;

    public static final int MAX_SIZES = 3;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
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

    public void setDescription(String decription) {
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

    public List<Flavour> getFlavours() {
        return mFlavours;
    }

    public void setFlavours(List<Flavour> flavours) {
        mFlavours = flavours;
    }

    public List<Optional> getOptionals() {
        return mOptionals;
    }

    public void setOptionals(List<Optional> optionals) {
        mOptionals = optionals;
    }
}
