package com.delivery.admin;

import com.delivery.util.StringUtils;

/**
 * Classe usada apenas para controlar acesso ao painel de administracao
 * Os unicos componentes do objeto sao usuario e senha, apenas para identificacao
 * e autenticacao
 *
 * @author tiagobagni
 *
 */
public class Admin {

	private String mUserName;
	private String mPassword;

	public String getUserName() {
		return mUserName;
	}
	public void setUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	public String getPassword() {
		return mPassword;
	}
	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public boolean isValid() {
		if (StringUtils.isEmpty(mPassword) || StringUtils.isEmpty(mUserName)) {
			return false;
		}
		return true;
	}
}
