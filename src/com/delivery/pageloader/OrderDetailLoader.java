package com.delivery.pageloader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.Logger;
import com.delivery.engine.command.PageLoaderCommand;
import com.delivery.order.Order;
import com.delivery.persistent.action.OrderActions;

public class OrderDetailLoader extends PageLoaderCommand {
	private String mRedirect;

	@Override
	public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
		mRedirect = "order/order-detail.jsp";
		String orderIdStr = req.getParameter("orderId");
        final long orderId;
        try {
        	orderId = Long.parseLong(orderIdStr);
        } catch (Exception e) {
        	Logger.error("OrderDetailLoader", e);
        	req.setAttribute("errorMsg", "N‹o foi poss’vel carregar dados do pedido");
        	mRedirect = "ErrorMessage.jsp";
        	return;
        }
        Order order = OrderActions.queryDetailedOrder(orderId);

        if (order == null) {
        	Logger.debug("Erro ao buscar informacoes do pedido");
        	req.setAttribute("errorMsg", "N‹o foi poss’vel carregar dados do pedido");
        	mRedirect = "ErrorMessage.jsp";
        } else {
        	Logger.debug("Pedido encontrado e informacoes buscadas com sucesso");
        	Logger.debug("Redirecionando para: " + mRedirect);
        	req.setAttribute("order", order);
        }
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
