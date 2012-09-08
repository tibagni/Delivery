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
package br.com.uol.pagseguro.infra;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.domain.HttpError;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.logs.PagSeguroDummyLogger;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.logs.PagSeguroLoggerFactory;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import br.com.uol.pagseguro.xmlparser.ErrorsParser;

/**
 * Encapsulates web service calls
 */
public class HttpsURLConnectionUtil {

    /**
     * PagSeguro Log tool
     * @see PagSeguroDummyLogger
     */
    static Logger log =  PagSeguroLoggerFactory.getLogger(HttpsURLConnectionUtil.class);

	private static final String HTTP_METHOD_POST = "POST";

	private static final String HTTP_METHOD_GET = "GET";

    /**
     * Creates a http connection and makes a remote call 
     * 
     * @param method
     * @param urlPath
     * @param contentType
     * @param xml
     * @return
     * @throws PagSeguroServiceException
     */
    private static HttpsURLConnection getHttpsURLConnection(String method, String urlPath, String contentType,
            String xml) throws PagSeguroServiceException {

        log.debug("method: " + method + ", content-type:" + contentType + ", target URL: " + urlPath);

        URL url;
        HttpsURLConnection connection;
        try {

            // Creates a connection
            url = new URL(urlPath);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-type", contentType);

            // write the XML
            if (xml != null && !xml.equals("")) {
                log.debug("sending XML: " + xml);
                connection.getOutputStream().write(xml.getBytes(PagSeguroSystem.getPagSeguroEncoding()));
            }

            // handle the response
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                if (responseCode == HttpsURLConnection.HTTP_BAD_REQUEST) {

                    List errors = ErrorsParser.readErrosXml(connection.getErrorStream());
                    log(errors);

                    throw new PagSeguroServiceException(errors, HttpError.fromValue(responseCode));
                }

                log.error("HTTP: " + responseCode + ". Error during PagSeguro API call.");
                throw new PagSeguroServiceException(HttpError.fromValue(responseCode));
            }

            log.debug("API call success: " + responseCode);

            // returns the http connection
            return connection;

        } catch (IOException e) {
            log.error("Error while parsing the response", e);
        } catch (ParserConfigurationException e) {
            log.error("Error while parsing the response", e);
        } catch (SAXException e) {
            log.error("Error while parsing the response", e);
        }

        return null;

    }

    /**
     * makes a post request in the informed URL
     * 
     * @param urlPath
     * @param contentType
     * @param xml
     * @return
     * @throws PagSeguroServiceException
     */
    public static HttpsURLConnection getHttpsPostConnection(String urlPath, String contentType, String xml)
            throws PagSeguroServiceException {
        return getHttpsURLConnection(HTTP_METHOD_POST, urlPath, contentType, xml);
    }

    /**
     * makes a get request n the informed URL
     * 
     * @param urlPath
     * @param contentType
     * @return
     * @throws PagSeguroServiceException
     */
    public static HttpsURLConnection getHttpsGetConnection(String urlPath, String contentType)
            throws PagSeguroServiceException {
        return getHttpsURLConnection(HTTP_METHOD_GET, urlPath, contentType, null);
    }

    private static void log(List errors) {
        String print = "HTTP: " + HttpsURLConnection.HTTP_BAD_REQUEST + ". Validation error in PagSeguro webservice: \n";

        for (int i = 0; i < errors.size(); i++) {
            Error error = (Error) errors.get(i);
            print += "> ";
            print += error.getCode() + " - " + error.getMessage();
            print += "\n";
        }

        log.error(print);
    }
}
