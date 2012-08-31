package com.delivery;

import javax.servlet.http.HttpSession;

import com.delivery.account.UserAccount;
import com.delivery.admin.Admin;
import com.delivery.order.Order;

public class SessionUtils {

	private static final String ORDER = "SessionOrder";
	private static final String USER_LOGGED = "UserSession";
	private static final String ADMIN_LOGGED = "AdminSession";

	public static void setLoggedUser(HttpSession session, UserAccount user) {
		if (user == null) {
			session.removeAttribute(USER_LOGGED);
		} else {
			session.setAttribute(USER_LOGGED, user);
		}
	}

	public static UserAccount getLoggedUser(HttpSession session) {
		try {
			return (UserAccount) session.getAttribute(USER_LOGGED);
		} catch (Exception e) {
			return null;
		}
	}

	public static void setLoggedAdmin(HttpSession session, Admin admin) {
		if (admin == null) {
			session.removeAttribute(ADMIN_LOGGED);
		} else {
			session.setAttribute(ADMIN_LOGGED, admin);
		}
	}

	public static Admin getLoggedAdmin(HttpSession session) {
		try {
			return (Admin) session.getAttribute(ADMIN_LOGGED);
		} catch (Exception e) {
			return null;
		}
	}

	public static void setActiveOrder(HttpSession session, Order order) {
		if (order == null) {
			session.removeAttribute(ORDER);
		} else {
			session.setAttribute(ORDER, order);
		}
	}

	public static Order getActiveOrder(HttpSession session) {
		try {
			return (Order) session.getAttribute(ORDER);
		} catch (Exception e) {
			return null;
		}
	}

	public static void clearSession(HttpSession session) {
		setLoggedUser(session, null);
		setActiveOrder(session, null);
		setLoggedAdmin(session, null);
	}
}
