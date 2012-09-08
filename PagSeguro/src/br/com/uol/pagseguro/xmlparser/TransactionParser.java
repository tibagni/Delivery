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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.Address;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.PaymentMethod;
import br.com.uol.pagseguro.domain.PaymentMethodCode;
import br.com.uol.pagseguro.domain.PaymentMethodType;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.Shipping;
import br.com.uol.pagseguro.domain.ShippingType;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.TransactionStatus;
import br.com.uol.pagseguro.domain.TransactionType;
import br.com.uol.pagseguro.logs.PagSeguroDummyLogger;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.logs.PagSeguroLoggerFactory;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import br.com.uol.pagseguro.service.TransactionSearchService;
import br.com.uol.pagseguro.util.DateParserUTC;

/**
 * Parses a transaction XML in a <b>Transaction</b> object
 * @see Transaction 
 */
public class TransactionParser {

    /**
     * PagSeguro Log tool
     * @see Logger
     */
    static Logger log =  PagSeguroLoggerFactory.getLogger(TransactionSearchService.class);

    /**
     * Parses the XML response form PagSeguro web services
     * 
     * @param xmlInputStream
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ParseException
     */
    public static Transaction readTransaction(InputStream xmlInputStream) throws ParserConfigurationException,
            SAXException, IOException, ParseException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        InputSource is = new InputSource(xmlInputStream);
        is.setEncoding(PagSeguroSystem.getPagSeguroEncoding());
        Document doc = dBuilder.parse(is);
        String tagValue = null;

        Element transactionElement = doc.getDocumentElement();
        Transaction transaction = new Transaction();

        tagValue = XMLParserUtils.getTagValue("code", transactionElement);
        if (tagValue != null) {
            transaction.setCode(tagValue);
        }

		log.debug("Parsing transaction");

        tagValue = XMLParserUtils.getTagValue("date", transactionElement);
        if (tagValue != null) {
            transaction.setDate(DateParserUTC.parse(tagValue));
        }

        tagValue = XMLParserUtils.getTagValue("code", transactionElement);
        if (tagValue != null) {
            transaction.setCode(tagValue);
        }

        tagValue = XMLParserUtils.getTagValue("reference", transactionElement);
        if (tagValue != null) {
            transaction.setReference(tagValue);
        }

        tagValue = XMLParserUtils.getTagValue("type", transactionElement);
        if (tagValue != null) {
            transaction.setType(TransactionType.fromValue(Integer.valueOf(tagValue)));
        }

        tagValue = XMLParserUtils.getTagValue("status", transactionElement);
        if (tagValue != null) {
            transaction.setStatus(TransactionStatus.fromValue(Integer.valueOf(tagValue)));
        }

        // paymentMethod
        Element paymentMethodElement = XMLParserUtils.getElement("paymentMethod", transactionElement);
        if (paymentMethodElement != null) {
            PaymentMethod paymentMethod = new PaymentMethod();

            tagValue = XMLParserUtils.getTagValue("type", paymentMethodElement);
            if (tagValue != null) {
                paymentMethod.setType(PaymentMethodType.fromValue(Integer.valueOf(tagValue)));
            }

            tagValue = XMLParserUtils.getTagValue("code", paymentMethodElement);
            if (tagValue != null) {
                paymentMethod.setCode(PaymentMethodCode.fromValue(Integer.valueOf(tagValue)));
            }
            transaction.setPaymentMethod(paymentMethod);

        }

        tagValue = XMLParserUtils.getTagValue("grossAmount", transactionElement);
        if (tagValue != null) {
            transaction.setGrossAmount(new BigDecimal(tagValue));
        }

        tagValue = XMLParserUtils.getTagValue("discountAmount", transactionElement);
        if (tagValue != null) {
            transaction.setDiscountAmount(new BigDecimal(tagValue));
        }

        tagValue = XMLParserUtils.getTagValue("feeAmount", transactionElement);
        if (tagValue != null) {
            transaction.setFeeAmount(new BigDecimal(tagValue));
        }

        tagValue = XMLParserUtils.getTagValue("netAmount", transactionElement);
        if (tagValue != null) {
            transaction.setNetAmount(new BigDecimal(tagValue));
        }

        tagValue = XMLParserUtils.getTagValue("extraAmount", transactionElement);
        if (tagValue != null) {
            transaction.setExtraAmount(new BigDecimal(tagValue));
        }

        tagValue = XMLParserUtils.getTagValue("lastEventDate", transactionElement);
        if (tagValue != null) {
            transaction.setLastEventDate(DateParserUTC.parse(tagValue));
        }

        tagValue = XMLParserUtils.getTagValue("installmentCount", transactionElement);
        if (tagValue != null) {
            transaction.setInstallmentCount(Integer.valueOf(tagValue));
        }

        // items
        Element itemsElement = XMLParserUtils.getElement("items", transactionElement);
        if (itemsElement != null) {
            List itElements = XMLParserUtils.getElements("item", itemsElement);
            List items = new ArrayList();
            Item item;
            for (int i = 0; i < itElements.size(); i++) {
                Element itElement = (Element) itElements.get(i);
                item = new Item();

                tagValue = XMLParserUtils.getTagValue("id", itElement);
                if (tagValue != null) {
                    item.setId(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("description", itElement);
                if (tagValue != null) {
                    item.setDescription(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("quantity", itElement);
                if (tagValue != null) {
                    item.setQuantity(Integer.valueOf(tagValue));
                }

                tagValue = XMLParserUtils.getTagValue("amount", itElement);
                if (tagValue != null) {
                    item.setAmount(new BigDecimal(tagValue));
                }

                tagValue = XMLParserUtils.getTagValue("weight", itElement);
                if (tagValue != null) {
                    item.setWeight(Long.valueOf(tagValue));
                }
                items.add(item);
            }
            transaction.setItems(items);
        }

        // sender
        Element senderElement = XMLParserUtils.getElement("sender", transactionElement);
        if (senderElement != null) {
            Sender sender = new Sender();

            tagValue = XMLParserUtils.getTagValue("name", senderElement);
            if (tagValue != null) {
                sender.setName(tagValue);
            }

            tagValue = XMLParserUtils.getTagValue("email", senderElement);
            if (tagValue != null) {
                sender.setEmail(tagValue);
            }

            // phone
            Element phoneElement = XMLParserUtils.getElement("phone", senderElement);
            if (phoneElement != null) {
                Phone phone = new Phone();

                tagValue = XMLParserUtils.getTagValue("areaCode", phoneElement);
                if (tagValue != null) {
                    phone.setAreaCode(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("number", phoneElement);
                if (tagValue != null) {
                    phone.setNumber(tagValue);
                }
                sender.setPhone(phone);
            }
            transaction.setSender(sender);
        }

        // shipping
        Element shippingElement = XMLParserUtils.getElement("shipping", transactionElement);
        if (shippingElement != null) {
            Shipping shipping = new Shipping();

            tagValue = XMLParserUtils.getTagValue("type", shippingElement);
            if (tagValue != null) {
                shipping.setType(ShippingType.fromValue(Integer.valueOf(tagValue)));
            }

            tagValue = XMLParserUtils.getTagValue("cost", shippingElement);
            if (tagValue != null) {
                shipping.setCost(new BigDecimal(tagValue));
            }

            // address
            Element addressElement = XMLParserUtils.getElement("address", shippingElement);
            if (addressElement != null) {
                Address address = new Address();

                tagValue = XMLParserUtils.getTagValue("street", addressElement);
                if (tagValue != null) {
                    address.setStreet(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("number", addressElement);
                if (tagValue != null) {
                    address.setNumber(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("complement", addressElement);
                if (tagValue != null) {
                    address.setComplement(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("city", addressElement);
                if (tagValue != null) {
                    address.setCity(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("state", addressElement);
                if (tagValue != null) {
                    address.setState(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("district", addressElement);
                if (tagValue != null) {
                    address.setDistrict(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("postalCode", addressElement);
                if (tagValue != null) {
                    address.setPostalCode(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("country", addressElement);
                if (tagValue != null) {
                    address.setCountry(tagValue);
                }
                shipping.setAddress(address);
            }
            transaction.setShipping(shipping);
        }

        log.debug("Parsing transaction success: " + transaction.getCode());

        return transaction;
    }
}
