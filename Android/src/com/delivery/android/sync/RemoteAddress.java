package com.delivery.android.sync;

public class RemoteAddress {
	String mStreet;
	int mNumber;
	String mComplement;
	String mDistrict;
	String mCity;
	String mUf;
	String mZip;


	@Override
	public String toString() {
		return "RemoteAddress [mStreet=" + mStreet + ", mNumber=" + mNumber
				+ ", mComplement=" + mComplement + ", mDistrict=" + mDistrict
				+ ", mCity=" + mCity + ", mUf=" + mUf + ", mZip=" + mZip + "]";
	}


}
