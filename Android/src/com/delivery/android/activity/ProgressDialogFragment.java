package com.delivery.android.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {
	private String mTitle = "";
	private String mMessage = "";

	public ProgressDialogFragment(String dialogTitle, String dialogMessage) {
		mTitle = dialogTitle;
		mMessage = dialogMessage;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(mTitle);
        dialog.setMessage(mMessage);
        dialog.setCancelable(false);

        return dialog;
	}
}
