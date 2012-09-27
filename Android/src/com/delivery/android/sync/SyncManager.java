package com.delivery.android.sync;

import java.util.ArrayList;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.delivery.android.account.AccountInfo;
import com.delivery.android.provider.OrderContentProvider;

public class SyncManager {
	private final ArrayList<SyncListener> mListeners;

	private static SyncManager sInstance;
	private final Context mContext;

	private boolean mSyncing;

	private SyncManager(Context context) {
		mListeners = new ArrayList<SyncListener>();
		mContext = context.getApplicationContext();
	}

	public static synchronized SyncManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SyncManager(context);
		}
		return sInstance;
	}

	public synchronized void registerListener(SyncListener listener) {
		mListeners.add(listener);
		if (mSyncing) {
			// If there is a syncing going on we need to notify this listener
			listener.onSyncPerforming();
		}
	}

	public synchronized void unregisterListener(SyncListener listener) {
		mListeners.remove(listener);
	}

	/*package*/ synchronized void notifySyncStarted() {
		mSyncing = true;
		for (SyncListener listener : mListeners) {
			listener.onSyncStarted();
		}
	}

	/*package*/ synchronized void notifySyncFinished(int status) {
		for (SyncListener listener : mListeners) {
			listener.onSyncFinished(status);
		}
		mSyncing = false;
	}

	public void requestSync(boolean manual) {
    	Account account = AccountInfo.getConfiguredAccount(mContext);
    	Bundle extras = new Bundle();
    	extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, manual);
    	ContentResolver.requestSync(account, OrderContentProvider.AUTHORITY, extras);
	}

	public static interface SyncListener {
		void onSyncStarted();
		void onSyncFinished(int status);
		void onSyncPerforming();
	}

	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_AUTH_ERROR = 1;
	public static final int STATUS_CANCELED = 2;
	public static final int STATUS_IO_ERROR = 3;
	public static final int STATUS_PARSER_ERROR = 4;

}
