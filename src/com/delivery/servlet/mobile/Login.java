package com.delivery.servlet.mobile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.order.DeliveryGuy;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.DeliveryGuyDao;
import com.delivery.util.StringUtils;

public class Login extends HttpServlet {

	private static final long serialVersionUID = -7011733129003551839L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Context initContext = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

			String code = req.getParameter("code");
			String password = req.getParameter("password");

			if (StringUtils.isEmpty(code) || StringUtils.isEmpty(password)) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			final DeliveryGuy deliveryGuy = new DeliveryGuy();
			deliveryGuy.setTokenValidForQuery(false);
			deliveryGuy.setCode(Integer.parseInt(code));
			deliveryGuy.setPassword(password);

			DaoManager daoManager = new DaoManager(dataSource);
			DeliveryGuy dG = (DeliveryGuy) daoManager.execute(new DaoManager.DaoCommand() {
				@Override
				public Object execute(DaoManager manager) throws SQLException {
					DeliveryGuyDao dao = manager.getDeliveryGuyDao();
					List<DeliveryGuy> result = dao.get(deliveryGuy);

					if (result == null || result.size() != 1) {
						return null;
					}

					return result.get(0);
				}
			});
			if (dG == null) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}

			// Primeira linha o token, segunda linha o nome do entregador
			resp.getWriter().println(dG.getToken());
			resp.getWriter().println(dG.getName());
		} catch(Exception e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

	}
}
