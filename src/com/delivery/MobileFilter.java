package com.delivery;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.order.DeliveryGuy;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.DeliveryGuyDao;
import com.delivery.util.StringUtils;

public class MobileFilter implements Filter {
	private DataSource mDataSource;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		final String token = req.getParameter("token");
		DeliveryGuy deliveryGuy = null;
		if (!StringUtils.isEmpty(token)) {
			DaoManager daoManager = new DaoManager(mDataSource);
			deliveryGuy = (DeliveryGuy) daoManager.execute(new DaoManager.DaoCommand() {
				@Override
				public Object execute(DaoManager manager) throws SQLException {
					DeliveryGuyDao dao = manager.getDeliveryGuyDao();
					DeliveryGuy query = new DeliveryGuy();
					query.setToken(token);
					query.setTokenValidForQuery(true);
					List<DeliveryGuy> result = dao.get(query);

					if (result == null || result.size() != 1) {
						return null;
					}
					return result.get(0);
				}
			});
		}
		if (deliveryGuy == null) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return; // Nao continua.
		}

		req.setAttribute("deliveryGuy", deliveryGuy);
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			Context initContext = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        mDataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");
		} catch(NamingException e) {
			Logger.error("MobileFilter", e);
		}
	}

}
