/**
 * Copyright [2011] [PagSeguro Internet Ltda.]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package br.com.uol.pagseguro.service;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.infra.HttpsURLConnectionUtil;
import br.com.uol.pagseguro.logs.PagSeguroDummyLogger;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.logs.PagSeguroLoggerFactory;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import br.com.uol.pagseguro.util.UrlUtil;
import br.com.uol.pagseguro.xmlparser.PaymentParser;

/**
 * Encapsulates web service calls regarding PagSeguro payment requests
 */
public class PaymentService {

	/**
	 * PagSeguro Log tool
	 * 
	 * @see PagSeguroDummyLogger
	 */
    static Logger log = PagSeguroLoggerFactory.getLogger(PaymentService.class);

    /**
     * PagSeguro payment web service URL
     */
    private static final String URL_SERVICE = PagSeguroSystem.getUrlPayment();

    /**
     * PagSeguro payment web site URL
     */
    private static final String URL_REDIR = PagSeguroSystem.getUrlPaymentRedir();

    /**
     * Content-type for web service requests
     */
    private static final String URL_CONTENT_TYPE = PagSeguroSystem.getContentTypeXml();

    /**
     * Charset for web service requests
     */
    private static final String CHARSET = PagSeguroSystem.getPagSeguroEncoding();

    /**
     * Requests a payment
     * 
     * @param paymentRequest
     * @return The Uri to where the user needs to be redirected to in order to complete the payment process
     * @throws PagSeguroServiceException
     */
    public static URL doPayment(Credentials credentials, PaymentRequest paymentRequest)
            throws PagSeguroServiceException {

    	log.info("PaymentService.Register(" + paymentRequest + ") - begin");
        String xml = PaymentParser.writePaymentXml(paymentRequest);

        // calling payment web service
        HttpsURLConnection connection = HttpsURLConnectionUtil.getHttpsPostConnection(buildUrl(credentials, paymentRequest),
                URL_CONTENT_TYPE + "; charset=" + CHARSET, xml);

        try {
            // parsing web service response
            URL paymentURL = new URL(URL_REDIR + PaymentParser.readPaymentReturnXml(connection.getInputStream()));
            
            log.info("PaymentService.Register("+paymentRequest+") - end - " + paymentURL);
            return paymentURL;
            
        } catch (Exception e) {
            log.error("PaymentService.Register(" + paymentRequest + ") - error ", e);
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }

    private static String buildUrl(Credentials credentials, PaymentRequest payment) {
        return URL_SERVICE + "?" + UrlUtil.buildQueryString(credentials.getAttributes());
    }
}