package com.delivery.order;

public class OrderItemRelOptional {

	private long mOrderId;
	private int mOrderItemId;
	private int mOptionalId;
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
	public int getOptionalId() {
		return mOptionalId;
	}
	public void setOptionalId(int mOptionalId) {
		this.mOptionalId = mOptionalId;
	}
}
