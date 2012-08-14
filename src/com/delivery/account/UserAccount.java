package com.delivery.account;

public class UserAccount {

	private String mName;
	private long mCpf;
	private String mTel;
	private String mEmail;

	private String mPassword;

	private Address mAddress;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public long getCpf() {
		return mCpf;
	}

	public void setCpf(long cpf) {
		mCpf = cpf;
	}

	public void setCpf(String cpf) {
		String strCpf = cpf.replaceAll("\\.", "").replaceAll("-", "");
		long primitiveCpf = Long.parseLong(strCpf);
		setCpf(primitiveCpf);
	}

	public String getTel() {
		return mTel;
	}

	public void setTel(String tel) {
		mTel = tel;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	public Address getAddress() {
		return mAddress;
	}

	public void setAddress(Address address) {
		mAddress = address;
	}

}
