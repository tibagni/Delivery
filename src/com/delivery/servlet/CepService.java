package com.delivery.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.Logger;

public class CepService extends HttpServlet {
	private static final long serialVersionUID = -4180739102988117763L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Logger.debug("CepService started!");

		try {
			String cep = "";
			String format ="javascript";

			if (req.getParameter("cep") == null) {
				throw new RuntimeException();
			}

			cep = req.getParameter("cep");
			if (req.getParameter("formato") != null) {
				format = req.getParameter("formato");
			}

			URL url = new URL("http://cep.republicavirtual.com.br/web_cep.php?cep=" + cep + "&formato=" + format);
			HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Request-Method", "GET");
			connection.setDoInput(true);
			connection.setDoOutput(false); // Nao vamos mandar nada, so receber
			connection.connect();

			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			String s;
			while (null != (s = br.readLine())) {
				response.append(s);
			}
			br.close();
			connection.disconnect();

			resp.getOutputStream().print(response.toString());
		} catch(Exception e) {
			Logger.error(" ", e);
			// retorna status de erro!
			resp.getOutputStream().print("var resultadoCEP = { 'resultado' : '-1'}");
		}
	}

}
