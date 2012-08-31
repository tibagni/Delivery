package com.delivery;

import javax.servlet.http.HttpSession;

import com.delivery.account.UserAccount;

public class UserLoginFilter extends LoginFilter {

	@Override
	public void destroy() {}

	@Override
	protected boolean hasAccess(HttpSession session) {
		UserAccount account = SessionUtils.getLoggedUser(session);
		return (account != null && account.isValid());
	}

}
