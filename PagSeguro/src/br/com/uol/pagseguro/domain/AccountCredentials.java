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
package br.com.uol.pagseguro.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Identifies a PagSeguro account
 */
public class AccountCredentials extends Credentials {

    /**
     * Primary email associated with this account
     */
    private String email;

    /**
     * PagSeguro token
     */
    private String token;

    /**
     * Initializes a new instance of the AccountCredentials class
     * 
     * @param email
     * @param token
     */
    public AccountCredentials(String email, String token) {

        if (email == null || email.trim().equals("") || token == null || token.trim().equals("")) {
            throw new RuntimeException("You must enter your credentials when instantiating ReceiverCredentials."
                    + " Currently email=[" + email + "] and token=[" + token + "].");
        }

        this.email = email.trim();
        this.token = token.trim();
    }

    /* (non-Javadoc)
     * @see br.com.uol.pagseguro.domain.Credentials#getAttributes()
     */
    public Map getAttributes() {
        Map attributeMap = new HashMap(3);
        attributeMap.put("email", this.email);
        attributeMap.put("token", this.token);
        return attributeMap;
    }

}
