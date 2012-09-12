package com.delivery.servlet.pagsegurotest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.xmlparser.PaymentParser;

import com.delivery.Logger;
import com.delivery.util.StringUtils;

public class AutomaticReply extends HttpServlet {
	private static final long serialVersionUID = -5530349331061548743L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
		String code = req.getParameter("code");
		Logger.debug(code);
		String queryString = buildQueryString(code, req.getServletContext());
		URL u = new URL("http://localhost:8080/Delivery/PaymentReturn");
		HttpURLConnection uc = (HttpURLConnection) u.openConnection();
		uc.setDoOutput(true);
		uc.setDoInput(true);
		uc.setUseCaches(false);
		uc.setRequestMethod("POST");
		uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		uc.setRequestProperty("Content-Length", "" + Integer.toString(queryString.getBytes().length));
		DataOutputStream wr = new DataOutputStream (uc.getOutputStream ());
		wr.writeBytes(queryString);
		wr.flush();
		wr.close();

		// ignora a resposta
		InputStream is = uc.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
	    rd.close();
	    Logger.debug("retorno: " + response.toString());
		uc.disconnect();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private String buildQueryString(String code, ServletContext context) throws FileNotFoundException {
		PaymentRequest paymentRequest = null;
		if (!StringUtils.isEmpty(code)) {
			File f = new File(context.getRealPath("/testePagSeguro/" + code));
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
			// Para testes estes dados sao suficientes
			StringBuilder s = new StringBuilder();
			s.append("Referencia=" + paymentRequest.getReference());
			s.append("&");
			s.append("TipoPagamento=Cartão de Crédito");
			s.append("&");
			s.append("StatusTransacao=Completo");
			return s.toString();
		}
		return null;
	}
}
