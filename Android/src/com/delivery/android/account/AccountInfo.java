package com.delivery.android.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class AccountInfo {
	public static final String ACCOUNT_TYPE = "com.delivery.android";
	public static final String AUTH_TOKEN_TYPE = "com.delivery.android";

	public static boolean hasAccount(Context context) {
    	return getConfiguredAccount(context) != null;
	}

	public static String buildAccountManagerName(String remoteAccountName, String userName) {
		return String.format("%s@%s", remoteAccountName, userName);
	}

	public static String getUserNameFromAccountManagerName(String amName) {
		String[] parts = amName.split("@");
		if (parts != null) {
			return parts[parts.length - 1];
		}
		return null;
	}

	public static Account getConfiguredAccount(Context context) {
    	AccountManager accountManager = AccountManager.get(context);
    	Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
    	if (accounts != null && accounts.length > 0) {
    		return accounts[0];
    	}
    	return null;

	}
}
