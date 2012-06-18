package com.delivery.menu;

import java.util.List;

public class Flavour {

    private int mId;
    private String mName;
    private String mDescription;
    private String mPicturePath;

    private int mProductId;

    private List<Price> mPrices;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPicturePath() {
        return mPicturePath;
    }

    public void setPicturePath(String picturePath) {
        mPicturePath = picturePath;
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    public List<Price> getPrices() {
        return mPrices;
    }

    public void setPrices(List<Price> prices) {
        mPrices = prices;
    }
}
