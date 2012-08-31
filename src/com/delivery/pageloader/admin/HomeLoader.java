package com.delivery.pageloader.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.engine.command.AdminPageLoaderCommand;

public class HomeLoader extends AdminPageLoaderCommand {

	@Override
	public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {}

	@Override
	public String getRedirect() {
		return "home.jsp";
	}

}
