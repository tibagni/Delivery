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
package br.com.uol.pagseguro.xmlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.TransactionSearchResult;
import br.com.uol.pagseguro.domain.TransactionSummary;
import br.com.uol.pagseguro.logs.PagSeguroDummyLogger;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.logs.PagSeguroLoggerFactory;
import br.com.uol.pagseguro.util.DateParserUTC;

/**
 * Utility class for handle transactions search in PagSeguro's web service 
 */
public class TransactionSearchResultXMLHandler extends DefaultHandler {

    private List handledElements = new ArrayList();

    private static final String ROOT_ELEMENT = "transactionSearchResult";
    private static final String TRANSACTIONS_ELEMENT = "transactions";

    private String parentElement = ROOT_ELEMENT;
    private String currentElement = ROOT_ELEMENT;

    private StringBuffer xmlTransaction;

    private TransactionSearchResult transactionSearchResult;
    private List transactions = new ArrayList();

    public TransactionSearchResultXMLHandler(TransactionSearchResult transactionSearchResult) {
        handledElements = new ArrayList();
        handledElements.add("date");
        handledElements.add("currentPage");
        handledElements.add("resultsInThisPage");
        handledElements.add("totalPages");

        this.transactionSearchResult = transactionSearchResult;
    }

    public TransactionSearchResult getTransactionSearchResult() {
        return transactionSearchResult;
    }

    public void setTransactionSearchResult(TransactionSearchResult transactionSearchResult) {
        this.transactionSearchResult = transactionSearchResult;
    }

    public void characters(char[] buffer, int start, int length) throws SAXException {

        StringBuffer buf = new StringBuffer();
        if (parentElement.equals(ROOT_ELEMENT)) {
            if (currentElement.equals("date")) {
                try {
                    transactionSearchResult.setDate(DateParserUTC.parse(buf.append(buffer, start, length).toString()));
                } catch (ParseException e) {
                    throw new SAXException(e);
                }
            } else if (currentElement.equals("currentPage")) {
                transactionSearchResult.setPage(Integer.parseInt(buf.append(buffer, start, length).toString()));
            } else if (currentElement.equals("resultsInThisPage")) {
                transactionSearchResult.setResultsInThisPage(Integer.parseInt(buf.append(buffer, start, length)
                        .toString()));
            } else if (currentElement.equals("totalPages")) {
                transactionSearchResult.setTotalPages(Integer.parseInt(buf.append(buffer, start, length).toString()));
            }
        } else if (currentElement.equals("transaction")) {
            xmlTransaction.append(buffer, start, length);
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (handledElements.contains(qName) && parentElement.equals(ROOT_ELEMENT)) {
            currentElement = qName;
        }
        if (qName.equals(TRANSACTIONS_ELEMENT)) {
            parentElement = TRANSACTIONS_ELEMENT;
        }
        if (qName.equals("transaction")) {
            currentElement = "transaction";
            xmlTransaction = new StringBuffer();
        }
        if (currentElement.equals("transaction")) {
            xmlTransaction.append("<" + qName + ">");
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals(TRANSACTIONS_ELEMENT)) {
            parentElement = ROOT_ELEMENT;
        }
        if (currentElement.equals("transaction")) {
            xmlTransaction.append("</" + qName + ">");
        }
        if (qName.equals("transaction")) {
            try {
                Transaction transaction = TransactionParser.readTransaction(new ByteArrayInputStream(xmlTransaction
                        .toString().getBytes()));
                TransactionSummary transactionSummary = buildTransactionSummary(transaction);
                transactions.add(transactionSummary);
            } catch (ParserConfigurationException e) {
                throw new SAXException(e);
            } catch (ParseException e) {
                throw new SAXException(e);
            } catch (IOException e) {
                throw new SAXException(e);
            }
        }
    }

    private TransactionSummary buildTransactionSummary(Transaction transaction) {
        TransactionSummary transactionSummary = new TransactionSummary();
        transactionSummary.setCode(transaction.getCode());
        transactionSummary.setDate(transaction.getDate());
        transactionSummary.setDiscountAmount(transaction.getDiscountAmount());
        transactionSummary.setExtraAmount(transaction.getExtraAmount());
        transactionSummary.setFeeAmount(transaction.getFeeAmount());
        transactionSummary.setGrossAmount(transaction.getGrossAmount());
        transactionSummary.setLastEvent(transaction.getLastEventDate());
        transactionSummary.setNetAmount(transaction.getNetAmount());
        transactionSummary.setPaymentMethod(transaction.getPaymentMethod());
        transactionSummary.setReference(transaction.getReference());
        transactionSummary.setStatus(transaction.getStatus());
        transactionSummary.setType(transaction.getType());
        return transactionSummary;
    }

    public void endDocument() throws SAXException {
        transactionSearchResult.setTransactionSummaries(transactions);
    }

    public static TransactionSearchResultXMLHandler getHandler(InputStream xml,
            TransactionSearchResult transactionSearchResult) throws ParserConfigurationException, SAXException,
            IOException {

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        InputSource input = new InputSource(xml);
        TransactionSearchResultXMLHandler handler = new TransactionSearchResultXMLHandler(transactionSearchResult);
        parser.parse(input, handler);
        return handler;
    }
}