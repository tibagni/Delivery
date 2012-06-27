package com.delivery.order;

import javax.servlet.http.HttpServletRequest;

import com.delivery.Logger;
import com.delivery.engine.command.OrderCommand;
import com.delivery.util.StringUtils;

public class RemoveItemFromOrderCmd extends OrderCommand {
    private String mRedirect = null;

    @Override
    public void execute(HttpServletRequest request) {
        Logger.debug("RemoveItemFromOrderCmd");

        Order order = (Order) request.getSession().getAttribute("SessionOrder");
        if (order == null) {
            return;
        }

        String id = request.getParameter("itemTempId");
        if (StringUtils.isEmpty(id)) {
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            return;
        }

        order.removeItem(itemId);
        if (itemId == -1) {
            // Remove tudo!
            order.clear();
        }

        mRedirect = "order/ShopCar.jsp";
    }

    @Override
    public String getRedirect() {
        return mRedirect;
    }

}
