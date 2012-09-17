package com.delivery.order;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.delivery.util.StringUtils;

public class DeliveryGuy {
	private String mName;
	private String mToken;

	private int mCode;
	private String mPassword;
	private boolean mTokenValidForQuery;

	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
	public boolean isTokenValidForQuery() {
		return mTokenValidForQuery;
	}
	public void setTokenValidForQuery(boolean isValid) {
		mTokenValidForQuery = isValid;
	}
	public String getToken() {
		if (StringUtils.isEmpty(mToken)) {
			if (StringUtils.isEmpty(mPassword) || StringUtils.isEmpty(mName)) {
				throw new IllegalStateException("Entregador sem senha ou nome");
			}
			try {
				String namePass = mPassword + mName + mCode;
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(namePass.getBytes());
				byte[] hashMd5 = md.digest();

				StringBuilder s = new StringBuilder();
				   for (int i = 0; i < hashMd5.length; i++) {
				       int parteAlta = ((hashMd5[i] >> 4) & 0xf) << 4;
				       int parteBaixa = hashMd5[i] & 0xf;
				       if (parteAlta == 0) s.append('0');
				       s.append(Integer.toHexString(parteAlta | parteBaixa));
				   }
				   mToken = s.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				mToken = mPassword + mName;
			}
		}
		return mToken;
	}
	public void setToken(String token) {
		mToken = token;
	}
	public int getCode() {
		return mCode;
	}
	public void setCode(int mCode) {
		this.mCode = mCode;
	}
	public String getPassword() {
		return mPassword;
	}
	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}
}
