package com.delivery.pageloader;

import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.SessionUtils;
import com.delivery.account.UserAccount;
import com.delivery.engine.command.PageLoaderCommand;
import com.delivery.order.Order;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OrderDao;

public class OrderHistoryLoader extends PageLoaderCommand {
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
					OrderDao dao = manager.getOrderDao();
					Order query = new Order();
					query.setUserAccountId(account.getCpf());
					query.addStatusToQuery(Order.OrderStatus.FINISHED);
					query.addStatusToQuery(Order.OrderStatus.CANCELLED);

					return dao.get(query);
				}
			});
	        req.setAttribute("orders", orders);
	        mRedirect = "order/order-history.jsp";

		} catch(Exception ex) {
			req.setAttribute("errorMsg", "N‹o foi poss’vel carregar o hist—rico de pedidos");
			mRedirect = "ErrorMessage.jsp";
		}
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
