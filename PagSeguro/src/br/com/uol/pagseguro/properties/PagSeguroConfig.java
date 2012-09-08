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

import br.com.uol.pagseguro.domain.AccountCredentials;

/**
 * Encapsulates PagSeguro configuration for API calls
 */
public class PagSeguroConfig {

    private static ResourceBundle resourceBundle;

    static {
        try {
            resourceBundle = ResourceBundle.getBundle("pagseguro-config", Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To activate the PagSeguro logging tool, set the <b>log.path<b> property in <b>pagseguro-config.properties</b> file.
     * 
     * @return the path to PagSeguro log file
     */
    public static String getLogPath() {
        return resourceBundle.getString("log.path");
    }
    
    public static String getLoggerImplementation() {
        return resourceBundle.getString("logger.implementation");
    }

    /**
     * Account credentials read from config file <b>pagseguro-config.properties</b>
     * To read the account credentials from config, you have to set <b>credential.email</b> and <b>credential.token</b> 
     * in the <b>pagseguro-config.properties</b> file
     * @return the account credentials read from <b>pagseguro-config.properties</b> file.
     * 
     */
    public static AccountCredentials getAccountCredentials() {

        String email = resourceBundle.getString("credential.email");
        String token = resourceBundle.getString("credential.token");

        email = email == null ? null : email.trim();
        token = token == null ? null : token.trim();

        //it is validated at this point to put a error message in the exception
        if (email == null || email.equals("") || token == null || token.equals("")) {
            throw new RuntimeException("To use credentials from config.properties file you must "
                    + "configure the properties credential.email and credential.token. Currently "
                    + "credential.email=[" + email + "] and credential.token=[" + token + "].");
        }

        return new AccountCredentials(email, token);

    }

}
