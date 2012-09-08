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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.TransactionSearchResult;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.infra.HttpsURLConnectionUtil;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.logs.PagSeguroLoggerFactory;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import br.com.uol.pagseguro.util.UrlUtil;
import br.com.uol.pagseguro.xmlparser.TransactionParser;
import br.com.uol.pagseguro.xmlparser.TransactionSearchResultXMLHandler;

/**
 * Encapsulates web service calls to search for PagSeguro transactions
 */
public class TransactionSearchService {

    /**
     * PagSeguro Log tool
     * @see Logger
     */
    static Logger log =  PagSeguroLoggerFactory.getLogger(TransactionSearchService.class);

    /**
     * PagSeguro transaction search web service URL
     */
    private static final String URL_SERVICE = PagSeguroSystem.getUrlSearch();

    /**
     * Content-type for web service requests
     */
    private static final String CONTENT_TYPE = PagSeguroSystem.getContentTypeFormUrlEncoded();

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"; // "2011-04-01T08:30"

    /**
     * Finds a transaction with a matching transaction code
     * 
     * @param credentials
     * @param transactionCode
     * @return a transaction in PagSeguro
     * @see Transaction
     * @throws PagSeguroServiceException
     */
    public static Transaction searchByCode(Credentials credentials, String transactionCode)
            throws PagSeguroServiceException {

		log.info("TransactionSearchService.SearchByCode(transactionCode=" + transactionCode + ") - begin");

        if (transactionCode == null || transactionCode.trim().equals("")) {
            throw new IllegalArgumentException("transaction code can not be null");
        }

        // calling transaction search web service
        HttpsURLConnection connection = HttpsURLConnectionUtil.getHttpsGetConnection(
                buildURLByCode(credentials, transactionCode), CONTENT_TYPE);

        try {
            // parsing transaction
            Transaction transaction = TransactionParser.readTransaction(connection.getInputStream());
			log.info("TransactionSearchService.SearchByCode(transactionCode="
					+ transactionCode + ") - end - " + transaction.toString());
            return transaction;
        } catch (Exception e) {
			log.error("TransactionSearchService.SearchByCode(transactionCode="
					+ transactionCode + ") - error", e);
            throw new RuntimeException(e);
        }finally{
        	connection.disconnect();
        }
    }

    /**
     * Search transactions associated with this set of credentials within a date range
     *
     * @param credentials
     * @param initialDate
     * @param finalDate
     * @param page
     * @param maxPageResults
     * @return a object of <b>TransactionSearchResult</b>
     * @see TransactionSearchResult
     * @throws PagSeguroServiceException
     */
    public static TransactionSearchResult searchByDate(Credentials credentials, Date initialDate, Date finalDate,
            Integer page, Integer maxPageResults) throws PagSeguroServiceException {
        
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    	String initialDateString = initialDate != null ? sdf.format(initialDate) : null;
    	String finalDateString = finalDate!= null ? sdf.format(finalDate) :  null;
        
		log.info("TransactionSearchService.SearchByDate(initialDate="
				+ initialDateString + ", finalDate=" + finalDateString + ") - begin");

        // call transaction search web service
        HttpsURLConnection connection = HttpsURLConnectionUtil.getHttpsGetConnection(
                buildURLByDate(credentials, initialDate, finalDate, page, maxPageResults), CONTENT_TYPE);

        TransactionSearchResult search = new TransactionSearchResult();
        try {
            // parsing PagSeguro response
            TransactionSearchResultXMLHandler.getHandler(connection.getInputStream(), search);
            
            log.info("TransactionSearchService.SearchByDate(initialDate="
    				+ initialDateString + ", finalDate=" + finalDateString + ") - end - "
    				+ search);
            
            return search;
            
        } catch (Exception e) {
        	log.error("TransactionSearchService.SearchByDate(initialDate="
    				+ initialDateString + ", finalDate=" + finalDateString + ") - error "
    				+ search, e);
            throw new RuntimeException(e);
        } finally {
        	connection.disconnect();
        }

    }

    private static String buildURLByDate(Credentials credentials, Date initialDate, Date finalDate, Integer page,
            Integer maxPageResults) {
        return buildURLByDateInterval(credentials, URL_SERVICE, initialDate, finalDate, page, maxPageResults);
    }

    private static String buildURLByCode(Credentials credentials, String transactionCode) {
        return URL_SERVICE + "/" + transactionCode + "?" + UrlUtil.buildQueryString(credentials.getAttributes());
    }

    private static String buildURLByDateInterval(Credentials credentials, String serviceURL, Date initialDate,
            Date finalDate, Integer page, Integer maxPageResults) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        StringBuffer url = new StringBuffer();
        url.append(serviceURL);
        url.append("?initialDate=" + (initialDate != null ? sdf.format(initialDate) : ""));
        url.append("&finalDate=" + (finalDate != null ? sdf.format(finalDate) : ""));
        if (page != null) {
            url.append("&page=" + page);
        }
        if (maxPageResults != null) {
            url.append("&maxPageResults=" + maxPageResults);
        }
        url.append("&" + UrlUtil.buildQueryString(credentials.getAttributes()));

        return url.toString();

    }
}