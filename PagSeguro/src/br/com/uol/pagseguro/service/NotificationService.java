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

import javax.net.ssl.HttpsURLConnection;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.infra.HttpsURLConnectionUtil;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.logs.PagSeguroLoggerFactory;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import br.com.uol.pagseguro.util.UrlUtil;
import br.com.uol.pagseguro.xmlparser.TransactionParser;

/**
 * Encapsulates web service calls regarding PagSeguro notifications
 */
public class NotificationService {

    /**
     * PagSeguro Log tool
     * @see Logger
     */
    static Logger log =  PagSeguroLoggerFactory.getLogger(PaymentService.class);

    /**
     * Url of Search Web Service
     */
    private static final String URL_SERVICE = PagSeguroSystem.getUrlNotification();

    /**
     * Get/Post content type used in the Search Web Service
     */
    private static final String CONTENT_TYPE = PagSeguroSystem.getContentTypeFormUrlEncoded();

    /**
     * Returns a transaction from a notification code
     * 
     * @param credentials
     * @param notificationCode
     * @return a transaction from a notification code
     * @throws PagSeguroServiceException
     */
    public static Transaction checkTransaction(Credentials credentials, String notificationCode)
            throws PagSeguroServiceException {

        log.info("NotificationService.CheckTransaction(notificationCode=" + notificationCode + ") - begin");

        // calling transaction notifications webservice
        HttpsURLConnection connection = HttpsURLConnectionUtil.getHttpsGetConnection(
                buildURL(credentials, notificationCode), CONTENT_TYPE);

        try {
            // Parsing the transaction
            Transaction transaction = TransactionParser.readTransaction(connection.getInputStream());
            log.info("NotificationService.CheckTransaction(notificationCode=" + notificationCode + ") - end - " + transaction);
            return transaction;
        } catch (Exception e) {
            log.error("NotificationService.CheckTransaction(notificationCode=" + notificationCode + ") - ", e);
            throw new RuntimeException(e);
        }
    }

    private static String buildURL(Credentials credentials, String notificationCode) {
        return URL_SERVICE + "/" + notificationCode + "/?" + UrlUtil.buildQueryString(credentials.getAttributes());
    }
}