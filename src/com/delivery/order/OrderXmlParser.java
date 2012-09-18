package com.delivery.order;

import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.delivery.Logger;
import com.delivery.account.Address;
import com.delivery.util.StringUtils;
import com.delivery.util.XmlParserUtils;

public class OrderXmlParser {

	public static String writeXmlOrder(Order order) {
		try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            //<order>
            Element orderElement = XmlParserUtils.createElement(doc, "order", null);

            //<id>
            Element idElement = XmlParserUtils.createElement(doc, "id", String.valueOf(order.getId()));
            orderElement.appendChild(idElement);

            // <address>
            if (order.getAddress() != null) {
            	Address addr = order.getAddress();
                Element addressElement = XmlParserUtils.createElement(doc, "address", null);
                //<street>
                Element streetElement = XmlParserUtils.createElement(doc, "street", addr.getStreet());
                addressElement.appendChild(streetElement);
                //<number>
                Element numberElement = XmlParserUtils.createElement(doc, "number", String.valueOf(addr.getNumber()));
                addressElement.appendChild(numberElement);
                if (!StringUtils.isEmpty(addr.getCompl())){
                	//<complement>
                    Element complementElement = XmlParserUtils.createElement(doc, "complement", addr.getCompl());
                    addressElement.appendChild(complementElement);

                }
                //<district>
                Element districtElement = XmlParserUtils.createElement(doc, "district", addr.getNeighborhood());
                addressElement.appendChild(districtElement);
                //<city>
                Element cityElement = XmlParserUtils.createElement(doc, "city", addr.getCity());
                addressElement.appendChild(cityElement);
                //<uf>
                Element ufElement = XmlParserUtils.createElement(doc, "uf", addr.getUF());
                addressElement.appendChild(ufElement);
                //<zip>
                Element zipElement = XmlParserUtils.createElement(doc, "zip", addr.getZipCode());
                addressElement.appendChild(zipElement);

                orderElement.appendChild(addressElement);
            }
            Payment payment = order.getPayment();
            if (payment != null) {
            	//<charge>
                int chargeValue = payment.getManualPayment() ? 1 : 0;
            	Element chargeElement = XmlParserUtils.createElement(doc, "charge", String.valueOf(chargeValue));
            	orderElement.appendChild(chargeElement);
            	if (chargeValue == 1) {
            		//<change>
            		double change = payment.getManualPaymentValue() - order.getPrice();
            		Element changeElement = XmlParserUtils.createElement(doc, "change", String.valueOf(change));
            		orderElement.appendChild(changeElement);
            	}
            }

            // O cliente mobile nao precisa saber a estrutura do pedido
            // O conteudo do pedido (Os itens) serao enviados em formato de texto,
            // como descricao (Apenas informativo)
            StringBuilder orderDescription = new StringBuilder();
            List<OrderItem> orderItems = order.getItems();
    		for (OrderItem item : orderItems) {
    			StringBuilder itemDescription = new StringBuilder();
    			itemDescription.append(item.getDescription());
    			Collection<String> flavours = item.getFlavours().values();
    			itemDescription.append(" - " + item.getCachedSizeName() + " -");
    			boolean first = true;
    			for (String f : flavours) {
    				if (first) {
    					first = false;
    				} else {
    					itemDescription.append(",");
    				}
    				itemDescription.append(" " + f);
    			}
    			Map<Integer, String> optionals = item.getOptionals();
    			if (optionals != null && optionals.size() > 0) {
    				itemDescription.append(" -");
    				first = true;
    				for (String optional : optionals.values()) {
    					if (first) {
    						first = false;
    					} else {
    						itemDescription.append(",");
    					}
    					itemDescription.append(" " + optional);
    				}
    			}

    			orderDescription.append(itemDescription.toString() + "\n");
    		}

            //<description>
            Element description = XmlParserUtils.createElement(doc, "description", orderDescription.toString());
            orderElement.appendChild(description);


            doc.appendChild(orderElement);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty("encoding", "ISO-8859-1");
            DOMSource source = new DOMSource(doc);

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);

            return sw.toString();

        } catch (Exception e) {
        	Logger.error("Erro ao criar xml de pedido", e);
            throw new RuntimeException(e);
        }
	}
}
