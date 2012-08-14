package com.delivery.account;

public class Address {

	private int mId;
	private String mStreet;
	private String mZipCode;
	private String mCity;
	private String mNeighborhood;
	private int mNumber;
	private String mUF;

	private String mCompl;

	private long mUserAccountId;

	public String getStreet() {
		return mStreet;
	}

	public void setStreet(String street) {
		mStreet = street;
	}

	public String getZipCode() {
		return mZipCode;
	}

	public void setZipCode(String zipCode) {
		mZipCode = zipCode;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		mCity = city;
	}

	public String getNeighborhood() {
		return mNeighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		mNeighborhood = neighborhood;
	}

	public int getNumber() {
		return mNumber;
	}

	public void setNumber(int number) {
		mNumber = number;
	}

	public String getCompl() {
		return mCompl;
	}

	public void setCompl(String compl) {
		mCompl = compl;
	}

	public String getUF() {
		return mUF;
	}

	public void setUF(String mUF) {
		this.mUF = mUF;
	}

	public long getUserAccountId() {
		return mUserAccountId;
	}

	public void setUserAccountId(long mUserAccountId) {
		this.mUserAccountId = mUserAccountId;
	}

	public int getId() {
		return mId;
	}

	public void setId(int mId) {
		this.mId = mId;
	}
}
