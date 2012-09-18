package com.delivery.android.sync;

public class RemoteOrder {
	long mRemoteId;
	RemoteAddress mRemoteAddress;
	boolean mCharge;
	double mChange;
	String mDescription;

	@Override
	public String toString() {
		return "RemoteOrder [mRemoteId=" + mRemoteId + ", mRemoteAddress="
				+ mRemoteAddress + ", mCharge=" + mCharge + ", mChange="
				+ mChange + ", mDescription=" + mDescription + "]";
	}
}
