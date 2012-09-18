package com.delivery.android.provider;

public class Address {
	private String mStreet;
	private int mNumber;
	private String mComplement;
	private String mDistrict;
	private String mCity;
	private String mUf;
	private String mZip;


	public String getStreet() {
		return mStreet;
	}


	public void setStreet(String street) {
		this.mStreet = street;
	}


	public int getNumber() {
		return mNumber;
	}


	public void setNumber(int number) {
		this.mNumber = number;
	}


	public String getComplement() {
		return mComplement;
	}


	public void setComplement(String complement) {
		this.mComplement = complement;
	}


	public String getDistrict() {
		return mDistrict;
	}


	public void setDistrict(String district) {
		this.mDistrict = district;
	}


	public String getCity() {
		return mCity;
	}


	public void setCity(String city) {
		this.mCity = city;
	}


	public String getUf() {
		return mUf;
	}


	public void setUf(String uf) {
		this.mUf = uf;
	}


	public String getZip() {
		return mZip;
	}


	public void setZip(String zip) {
		this.mZip = zip;
	}


	@Override
	public String toString() {
		return "RemoteAddress [mStreet=" + mStreet + ", mNumber=" + mNumber
				+ ", mComplement=" + mComplement + ", mDistrict=" + mDistrict
				+ ", mCity=" + mCity + ", mUf=" + mUf + ", mZip=" + mZip + "]";
	}


}
