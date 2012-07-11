package com.delivery.pageloader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.engine.command.PageLoaderCommand;

public class SignUpLoader extends PageLoaderCommand {
	private final String mRedirect = "signup.jsp";

	@Override
	public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
		// Nao precisa fazer nada! So carregar a pagina
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
