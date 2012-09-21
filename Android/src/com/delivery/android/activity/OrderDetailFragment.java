package com.delivery.android.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.delivery.android.R;
import com.delivery.android.provider.Address;
import com.delivery.android.provider.Order;

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


    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        new LoadOrderTask().execute();

        return rootView;
    }

    private void onOrderLoaded(final Order order) {
    	if (order != null) {
    		mOrderIdTextView.setText(String.valueOf(order.getId()));
    		mOrderdescriptionTextView.setText(order.getDescription());
    		if (order.isCharge()) {
    			mChargeMsgTextView.setVisibility(View.VISIBLE);
    			mChangeTextView.setVisibility(View.VISIBLE);
    			mChangeTextView.setText(getString(R.string.change_view_string, String.valueOf(order.getChange())));
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
}
