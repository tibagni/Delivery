package com.delivery.order;

import java.util.HashMap;
import java.util.Map;

public class OrderItem {

    private int mId;
    private int mProductId;
    private long mOrderId;
    private double mPrice;
    private int mSize;

    public int getSize() {
        return mSize;
    }
    public void setSize(int size) {
        this.mSize = size;
    }

    // Atributos utilizados enquanto o pedido esta na memoria
    private Map<Integer, String> mFlavours;
    private Map<Integer, String> mOptionals;
    private int mTemporaryId;

    //Usado na Ui para mostrar a descricao do item
    private String mDescription;
    private String mCachedSizeName;


    public int getTemporaryId() {
        return mTemporaryId;
    }
    public void setmTeporaryId(int temporaryId) {
        mTemporaryId = temporaryId;
    }

    public int getId() {
        return mId;
    }
    public void setId(int id) {
        this.mId = id;
    }
    public int getProductId() {
        return mProductId;
    }
    public void setProductId(int productId) {
        this.mProductId = productId;
    }
    public long getOrderId() {
        return mOrderId;
    }
    public void setOrderId(long orderId) {
        this.mOrderId = orderId;
    }
    public double getPrice() {
        return mPrice;
    }
    public void setPrice(double price) {
        this.mPrice = price;
    }
    public String getCachedSizeName() {
        return mCachedSizeName;
    }
    public void setCachedSizeName(String cachedSizeName) {
        this.mCachedSizeName = cachedSizeName;
    }
    public Map<Integer, String> getFlavours() {
        return mFlavours;
    }
    public void setFlavours(Map<Integer, String> flavours) {
        this.mFlavours = flavours;
    }
    public Map<Integer, String> getOptionals() {
        return mOptionals;
    }
    public void setOptionals(Map<Integer, String> optionals) {
        this.mOptionals = optionals;
    }

    public void addNewFlavour(int flavourId, String flavourName) {
        if (mFlavours == null) {
            mFlavours = new HashMap<Integer, String>();
        }
        mFlavours.put(flavourId, flavourName);
    }

    public void addNewOptional(int optionalId, String optionalName) {
        if (mOptionals == null) {
            mOptionals = new HashMap<Integer, String>();
        }
        mOptionals.put(optionalId, optionalName);
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
