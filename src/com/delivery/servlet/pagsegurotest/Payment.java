package com.delivery.servlet.pagsegurotest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.xmlparser.PaymentParser;

import com.delivery.util.StringUtils;

public class Payment extends HttpServlet {
	private static final long serialVersionUID = -6048260862421890723L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String code = req.getParameter("code");
		PaymentRequest paymentRequest = null;
		if (!StringUtils.isEmpty(code)) {
			File f = new File(req.getServletContext().getRealPath("/testePagSeguro/" + code));
			if (f.exists()) {
				InputStream is = new FileInputStream(f);
				try {
					paymentRequest = PaymentParser.readPaymentRequestXml(is);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (paymentRequest != null) {
			req.setAttribute("code", code);
			req.setAttribute("paymentRequest", paymentRequest);
			req.setAttribute("xmlFile", "/Delivery/testePagSeguro/" + code);
		}

		RequestDispatcher dispatcher = req.getRequestDispatcher("/pagSeguroTestEnvironment/payment.jsp");
		dispatcher.forward(req, resp);
	}

}
