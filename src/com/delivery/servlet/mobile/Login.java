package com.delivery.servlet.mobile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.Logger;

public class Login extends HttpServlet {

	private static final long serialVersionUID = -7011733129003551839L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String code = req.getParameter("code");
		String password = req.getParameter("password");

		Logger.debug("Mobile login: code-" + code + " pswd-" + password);

		resp.getWriter().println("token!!!!!");
	}
}
