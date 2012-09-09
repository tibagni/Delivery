package com.delivery.servlet.pagsegurotest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.xmlparser.PaymentParser;

import com.delivery.Logger;

public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 9049886246361971835L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PaymentRequest paymentRequest = null;
		try {
			paymentRequest = PaymentParser.readPaymentRequestXml(req.getInputStream());
			Logger.debug(paymentRequest.toString());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resp.getWriter().write("<checkout><code>" + 123 + "</code></checkout>");
		resp.setStatus(HttpServletResponse.SC_OK);
	}
}
