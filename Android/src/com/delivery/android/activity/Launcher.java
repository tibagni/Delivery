package com.delivery.android.activity;

import android.app.Activity;
import android.content.Intent;

import com.delivery.android.account.AccountInfo;
import com.delivery.android.account.AuthenticatorActivity;

public class Launcher extends Activity {

	private static final int CREATE_ACCOUNT_ACTIVITY = 1;

	@Override
	protected void onResume() {
		super.onResume();

        if (!AccountInfo.hasAccount(this)) {
        	Intent intent = new Intent(this, AuthenticatorActivity.class);
        	startActivityForResult(intent, CREATE_ACCOUNT_ACTIVITY);
        } else {
        	Intent intent = new Intent(this, OrderListActivity.class);
        	startActivity(intent);
        	finish();
        }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case CREATE_ACCOUNT_ACTIVITY:
				if(resultCode != RESULT_OK) {
					finish();
				}
		}
	}

}
