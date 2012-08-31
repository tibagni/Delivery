package com.delivery;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class LoginFilter implements Filter {
	protected ServletContext mContext;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (!hasAccess(req.getSession())) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return; // Nao continua.

		}

		// Continua com o processamento a cadeia
		chain.doFilter(request, response);
	}

	protected abstract boolean hasAccess(HttpSession httpSession);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		mContext = filterConfig.getServletContext();
	}

}
