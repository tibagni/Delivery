package com.delivery.pageloader.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.Logger;
import com.delivery.engine.command.AdminPageLoaderCommand;
import com.delivery.order.DeliveryGuy;
import com.delivery.order.Order;
import com.delivery.persistent.action.OrderActions;

public class OrderDetailLoader extends AdminPageLoaderCommand {
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
        	mRedirect = "sss"; // TODO
        	return;
        }
        Order order = OrderActions.queryDetailedOrder(orderId);

        if (order == null) {
        	Logger.debug("Erro ao buscar informacoes do pedido");
        	//TODO
        } else {
        	Logger.debug("Pedido encontrado e informacoes buscadas com sucesso");
        	Logger.debug("Redirecionando para: " + mRedirect);
            if (order.getNextAllowedStatus() == Order.OrderStatus.DELIVERING) {
            	List<DeliveryGuy> deliveryGuyList = OrderActions.getDeliveryGuyList();
            	req.setAttribute("deliveryGuyList", deliveryGuyList);
            }
        	req.setAttribute("order", order);
        }
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
