package com.delivery.order;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import com.delivery.engine.command.OrderCommand;
import com.delivery.persistent.action.OrderActions;

public class ManualPaymentCmd extends OrderCommand {
	private String mRedirect;

	@Override
	public void execute(HttpServletRequest request) {
		try {
	        String value = request.getParameter("value").replaceAll(",", ".");
	        String orderId = request.getParameter("orderId");
	        String paymentId = request.getParameter("paymentId");

	        double dValue = new BigDecimal(value).doubleValue();
	        long lOrderId = Long.parseLong(orderId);
	        long lPaymentId = Long.parseLong(paymentId);

	        OrderActions.setOrderInManualPayment(lOrderId, lPaymentId, dValue);

	        request.setAttribute("redirectURL", "/Delivery");
	        request.setAttribute("redirectMsg", "Pagamento combinado! Voce será redirecionado para a página principal");
	        mRedirect = "redirector.jsp";

		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", "Ocorreu algum erro inesperado! Vá em 'Meus pedidos' e tente realizar a operação novamente");
			mRedirect = "ErrorMessage.jsp";
		}

	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
