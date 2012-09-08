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
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.logs.PagSeguroDummyLogger;
import br.com.uol.pagseguro.logs.PagSeguroLoggerFactory;
import br.com.uol.pagseguro.properties.PagSeguroSystem;

/**
 * Parses PaymentRequests and responses
 */
public class PaymentParser {

    /**
     * PagSeguro Log tool
     * @see PagSeguroDummyLogger
     */
    static Logger log =  PagSeguroLoggerFactory.getLogger(PaymentParser.class);

    /**
     * Creates the XML representation of the payment request
     *
     * @param paymentRequest
     * @return
     */
    public static String writePaymentXml(PaymentRequest paymentRequest) {

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element checkout = XMLParserUtils.createElement(doc, "checkout", null);

            // <sender>
            if (paymentRequest.getSender() != null) {
                Element sender = XMLParserUtils.createElement(doc, "sender", null);

                if (paymentRequest.getSender().getName() != null) {
                    Element senderName = XMLParserUtils.createElement(doc, "name", paymentRequest.getSender().getName());
                    sender.appendChild(senderName);
                }

                if (paymentRequest.getSender().getEmail() != null) {
                    Element senderEmail = XMLParserUtils.createElement(doc, "email", paymentRequest.getSender().getEmail());
                    sender.appendChild(senderEmail);
                }

                // <phone>
                if (paymentRequest.getSender().getPhone() != null) {
                    Element senderPhone = XMLParserUtils.createElement(doc, "phone", null);

                    if (paymentRequest.getSender().getPhone().getAreaCode() != null) {
                        Element senderPhoneAreaCode = XMLParserUtils.createElement(doc, "areaCode", paymentRequest.getSender()
                                .getPhone().getAreaCode());
                        senderPhone.appendChild(senderPhoneAreaCode);
                    }

                    if (paymentRequest.getSender().getPhone().getNumber() != null) {
                        Element senderPhoneNumber = XMLParserUtils.createElement(doc, "number", paymentRequest.getSender()
                                .getPhone().getNumber());
                        senderPhone.appendChild(senderPhoneNumber);
                    }
                    sender.appendChild(senderPhone);
                }
                checkout.appendChild(sender);
            }

            // <currency>
            if (paymentRequest.getCurrency() != null) {
                Element currency = XMLParserUtils.createElement(doc, "currency", paymentRequest.getCurrency());
                checkout.appendChild(currency);
            }

            // <redirectURL>
            if (paymentRequest.getRedirectURL() != null) {
                Element redirectURL = XMLParserUtils.createElement(doc, "redirectURL", paymentRequest.getRedirectURL()
                        .toString());
                checkout.appendChild(redirectURL);
            }

            // <items>
            if (paymentRequest.getItems() != null && !paymentRequest.getItems().isEmpty()) {
                Element items = doc.createElement("items");
                for (int j = 0; j < paymentRequest.getItems().size(); j++) {
                    Item i = (Item) paymentRequest.getItems().get(j);

                    // <item>
                    Element item = XMLParserUtils.createElement(doc, "item", null);

                    if (i.getId() != null) {
                        Element itemId = XMLParserUtils.createElement(doc, "id", i.getId());
                        item.appendChild(itemId);
                    }

                    if (i.getDescription() != null) {
                        Element itemDescription = XMLParserUtils.createElement(doc, "description", i.getDescription());
                        item.appendChild(itemDescription);
                    }

                    if (i.getQuantity() != null) {
                        Element itemQuantity = XMLParserUtils
                                .createElement(doc, "quantity", i.getQuantity().toString());
                        item.appendChild(itemQuantity);
                    }

                    if (i.getAmount() != null) {
                    	BigDecimal amount = i.getAmount();
                    	DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
                    	symbols.setDecimalSeparator('.');
                    	DecimalFormat frmt = new DecimalFormat("###.00", symbols);
                    	String formatted = frmt.format(amount.doubleValue());
                        Element itemAmount = XMLParserUtils.createElement(doc, "amount", formatted);
                        item.appendChild(itemAmount);
                    }

                    if (i.getWeight() != null) {
                        Element itemWeight = XMLParserUtils.createElement(doc, "weight", i.getWeight().toString());
                        item.appendChild(itemWeight);
                    }

                    if (i.getShippingCost() != null) {
                        Element itemShippingCost = XMLParserUtils.createElement(doc, "shippingCost", i
                                .getShippingCost().toString());
                        item.appendChild(itemShippingCost);
                    }
                    items.appendChild(item);
                }
                checkout.appendChild(items);
            }
            // <extraAmount>
            if (paymentRequest.getExtraAmount() != null) {
                Element extraAmount = XMLParserUtils.createElement(doc, "extraAmount", paymentRequest.getExtraAmount()
                        .toString());
                checkout.appendChild(extraAmount);
            }

            // <reference>
            if (paymentRequest.getReference() != null) {
                Element reference = XMLParserUtils.createElement(doc, "reference", paymentRequest.getReference().toString());
                checkout.appendChild(reference);
            }

            // <shipping>
            if (paymentRequest.getShipping() != null) {
                Element shipping = XMLParserUtils.createElement(doc, "shipping", null);

                if (paymentRequest.getShipping().getType() != null) {
                    Element shippingType = XMLParserUtils.createElement(doc, "type",
                            Integer.toString(paymentRequest.getShipping().getType().getValue()));
                    shipping.appendChild(shippingType);
                }

                if (paymentRequest.getShipping().getCost() != null) {
                    Element shippingCost = XMLParserUtils.createElement(doc, "cost", paymentRequest.getShipping().getCost()
                            .toString());
                    shipping.appendChild(shippingCost);
                }

                // <address>
                if (paymentRequest.getShipping().getAddress() != null) {
                    Element address = XMLParserUtils.createElement(doc, "address", null);

                    if (paymentRequest.getShipping().getAddress().getStreet() != null) {
                        Element addressStreet = XMLParserUtils.createElement(doc, "street", paymentRequest.getShipping()
                                .getAddress().getStreet());
                        address.appendChild(addressStreet);
                    }

                    if (paymentRequest.getShipping().getAddress().getNumber() != null) {
                        Element addressNumber = XMLParserUtils.createElement(doc, "number", paymentRequest.getShipping()
                                .getAddress().getNumber());
                        address.appendChild(addressNumber);
                    }

                    if (paymentRequest.getShipping().getAddress().getComplement() != null) {
                        Element addressComplement = XMLParserUtils.createElement(doc, "complement", paymentRequest
                                .getShipping().getAddress().getComplement());
                        address.appendChild(addressComplement);
                    }

                    if (paymentRequest.getShipping().getAddress().getCity() != null) {
                        Element addressCity = XMLParserUtils.createElement(doc, "city", paymentRequest.getShipping()
                                .getAddress().getCity());
                        address.appendChild(addressCity);
                    }

                    if (paymentRequest.getShipping().getAddress().getState() != null) {
                        Element addressState = XMLParserUtils.createElement(doc, "state", paymentRequest.getShipping()
                                .getAddress().getState());
                        address.appendChild(addressState);
                    }

                    if (paymentRequest.getShipping().getAddress().getDistrict() != null) {
                        Element addressDistrict = XMLParserUtils.createElement(doc, "district", paymentRequest.getShipping()
                                .getAddress().getDistrict());
                        address.appendChild(addressDistrict);
                    }

                    if (paymentRequest.getShipping().getAddress().getPostalCode() != null) {
                        Element addressPostalCode = XMLParserUtils.createElement(doc, "postalCode", paymentRequest
                                .getShipping().getAddress().getPostalCode());
                        address.appendChild(addressPostalCode);
                    }

                    if (paymentRequest.getShipping().getAddress().getCountry() != null) {
                        Element addressCountry = XMLParserUtils.createElement(doc, "country", paymentRequest.getShipping()
                                .getAddress().getCountry());
                        address.appendChild(addressCountry);
                    }

                    shipping.appendChild(address);
                }
                checkout.appendChild(shipping);
            }

            // <maxAge>
            if (paymentRequest.getMaxAge() != null) {
                Element getMaxAge = XMLParserUtils.createElement(doc, "maxAge", paymentRequest.getMaxAge().toString());
                checkout.appendChild(getMaxAge);
            }
            // <maxUses>
            if (paymentRequest.getMaxUses() != null) {
                Element maxUses = XMLParserUtils.createElement(doc, "maxUses", paymentRequest.getMaxUses().toString());
                checkout.appendChild(maxUses);
            }

            doc.appendChild(checkout);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty("encoding", PagSeguroSystem.getPagSeguroEncoding());
            DOMSource source = new DOMSource(doc);

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);

            return sw.toString();

        } catch (Exception e) {
            log.error("Error while parsing Pagseguro webservice's response");
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the payment request code
     *
     * @param xmlInputStream
     * @return the payment request code
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws ParseException
     * @throws IOException
     */
    public static String readPaymentReturnXml(InputStream xmlInputStream)
            throws ParserConfigurationException, SAXException, ParseException, IOException {

        log.debug("Parsing Payment API response.");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlInputStream);

        Element paymentReturnElement = doc.getDocumentElement();

        String paymentRequestCode = XMLParserUtils.getTagValue("code", paymentReturnElement);

        log.debug("Checkout registered Success! Payment request code: "+ paymentRequestCode);

        return paymentRequestCode;
    }
}