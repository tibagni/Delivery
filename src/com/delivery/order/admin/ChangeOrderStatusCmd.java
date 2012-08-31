package com.delivery.order.admin;

import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.AdminOrderCommand;
import com.delivery.order.Order;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OrderDao;

public class ChangeOrderStatusCmd extends AdminOrderCommand {

	@Override
	public void execute(HttpServletRequest request) {
		try {
	        // Get DataSource
	        Context initContext  = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

	        String orderIdStr = request.getParameter("orderId");
	        String newStatusStr = request.getParameter("newStatus");
	        final long orderId;
	        final int newStatus;
	        try {
	        	orderId = Long.parseLong(orderIdStr);
	        	newStatus = Integer.parseInt(newStatusStr);
	        } catch (Exception e) {
	        	Logger.error("OrderDetailLoader", e);
	        	return;
	        }

	        DaoManager daoManager = new DaoManager(dataSource);
	        boolean success = (Boolean) daoManager.transaction(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {
					OrderDao dao = manager.getOrderDao();
					Order query = new Order();
					query.setId(orderId);

					List<Order> result = dao.get(query);
					if (result == null || result.size() == 0) {
						Logger.wtf("Nao e possivel mudar o stats do pedido. Nao encontrado!!!!!");
						return false;
					}
					Order order = result.get(0);

					try {
						dao.changeOrderStatus(order.getStatus(), newStatus, order.getId());
					} catch(IllegalStateException e) {
						Logger.error("Nao foi possivel alterar status do pedido. Estados inconsistentes");
						return false;
					}

					return true;
				}
			});
		} catch(NamingException e) {
	        Logger.error("NamingException", e);
		}
	}

	@Override
	public String getRedirect() {
		return null;
	}

}
