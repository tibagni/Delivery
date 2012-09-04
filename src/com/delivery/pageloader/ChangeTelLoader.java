package com.delivery.pageloader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.SessionUtils;
import com.delivery.engine.command.PageLoaderCommand;

public class ChangeTelLoader extends PageLoaderCommand {
	private String mRedirect;

	@Override
	public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
		if (SessionUtils.getLoggedUser(req.getSession()) == null) {
			mRedirect = "Login.jsp";
		} else {
			mRedirect = "user/change-tel.jsp";
		}
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
