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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Utility class for XML parsing
 */
public class XMLParserUtils {

    /**
     * Gets XML elements
     * 
     * @param elementTag
     * @param fromElement
     * @return
     */
    public static List getElements(String elementTag, Element fromElement) {
        List elements = new ArrayList();
        NodeList nodeList = fromElement.getElementsByTagName(elementTag);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elements.add((Element) node);
            }
        }
        return elements;
    }

    /**
     * Gets an element from the XML
     * 
     * @param elementTag
     * @param fromElement
     * @return
     */
    public static Element getElement(String elementTag, Element fromElement) {
        Element element = null;
        NodeList nodeList = fromElement.getElementsByTagName(elementTag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element) node;
            }
        }
        return element;
    }

    /**
     * Gets an element value
     * 
     * @param valueTag
     * @param fromElement
     * @return
     */
    public static String getTagValue(String valueTag, Element fromElement) {
        NodeList nodeList = fromElement.getElementsByTagName(valueTag);
        if (nodeList != null && nodeList.getLength() > 0) {
            NodeList childList = nodeList.item(0).getChildNodes();
            if (childList != null && childList.getLength() > 0) {
                Node node = (Node) childList.item(0);
                return node.getNodeValue();
            }
        }
        return null;
    }

    /**
     * Creates a new element
     * 
     * @param doc
     * @param tag
     * @param data
     * @return
     */
    public static Element createElement(Document doc, String tag, String data) {
        Element receiverEmail = doc.createElement(tag);
        if (data != null) {
            Text receiverEmailText = doc.createTextNode(data);
            receiverEmail.appendChild(receiverEmailText);
        }
        return receiverEmail;
    }
}