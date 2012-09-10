package com.delivery.servlet.pagsegurotest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.xmlparser.PaymentParser;

import com.delivery.Logger;

public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 9049886246361971835L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PaymentRequest paymentRequest = null;
		InputStream is = req.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(is, baos);
		byte[] xmlBytes = baos.toByteArray();
		String paymentCode;
		try {
			paymentRequest = PaymentParser.readPaymentRequestXml(new ByteArrayInputStream(xmlBytes));
			Logger.debug(paymentRequest.toString());

			// Salva o paymentRequest em um arquivo xml

			//Primeiro gera o nome do arquivo - que sera o codigo do pagamento
			Random r = new Random();
			byte[] randomBytes = new byte[5];
			r.nextBytes(randomBytes);

			// Cria um hash combinando o xml com alguns bytes aleatorios (para que seja unico)
			byte[] nameBytes = new byte[xmlBytes.length + randomBytes.length];
			System.arraycopy(xmlBytes, 0, nameBytes, 0, xmlBytes.length);
			System.arraycopy(randomBytes, 0, nameBytes, xmlBytes.length, randomBytes.length);
			paymentCode = hashName(nameBytes);

			File f = new File(req.getServletContext().getRealPath("/testePagSeguro/" + paymentCode));
			OutputStream fos = new FileOutputStream(f);
			fos.write(xmlBytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			// Qualquer excessao tratamos como erro
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		resp.getWriter().write("<checkout><code>" + paymentCode + "</code></checkout>");
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	private String hashName(byte[] bytes) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(bytes);
		byte[] hashMd5 = md.digest();

		StringBuilder s = new StringBuilder();
		   for (int i = 0; i < hashMd5.length; i++) {
		       int parteAlta = ((hashMd5[i] >> 4) & 0xf) << 4;
		       int parteBaixa = hashMd5[i] & 0xf;
		       if (parteAlta == 0) s.append('0');
		       s.append(Integer.toHexString(parteAlta | parteBaixa));
		   }
		   return s.toString();
	}


}
