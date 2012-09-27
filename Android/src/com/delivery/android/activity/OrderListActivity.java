package com.delivery.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.delivery.android.R;
import com.delivery.android.sync.SyncManager;
import com.delivery.android.sync.SyncManager.SyncListener;

public class OrderListActivity extends FragmentActivity
        implements OrderListFragment.Callback {

    private boolean mTwoPane;
    private MenuItem mSyncButton;

    private final SyncListener mSyncListener = new UiSyncListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_order_list);

        if (findViewById(R.id.order_detail_container) != null) {
            mTwoPane = true;
            ((OrderListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.order_list))
                    .setActivateOnItemClick(true);
        }
		setProgressBarIndeterminateVisibility(false);
    }

    @Override
    protected void onResume() {
    	super.onResume();
        SyncManager.getInstance(this).registerListener(mSyncListener);
    }

    @Override
    protected void onStop() {
    	super.onStop();
        SyncManager.getInstance(this).unregisterListener(mSyncListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_list_options_menu, menu);
        mSyncButton = menu.getItem(0);
        return true;
    }

	private void showSelectServerDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SelectServerDialog editNameDialog = new SelectServerDialog();
        editNameDialog.show(fm, "fragment_select_server");
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.select_server:
	        	showSelectServerDialog();
	            return true;
	        case R.id.sync:
	        	SyncManager.getInstance(this).requestSync(true);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
    }

    @Override
    public void onItemSelected(long id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putLong(OrderDetailFragment.ARG_ITEM_ID, id);
            OrderDetailFragment fragment = new OrderDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.order_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, OrderDetailActivity.class);
            detailIntent.putExtra(OrderDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

	private void handleSyncError(int status) {
		switch (status) {
			case SyncManager.STATUS_AUTH_ERROR:
				Toast.makeText(this, R.string.authentication_error_msg, Toast.LENGTH_LONG).show();
				break;
			case SyncManager.STATUS_CANCELED:
				Toast.makeText(this, R.string.canceled_error_msg, Toast.LENGTH_LONG).show();
				break;
			case SyncManager.STATUS_IO_ERROR:
				Toast.makeText(this, R.string.io_error_msg, Toast.LENGTH_LONG).show();
				break;
			case SyncManager.STATUS_PARSER_ERROR:
				Toast.makeText(this, R.string.parser_error_msg, Toast.LENGTH_LONG).show();
				break;
		}
	}

	private void syncStarted() {
		setLoadingState();
	}

	private void syncFinished(int status) {
		exitLoadingState();
		handleSyncError(status);
	}

	private void syncPerforming() {
		setLoadingState();

	}

	private void setLoadingState() {
		if (mSyncButton != null) {
			mSyncButton.setVisible(false);
		}
		setProgressBarIndeterminateVisibility(true);
	}

	private void exitLoadingState() {
		if (mSyncButton != null) {
			mSyncButton.setVisible(true);
		}
		setProgressBarIndeterminateVisibility(false);
	}


	private class UiSyncListener extends SyncListenerUiWrapper {
		@Override
		public void onSyncStartedUi() {
			syncStarted();
		}
		@Override
		public void onSyncFinishedUi(int status) {
			syncFinished(status);
		}
		@Override
		public void onSyncPerformingUi() {
			syncPerforming();
		}
	}
}
