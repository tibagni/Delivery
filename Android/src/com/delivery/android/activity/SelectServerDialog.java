package com.delivery.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.delivery.android.R;
import com.delivery.android.preferences.DeliveryPreferences;

public class SelectServerDialog extends DialogFragment implements OnEditorActionListener {
	private EditText mEditText;
	private DeliveryPreferences mPrefs;

    public SelectServerDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
		mPrefs = DeliveryPreferences.getInstance(activity);
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

	private void onFinishEditDialog(String textEntered) {
		mPrefs.setOrderServerAddress(textEntered);
		String not = getString(R.string.selected_server_msg, textEntered);
		Toast.makeText(getActivity(), not, Toast.LENGTH_SHORT).show();
	}
}
