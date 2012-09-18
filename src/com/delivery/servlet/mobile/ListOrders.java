package com.delivery.servlet.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.order.DeliveryGuy;
import com.delivery.order.Order;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OrderDao;

public class ListOrders extends HttpServlet {

	private static final long serialVersionUID = -3237551646399315115L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final DeliveryGuy dG = (DeliveryGuy) req.getAttribute("deliveryGuy");
		try {
			Context initContext = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

	        DaoManager daoManager = new DaoManager(dataSource);
	        @SuppressWarnings("unchecked")
			List<Order> orders = (List<Order>) daoManager.execute(new DaoManager.DaoCommand() {
				@Override
				public Object execute(DaoManager manager) throws SQLException {
					OrderDao dao = manager.getOrderDao();
					Order query = new Order();
					query.setDeliveryGuyId(dG.getCode());
					query.addStatusToQuery(Order.OrderStatus.DELIVERING);

					List<Order> orders = dao.get(query);
					if (orders == null || orders.size() == 0) {
						return null;
					}

					return orders;
				}
			});

	        if (orders != null) {
	        	PrintWriter writer = resp.getWriter();
	        	for (Order o : orders) {
	        		writer.println(o.getId());
	        	}
	        }

		} catch(NamingException e) {
			Logger.error("MobileFilter", e);
		}


	}
}
