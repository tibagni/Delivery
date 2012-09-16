package com.delivery.android.account;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.delivery.android.R;
import com.delivery.android.framework.accounts.AccountAuthenticatorFragmentActivity;
import com.delivery.android.preferences.DeliveryPreferences;
import com.delivery.android.provider.OrderContentProvider;

public class AuthenticatorActivity extends AccountAuthenticatorFragmentActivity {

	private static final String TAG = "AuthenticatorActivity";
	/** The Intent flag to confirm credentials. */
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";
    /** The Intent extra to store password. */
    public static final String PARAM_PASSWORD = "password";
    /** The Intent extra to store username. */
    public static final String PARAM_USERNAME = "username";
    /** The Intent extra to store username. */
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

	private DeliveryPreferences mPrefs;
	private AccountManager mAccountManager;

	private String mUsername;
	private String mPassword;

	private boolean mRequestNewAccount;
	private boolean mConfirmCredentials;

	private EditText mLoginEdit;
	private EditText mPasswordEdit;

	private UserLoginTask mAuthTask;

    private TextView mMessage;

    private ProgressDialogFragment mProgressDialog;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		if (AccountInfo.hasAccount(this)) {
			showAccountAlreadyAddedAlert();
		}

		setContentView(R.layout.authenticator_activity);
		mPrefs = DeliveryPreferences.getInstance(this);

        mAccountManager = AccountManager.get(this);
        final Intent intent = getIntent();
        mUsername = intent.getStringExtra(PARAM_USERNAME);
        mRequestNewAccount = mUsername == null;
        mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS, false);

        mLoginEdit = (EditText) findViewById(R.id.login_box);
        mPasswordEdit = (EditText) findViewById(R.id.password_box);

        mMessage = (TextView) findViewById(R.id.login_message);

        if (!TextUtils.isEmpty(mUsername)) {
        	mLoginEdit.setText(mUsername);
        }
	}

	public void handleLogin(View view) {
        if (mRequestNewAccount) {
            mUsername = mLoginEdit.getText().toString();
        }
        mPassword = mPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
        	showMessage(R.string.incomplete_login_info);
        } else {
            mAuthTask = new UserLoginTask();
            mAuthTask.execute();
        }

	}

    /**
     * Called when response is received from the server for confirm credentials
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller.
     *
     * @param result the confirmCredentials result.
     */
    private void finishConfirmCredentials(boolean result) {
        Log.i(TAG, "finishConfirmCredentials()");
        final Account account = new Account(mUsername, AccountInfo.ACCOUNT_TYPE);
        mAccountManager.setPassword(account, mPassword);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. We store the
     * authToken that's returned from the server as the 'password' for this
     * account - so we're never storing the user's actual password locally.
     *
     * @param result the confirmCredentials result.
     */
    private void finishLogin(String authToken) {
        Log.i(TAG, "finishLogin()");
        final Account account = new Account(mUsername, AccountInfo.ACCOUNT_TYPE);
        if (mRequestNewAccount) {
            mAccountManager.addAccountExplicitly(account, mPassword, null);
        } else {
            mAccountManager.setPassword(account, mPassword);
        }
        ContentResolver.setIsSyncable(account, OrderContentProvider.AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, OrderContentProvider.AUTHORITY, true);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountInfo.ACCOUNT_TYPE);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

	public void handleCancel(View view) {
        setResult(RESULT_CANCELED, null);
        finish();
	}

	private void showMessage(int resId) {
		showMessage(getString(resId));
	}

	private void showMessage(CharSequence message) {
		if (mMessage.getVisibility() != View.VISIBLE) {
			mMessage.setVisibility(View.VISIBLE);
		}
		mMessage.setText(message);
	}

	private void hideMessage() {
		if (mMessage.getVisibility() == View.VISIBLE) {
			mMessage.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.login_options_menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.select_server:
	        	showSelectServerDialog();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void showSelectServerDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SelectServerDialog editNameDialog = new SelectServerDialog();
        editNameDialog.show(fm, "fragment_select_server");
	}

	private void showAccountAlreadyAddedAlert() {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogFragment alreadyAddedDialog = new AlertDialogFragment();
        alreadyAddedDialog.show(fm, "fragment_alert_already_added");
	}

    private void showProgress() {
        // We save off the progress dialog in a field so that we can dismiss
        // it later. We can't just call dismissDialog(0) because the system
        // can lose track of our dialog if there's an orientation change.
        FragmentManager fm = getSupportFragmentManager();
        mProgressDialog = new ProgressDialogFragment();
        mProgressDialog.show(fm, "fragment_progress");
	}

    /**
     * Hides the progress UI for a lengthy operation.
     */
    private void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

	private void onFinishEditDialog(String textEntered) {
		mPrefs.setOrderServerAddress(textEntered);
		String not = getString(R.string.selected_server_msg, textEntered);
		Toast.makeText(this, not, Toast.LENGTH_SHORT).show();
	}

    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param authToken the authentication token returned by the server, or NULL if
     *            authentication failed.
     */
    private void onAuthenticationResult(String authToken) {

        boolean success = ((authToken != null) && (authToken.length() > 0));
        Log.i(TAG, "onAuthenticationResult(" + success + ")");

        // Our task is complete, so clear it out
        mAuthTask = null;

        // Hide the progress dialog
        hideProgress();

        if (success) {
            if (!mConfirmCredentials) {
                finishLogin(authToken);
            } else {
                finishConfirmCredentials(success);
            }
        } else {
            Log.e(TAG, "onAuthenticationResult: failed to authenticate");
            if (mRequestNewAccount) {
                // "Please enter a valid username/password.
            	showMessage(R.string.incorrect_login_info);
            } else {
                // "Please enter a valid password." (Used when the
                // account is already in the database but the password
                // doesn't work.)
            	showMessage(R.string.incorrect_login_info);
            }
        }
    }

    private void onIOError() {
        Log.i(TAG, "onIOError()");

        // Our task is complete, so clear it out
        mAuthTask = null;

        // Hide the progress dialog
        hideProgress();
    	showMessage(R.string.connection_error_message);
    }

    private void onAuthenticationCancel() {
        Log.i(TAG, "onAuthenticationCancel()");

        // Our task is complete, so clear it out
        mAuthTask = null;

        // Hide the progress dialog
        hideProgress();
    }

	private class SelectServerDialog extends DialogFragment implements OnEditorActionListener {
		private EditText mEditText;

	    public SelectServerDialog() {
	        // Empty constructor required for DialogFragment
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.select_server_dialog, container);
	        mEditText = (EditText) view.findViewById(R.id.server_address);
	        getDialog().setTitle(R.string.select_server_dialog_title);
	        mEditText.setOnEditorActionListener(this);

	        String currentAddress = mPrefs.getOrderServerAddress();
	        if (!TextUtils.isEmpty(currentAddress)) {
	        	mEditText.setText(currentAddress);
	        }

	        return view;
	    }

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (EditorInfo.IME_ACTION_DONE == actionId) {
	            // Return input text to activity
	            onFinishEditDialog(mEditText.getText().toString());
	            this.dismiss();
	            return true;
	        }
	        return false;
		}
	}

	private class ProgressDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
	        final ProgressDialog dialog = new ProgressDialog(AuthenticatorActivity.this);
	        dialog.setMessage(getText(R.string.authenticating));
	        dialog.setIndeterminate(true);
	        dialog.setCancelable(true);

	        return dialog;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
            Log.i(TAG, "user cancelling authentication");
            if (mAuthTask != null) {
                mAuthTask.cancel(true);
            }
		}
	}

	private class AlertDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
	        final AlertDialog dialog = new AlertDialog.Builder(AuthenticatorActivity.this).create();
	        dialog.setTitle(R.string.account_already_added_title);
	        dialog.setMessage(getText(R.string.account_already_added));
	        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok_btn), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			});
	        dialog.setCancelable(false);

	        return dialog;
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			finish();
		}
	}

	/**
     * Represents an asynchronous task used to authenticate a user against the
     * SampleSync Service
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {
    	private static final int SUCCESS =  0;
    	private static final int IO_ERROR = 1;

    	private volatile int mStatus = SUCCESS;


    	@Override
    	protected void onPreExecute() {
            // Show a progress dialog.
            showProgress();
            // Hides any possible error message
            hideMessage();
    	}

		@Override
        protected String doInBackground(Void... args) {
			try {
				return AccountAuthenticator.authenticate(AuthenticatorActivity.this, mUsername, mPassword);
			} catch (IOException e) {
				mStatus = IO_ERROR;
				return null;
			}
        }

        @Override
        protected void onPostExecute(final String authToken) {
        	switch (mStatus) {
        		case IO_ERROR:
        			onIOError();
                    break;
        		default:
                    // On a successful authentication, call back into the Activity to
                    // communicate the authToken (or null for an error).
                    onAuthenticationResult(authToken);
                    break;
        	}
        }

        @Override
        protected void onCancelled() {
            // If the action was canceled (by the user clicking the cancel
            // button in the progress dialog), then call back into the
            // activity to let it know.
            onAuthenticationCancel();
        }
    }
}
