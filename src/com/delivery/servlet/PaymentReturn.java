package com.delivery.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.uol.pagseguro.properties.PagSeguroConfig;

import com.delivery.Logger;
import com.delivery.persistent.action.OrderActions;

public class PaymentReturn extends HttpServlet{
	private static final long serialVersionUID = -705604846382819445L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//o método POST indica que a requisição é o retorno da validação NPI.
		Logger.debug("PaymentReturn POST");
		Enumeration<?> en = req.getParameterNames();
		String token = PagSeguroConfig.getAccountToken();
		StringBuffer validaNPI = new StringBuffer("Comando=validar&Token=");
		validaNPI.append(token);

		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = req.getParameter(paramName);
			validaNPI.append("&");
			validaNPI.append(paramName);
			validaNPI.append("=");
			validaNPI.append(paramValue);
		}


//		URL u = new URL("https://pagseguro.uol.com.br/pagseguro-ws/checkout/NPI.jhtml");
//		URLConnection uc = u.openConnection();
//		uc.setDoOutput(true);
//		uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		PrintWriter pw = new PrintWriter(uc.getOutputStream());
//		pw.println(validaNPI.toString());
//		pw.close();
//
//		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
//		String res = in.readLine();
//		in.close();

		//Descomentar quando acabar os testes
		String res = "VERIFICADO";

		//Verifica se o status da transação está VERIFICADO
		String transacaoID = req.getParameter("TransacaoID");
		if (res.equals("VERIFICADO")) {
			String status = req.getParameter("StatusTransacao");
			if ("Completo".equals(status)) {
				try {
				Logger.debug("Pagamento concluido");
				String ref = req.getParameter("Referencia");
				String[] refParts = ref.split("-");
				long orderId = Long.parseLong(refParts[2]);
				OrderActions.setOrderPaymentCompleted(orderId);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} else if (res.equals("FALSO")) {
			//o post nao foi validado
		} else {
			//erro na integração com PagSeguro.
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Logger.debug("PaymentReturn GET");

		//o método GET indica que a requisição é o retorno do Checkout PagSeguro para o site vendedor.
		//no término do checkout o usuário é redirecionado para este bloco.
		RequestDispatcher dispatcher = req.getRequestDispatcher("paymentCompleted.jsp");
    	dispatcher.forward(req, resp);
	}
}
