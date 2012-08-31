package com.delivery;

import javax.servlet.http.HttpSession;

import com.delivery.admin.Admin;

public class AdminFilter extends LoginFilter {

	@Override
	public void destroy() {}

	@Override
	protected boolean hasAccess(HttpSession httpSession) {
		Admin account = SessionUtils.getLoggedAdmin(httpSession);
		return (account != null && account.isValid());
	}

}
