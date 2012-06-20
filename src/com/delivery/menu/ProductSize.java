package com.delivery.menu;

public class ProductSize {

    private int mId;
    private String mName;
    private int mProductId;

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

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mId;
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
        result = prime * result + mProductId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ProductSize other = (ProductSize) obj;
        if (mId != other.mId)
            return false;
        if (mName == null) {
            if (other.mName != null)
                return false;
        } else if (!mName.equals(other.mName))
            return false;
        if (mProductId != other.mProductId)
            return false;
        return true;
    }
}
