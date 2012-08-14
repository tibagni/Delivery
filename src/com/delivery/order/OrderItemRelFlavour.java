package com.delivery.order;

public class OrderItemRelFlavour {

	private long mOrderId;
	private int mOrderItemId;
	private int mFlavourId;
	public long getOrderId() {
		return mOrderId;
	}
	public void setOrderId(long mOrderId) {
		this.mOrderId = mOrderId;
	}
	public int getOrderItemId() {
		return mOrderItemId;
	}
	public void setOrderItemId(int mOrderItemId) {
		this.mOrderItemId = mOrderItemId;
	}
	public int getFlavourId() {
		return mFlavourId;
	}
	public void setFlavourId(int mFlavourId) {
		this.mFlavourId = mFlavourId;
	}

}
