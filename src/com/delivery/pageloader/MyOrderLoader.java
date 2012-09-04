package com.delivery.pageloader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.SessionUtils;
import com.delivery.account.UserAccount;
import com.delivery.engine.command.PageLoaderCommand;
import com.delivery.order.Order;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OrderDao;
import com.delivery.persistent.action.OrderActions;

public class MyOrderLoader extends PageLoaderCommand {
	private String mRedirect;

	@Override
	public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
		final UserAccount account = SessionUtils.getLoggedUser(req.getSession());
		if (account == null || !account.isValid()) {
			mRedirect = "Login.jsp";
			return;
		}

		try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            DaoManager daoManager = new DaoManager(dataSource);
            @SuppressWarnings("unchecked")
			List<Order> orders = (List<Order>) daoManager.execute(new DaoManager.DaoCommand() {
				@Override public Object execute(DaoManager manager) throws SQLException {
					// Primeiro vamos pegar a lista de pedidos abertos do usuario
					Order query = new Order();
					query.addExcludeStatusToQuery(Order.OrderStatus.FINISHED);
					query.addExcludeStatusToQuery(Order.OrderStatus.CANCELLED);
					query.setUserAccountId(account.getCpf());
					OrderDao orderDao = manager.getOrderDao();
					List<Order> orders = orderDao.get(query);

					return orders;
				}
			});

            ArrayList<Order> detailedOrders = new ArrayList<Order>();
            for (Order o : orders) {
            	Order detailedOrder = OrderActions.queryDetailedOrder(o.getId());
            	detailedOrders.add(detailedOrder);
            }

            req.setAttribute("orders", detailedOrders);
            mRedirect = "order/my-orders.jsp";
            Logger.debug("MyOrders conclu’do!");

		} catch (NamingException e) {
            Logger.error("NamingException", e);
			mRedirect = "Login.jsp"; // TODO error
        }
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
