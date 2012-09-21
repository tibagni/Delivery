package com.delivery.android.activity;

import android.os.Handler;
import android.os.Looper;

import com.delivery.android.sync.SyncManager.SyncListener;

public abstract class SyncListenerUiWrapper implements SyncListener {
	private final Handler mUiHandler;

	public SyncListenerUiWrapper() {
		mUiHandler = new Handler(Looper.getMainLooper());
	}

	@Override
	public void onSyncStarted() {
		mUiHandler.post(new Runnable() {
			@Override public void run() {
				onSyncStartedUi();
			}
		});
	}

	@Override
	public void onSyncFinished(final int status) {
		mUiHandler.post(new Runnable() {
			@Override public void run() {
				onSyncFinishedUi(status);
			}
		});
	}

	@Override
	public void onSyncPerforming() {
		mUiHandler.post(new Runnable() {
			@Override public void run() {
				onSyncPerformingUi();
			}
		});
	}

	public abstract void onSyncStartedUi();

	public abstract void onSyncFinishedUi(int status);

	public abstract void onSyncPerformingUi();

}
