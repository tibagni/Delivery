package com.delivery.android.provider;


public class Order {
	private long mRemoteId;
	private Address mRemoteAddress;
	private boolean mCharge;
	private double mChange;
	private String mDescription;

	public long getRemoteId() {
		return mRemoteId;
	}

	public void setRemoteId(long remoteId) {
		this.mRemoteId = remoteId;
	}

	public Address getRemoteAddress() {
		return mRemoteAddress;
	}

	public void setRemoteAddress(Address remoteAddress) {
		this.mRemoteAddress = remoteAddress;
	}

	public boolean isCharge() {
		return mCharge;
	}

	public void setCharge(boolean charge) {
		this.mCharge = charge;
	}

	public double getChange() {
		return mChange;
	}

	public void setChange(double change) {
		this.mChange = change;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	@Override
	public String toString() {
		return "RemoteOrder [mRemoteId=" + mRemoteId + ", mRemoteAddress="
				+ mRemoteAddress + ", mCharge=" + mCharge + ", mChange="
				+ mChange + ", mDescription=" + mDescription + "]";
	}
}
