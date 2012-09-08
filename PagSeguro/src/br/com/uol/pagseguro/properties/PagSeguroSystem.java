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
package br.com.uol.pagseguro.properties;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Helper to get values from properties
 * 
 */
public class PagSeguroSystem {

    private static ResourceBundle resourceBundle;

    static {
        try {
            resourceBundle = ResourceBundle.getBundle("pagseguro-system", Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUrlNotification() {
        return resourceBundle.getString("url.notification");
    }

    public static String getUrlPayment() {
        return resourceBundle.getString("url.payment");
    }

    public static String getUrlPaymentRedir() {
        return resourceBundle.getString("url.payment.redir");
    }

    public static String getUrlSearch() {
        return resourceBundle.getString("url.search");
    }

    public static String getContentTypeFormUrlEncoded() {
        return resourceBundle.getString("contentType.formUrlEncoded");
    }

    public static String getContentTypeXml() {
        return resourceBundle.getString("contentType.xml");
    }

    public static String getPagSeguroEncoding() {
        return resourceBundle.getString("encoding");
    }

}
