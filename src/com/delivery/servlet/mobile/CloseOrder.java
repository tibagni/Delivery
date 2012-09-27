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

import com.delivery.order.Order;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OrderDao;

public class CloseOrder extends HttpServlet {
	private static final long serialVersionUID = 237788274090968983L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			Context initContext = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

	        String orderIdStr = req.getParameter("orderId");
	        final long orderId = Long.parseLong(orderIdStr);

	        DaoManager daoManager = new DaoManager(dataSource);
	        daoManager.execute(new DaoManager.DaoCommand() {
				@Override
				public Object execute(DaoManager manager) throws SQLException {
					OrderDao dao = manager.getOrderDao();
					Order query = new Order();
					query.setId(orderId);
					List<Order> resultList = dao.get(query);
					if (resultList == null || resultList.size() != 1) {
						throw new IllegalStateException();
					}
					Order result = resultList.get(0);
					if (result.getStatus() != Order.OrderStatus.DELIVERING) {
						throw new IllegalStateException();
					}

					dao.changeOrderStatus(result.getStatus(), Order.OrderStatus.FINISHED, orderId, -1);
					return null;
				}
			});
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
