package com.delivery.android.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class AccountInfo {
	public static final String ACCOUNT_TYPE = "com.delivery.android";
	public static final String AUTH_TOKEN_TYPE = "com.delivery.android";

	public static boolean hasAccount(Context context) {
    	AccountManager accountManager = AccountManager.get(context);
    	Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
    	return (accounts != null && accounts.length > 0);
	}
}
