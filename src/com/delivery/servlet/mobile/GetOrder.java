package com.delivery.servlet.mobile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.Logger;
import com.delivery.order.Order;
import com.delivery.order.OrderXmlParser;
import com.delivery.persistent.action.OrderActions;

public class GetOrder extends HttpServlet {

	private static final long serialVersionUID = 7093264094282924305L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String orderId = req.getParameter("orderId");
			Order order = OrderActions.queryDetailedOrder(Long.parseLong(orderId));
			String xml = OrderXmlParser.writeXmlOrder(order);

			resp.getWriter().println(xml);
		} catch(NumberFormatException e) {
			Logger.error("GetOrderServlet", e);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
