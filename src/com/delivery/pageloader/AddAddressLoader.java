package com.delivery.pageloader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.SessionUtils;
import com.delivery.account.UserAccount;
import com.delivery.engine.command.PageLoaderCommand;

public class AddAddressLoader extends PageLoaderCommand {
	private String mRedirect;

	@Override
	public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
		UserAccount account = SessionUtils.getLoggedUser(req.getSession());
		if (account == null || !account.isValid()) {
			mRedirect = "Login.jsp";
			return;
		}

		mRedirect = "user/add-address.jsp";
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
