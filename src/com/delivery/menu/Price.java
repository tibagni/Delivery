package com.delivery.menu;

public class Price {

    private int mFlavourId;
    private int mSizeId;

    private double mPrice;

    private String mCachedSizeName;

    public int getFlavourId() {
        return mFlavourId;
    }

    public void setFlavourId(int flavourId) {
        mFlavourId = flavourId;
    }

    public int getSizeId() {
        return mSizeId;
    }

    public void setSizeId(int sizeId) {
        mSizeId = sizeId;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public String getCachedSizeName() {
        return mCachedSizeName;
    }

    public void setCachedSizeName(String sizeName) {
        mCachedSizeName = sizeName;
    }

}
