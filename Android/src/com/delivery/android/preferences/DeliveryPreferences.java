package com.delivery.android.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class DeliveryPreferences {

	private static DeliveryPreferences sInstance;
	private final Context mContext;

	private final SharedPreferences mSharedPreferences;

	private static final String PREFS_NAME = "DeliveryPreferences";

	private static final String PREF_SERVER_ADDR = "server_addr";
	private static final String PREF_SERVER_ADDR_DEFAULT = "http://10.0.2.2:8080/Delivery";

	public static synchronized DeliveryPreferences getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DeliveryPreferences(context);
		}
		return sInstance;
	}

	private DeliveryPreferences(Context c) {
		mContext = c.getApplicationContext();
		mSharedPreferences = mContext.getSharedPreferences(PREFS_NAME, 0);
	}

	public String getOrderServerAddress() {
		return mSharedPreferences.getString(PREF_SERVER_ADDR, PREF_SERVER_ADDR_DEFAULT);
	}

	public void setOrderServerAddress(String addr) {
		mSharedPreferences.edit().putString(PREF_SERVER_ADDR, addr).apply();
	}
}
