package com.delivery.android.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.delivery.android.R;
import com.delivery.android.account.AccountInfo;
import com.delivery.android.client.NetworkUtils;
import com.delivery.android.preferences.DeliveryPreferences;
import com.delivery.android.provider.Address;
import com.delivery.android.provider.Order;
import com.delivery.android.sync.SyncManager;

public class OrderDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private TextView mOrderIdTextView;
    private TextView mOrderdescriptionTextView;
    private TextView mChargeMsgTextView;
    private TextView mChangeTextView;
    private TextView mAddressTextView;
    private TextView mDateTextView;
    private TextView mClientNameTextView;
    private Button mMapButton;

    private long mOrderId;
    private long mOrderRemoteId;

    private ProgressDialogFragment mProgress;


    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
        	mOrderId = getArguments().getLong(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        mOrderIdTextView = (TextView) rootView.findViewById(R.id.order_id_view);
        mOrderdescriptionTextView = (TextView) rootView.findViewById(R.id.description);
        mChargeMsgTextView = (TextView) rootView.findViewById(R.id.charge_msg);
        mChangeTextView = (TextView) rootView.findViewById(R.id.change);
        mAddressTextView = (TextView) rootView.findViewById(R.id.address_view);
        mDateTextView = (TextView) rootView.findViewById(R.id.date_time_value);
        mClientNameTextView = (TextView) rootView.findViewById(R.id.client_name_view);
        mMapButton = (Button) rootView.findViewById(R.id.map_button);
        showProgressDialog();
        new LoadOrderTask().execute();

        return rootView;
    }

	private void showProgressDialog() {
        FragmentManager fm = getFragmentManager();
        mProgress = new ProgressDialogFragment(getString(R.string.loading), getString(R.string.loading_order));
        mProgress.show(fm, "fragment_progress");
	}

	private void hideProgressDialog() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.order_detail_options_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.finalize_order:
	        	showFinalizeConfirmation();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
    }

	private void showFinalizeConfirmation() {
        FragmentManager fm = getFragmentManager();
        ConfirmationDialogFragment alreadyAddedDialog = new ConfirmationDialogFragment();
        alreadyAddedDialog.show(fm, "fragment_confirmation");
	}

    private void onOrderLoaded(final Order order) {
    	if (order != null) {
    		mOrderRemoteId = order.getRemoteId();
    		mOrderIdTextView.setText(String.valueOf(order.getRemoteId()));
    		mOrderdescriptionTextView.setText(order.getDescription());
    		if (order.isCharge()) {
    			mChargeMsgTextView.setVisibility(View.VISIBLE);
    			mChangeTextView.setVisibility(View.VISIBLE);
    			mChangeTextView.setText(getString(R.string.change_view_string, String.valueOf(order.getChange()),
    					String.valueOf(order.getPaymentValue())));
    		}
    		final Address addr = order.getRemoteAddress();
    		String comp = addr.getComplement() == null ? "" : addr.getComplement();
    		mAddressTextView.setText(getString(R.string.address_view, addr.getStreet(),
    				String.valueOf(addr.getNumber()), comp, addr.getDistrict(), addr.getCity(), addr.getUf(), addr.getZip()));
    		mDateTextView.setText(order.getDateTime());
    		mClientNameTextView.setText(order.getClientName());

    		mMapButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String destination = String.format("%s, %s, %s, Brasil",
							addr.getStreet(), addr.getDistrict(), addr.getCity());
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
							Uri.parse("http://maps.google.com/maps?daddr=" + destination));
					startActivity(intent);
				}
			});
    	}

    	hideProgressDialog();
    }

    private void onFinishOrder() {
    	new FinishOrderTask().execute();
    }

    private void onOrdereFinished(boolean success) {
    	if (!success) {
    		Toast.makeText(getActivity(), R.string.error_finishing_order, Toast.LENGTH_LONG).show();
    	} else {
    		SyncManager.getInstance(getActivity()).requestSync(true);
    		getFragmentManager().beginTransaction().remove(this).commit();
    		Activity activity = getActivity();
    		// if we are running on one screen, we need to return to list activity
    		if (activity instanceof OrderDetailActivity) {
    			((OrderDetailActivity) activity).navigateUp();
    		}
    	}
    }

    private class FinishOrderTask extends AsyncTask<Void, Void, Boolean> {
    	ProgressDialogFragment mProgressFinishing;

    	@Override
    	protected void onPreExecute() {
            FragmentManager fm = getFragmentManager();
            mProgressFinishing = new ProgressDialogFragment(getString(R.string.loading), getString(R.string.finishing_order));
            mProgressFinishing.show(fm, "fragment_progress_finishing");
    	}

		@Override
		protected Boolean doInBackground(Void... args) {
			final String URL = DeliveryPreferences.getInstance(getActivity()).getOrderServerAddress() + "/mobile/CloseOrder";
			final HttpResponse resp;
	    	try {
	    		// First we need to get the account token
	    		AccountManager am = AccountManager.get(getActivity());
	    		Account account = AccountInfo.getConfiguredAccount(getActivity());
	    		Bundle result = am.getAuthToken(account, AccountInfo.AUTH_TOKEN_TYPE, null,
	    				true, null, null).getResult();
	    		String token = result.getString(AccountManager.KEY_AUTHTOKEN);

	    		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("token", token));
	            params.add(new BasicNameValuePair("orderId", String.valueOf(mOrderRemoteId)));
	            final HttpEntity entity;
	            try {
	                entity = new UrlEncodedFormEntity(params);
	            } catch (final UnsupportedEncodingException e) {
	                // this should never happen.
	                throw new IllegalStateException(e);
	            }
	            final HttpPost post = new HttpPost(URL);
				post.addHeader(entity.getContentType());
				post.setEntity(entity);
				resp = NetworkUtils.getHttpClient().execute(post);
				int statusCode = resp.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					// Some error occured
					return false;
				}
	        } catch (Exception e) {
	        	Log.e("ERROR", e.getMessage(), e);
	        	return false;
	        }
	    	return true;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			if (mProgressFinishing != null) {
				mProgressFinishing.dismiss();
				mProgressFinishing = null;
			}
			onOrdereFinished(success);
		}

    }

    private class LoadOrderTask extends AsyncTask<Void, Void, Order> {

		@Override
		protected Order doInBackground(Void... params) {
			Cursor orderCursor = null;
			Cursor addressCursor = null;
			Order order = null;
			try {
				ContentResolver resolver = getActivity().getContentResolver();
				Uri orderUri = Uri.withAppendedPath(Order.CONTENT_URI, String.valueOf(mOrderId));
				orderCursor = resolver.query(orderUri, null, null, null, null);
				orderCursor.moveToFirst();
				order = Order.restore(orderCursor);

				Uri addressUri = Uri.withAppendedPath(Address.CONTENT_URI, String.valueOf(order.getAddressKey()));
				addressCursor = resolver.query(addressUri, null, null, null, null);
				addressCursor.moveToFirst();
				Address address = Address.restore(addressCursor);
				order.setRemoteAddress(address);
			} finally {
				if (addressCursor != null) {
					addressCursor.close();
				}
				if (orderCursor != null) {
					orderCursor.close();
				}
			}
			return order;
		}

		@Override
		protected void onPostExecute(Order result) {
			onOrderLoaded(result);
		}

    }

    private class ConfirmationDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
	        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
	        dialog.setTitle(R.string.finalize_order_confirmation_title);
	        dialog.setMessage(getText(R.string.finalize_order_confimation_message));
	        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
	        		getString(R.string.finalize_order_confirmation_ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
					onFinishOrder();
				}
			});
	        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
	        		getString(R.string.finalize_order_confirmation_cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			});
	        dialog.setCancelable(true);

	        return dialog;
		}
	}
}
